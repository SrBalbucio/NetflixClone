package app.netflix.view;

import app.netflix.AppInfo;
import app.netflix.Main;
import app.netflix.Window;
import balbucio.discordoauth.DiscordAPI;
import balbucio.discordoauth.model.TokensResponse;
import balbucio.discordoauth.model.User;
import com.sun.javafx.application.PlatformImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Data;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

@Data
public class DiscordLoginView extends JPanel {
    private Stage stage;
    private WebView browser;
    private JFXPanel jfxPanel;
    @Getter
    public boolean isFinished = false;

    private int w, h;
    public DiscordLoginView(){
        this.setLayout(new BorderLayout());
        JPanel info = new JPanel(new FlowLayout(FlowLayout.CENTER));
        info.add(new JLabel("Faça login no Discord para continuar."));
        this.add(info, BorderLayout.NORTH);
        this.jfxPanel = new JFXPanel();
        PlatformImpl.startup(() -> {

            stage = new Stage();

            stage.setTitle("Hello Java FX");
            stage.setResizable(true);

            Group root = new Group();
            Scene scene = new Scene(root, jfxPanel.getWidth(), jfxPanel.getHeight());
            stage.setScene(scene);

            browser = new WebView();
            browser.getEngine().setJavaScriptEnabled(true);
            browser.getEngine().setUserDataDirectory(new File("browser"));
            browser.getEngine().load(Main.discordOAuth.getAuthorizationURL("netflixclone"));
            browser.getEngine().getLoadWorker().stateProperty().addListener((observableValue, state, t1) -> {
                if (state == Worker.State.SCHEDULED || state == Worker.State.RUNNING || state == Worker.State.READY) {
                    System.out.println(browser.getEngine().getLocation());
                    if (browser.getEngine().getLocation().contains("netflixclone.com/netflix/discordlogin")) {
                        String[] urlPartener = browser.getEngine().getLocation().replace("https://netflixclone.com/netflix/discordlogin?", "").split("&");
                        for (String part : urlPartener) {
                            String[] p = part.split("=");
                            if (p[0].equalsIgnoreCase("code")) {
                                processCode(p[1]);
                            }
                        }
                    }
                }
            });
            root.getChildren().add(browser);
            browser.setMaxSize(jfxPanel.getWidth(), jfxPanel.getHeight());
            browser.setMinSize(jfxPanel.getWidth(), jfxPanel.getHeight());
            jfxPanel.setScene(scene);
        });
        this.add(jfxPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private boolean loaded = false;

    public void processCode(String code){
        if(loaded){
            return;
        }
        Main.window.getLoadView().setLoadText("Loading your Discord Account...");
        Main.window.show("LOAD");
        Main.window.getContent().remove(this);
        try {
            System.out.println(code);
            TokensResponse tokens = Main.discordOAuth.getTokens(code);
            DiscordAPI api = new DiscordAPI(tokens.getAccessToken());
            System.out.println("a");
            User user = api.fetchUser();
            System.out.println("b");
            AppInfo.LOGGED = true;
            AppInfo.ACCOUNT = Main.accountManager.loadUser(user);
            Main.setConfig("loggedId", user.getId());
            isFinished = true;
            Main.window.loadMainView();
        } catch (Exception e){
            e.printStackTrace();
            Main.window.getLoadView().setLoadText("Ops... It looks like your login via Discord went wrong.");
            Main.uiBooster.showConfirmDialog(
                    "Parece que o login interno via Discord ocorreu mal.\n" +
                            "Possíveis motivos:\n - Rede não segura\n - Instalação inválida do Java e do JFX\n - Problemas de dependência\n\n" +
                            "Como alternativa você pode abrir o aplicativo do Discord ou\n" +
                            "tentar criar uma conta manualmente.", "Login via discord inválido!");
        }
    }
}
