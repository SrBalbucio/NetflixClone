package app.netflix;

import app.netflix.view.*;
import balbucio.glasslibrary.GlassFrame;
import balbucio.org.ejsl.utils.ImageUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Window extends GlassFrame {

    private CardLayout cardLayout = new CardLayout();
    @Getter
    private LoadView loadView = new LoadView();
    @Getter
    private MainView mainView;
    @Getter
    private ListaView listaView;
    @Getter
    private ForYouView forYouView;
    @Getter
    private JPanel content;

    public Window() {
        super("Netflix Clone - Unoficial Client");
        this.setSize(1280, 700);
        this.setIconImage(ImageUtils.resizeImage(ImageUtils.setFitCenter(AppInfo.NETFLIX_SYMBOL, 48, 48), 48, 48, Color.BLACK));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        content = new JPanel(cardLayout);
        content.add(loadView, "LOAD");
        this.layout(new BorderLayout());
        this.addComponent(content, BorderLayout.CENTER);
        this.setVisible(true);
        cardLayout.show(content, "LOAD");
    }

    public void loadMainView() {
        mainView = new MainView();
        listaView = new ListaView();
        forYouView = new ForYouView();
        content.add(mainView, "HOME");
        content.add(listaView, "LISTA");
        content.add(forYouView, "FORYOU");
        long startLoading = System.currentTimeMillis();
        int i = 4;
        for (int i1 = 1; i1 < i; i1++) {
            File trailer = new File("trailer0" + i1 + ".mp4");
            if (!trailer.exists()) {
                try {
                    Files.copy(this.getClass().getResourceAsStream("/trailer/trailer0" + i1 + ".mp4"), trailer.toPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        PlayerView view = new PlayerView(new Media(new File("trailer01.mp4").toURI().toString()),
                () -> {
                    Main.window.show("HOME");
                    Main.allLoaded = true;
                }, () -> {
            show("NETFLIXLOAD");
        });
        content.add(view, "NETFLIXLOAD");
        if ((startLoading - System.currentTimeMillis()) > 6000) {
            show("NETFLIXLOAD");
        }
    }

    public void show(String page) {
        cardLayout.show(content, page);
    }
}
