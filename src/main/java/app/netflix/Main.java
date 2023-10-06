package app.netflix;

import app.netflix.manager.AccountManager;
import app.netflix.manager.MovieManager;
import app.netflix.manager.WatchManager;
import app.netflix.model.Account;
import balbucio.discordoauth.DiscordOAuth;
import balbucio.discordoauth.scope.SupportedScopes;
import balbucio.responsivescheduler.RSTask;
import balbucio.responsivescheduler.ResponsiveScheduler;
import balbucio.sqlapi.sqlite.HikariSQLiteInstance;
import balbucio.sqlapi.sqlite.SqliteConfig;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.user.DiscordUser;
import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.model.Form;
import info.movito.themoviedbapi.TmdbApi;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        ArgumentParser parser = ArgumentParsers.newFor("NetflixClone")
                .build().defaultHelp(true)
                .description("Opcoes avancadas do Netflix Clone");
        parser.addArgument("--login")
                .choices("ON", "OFF")
                .setDefault("ON")
                .help("Ativa o login");
        parser.addArgument("--discordrpc")
                .help("Ativa o DiscordRPC");

        Namespace ns = parser.parseArgs(args);

        if(ns.getAttrs().containsKey("login")){
            AppInfo.USE_LOGIN = ns.getString("login").equalsIgnoreCase("ON");
        }

        if(ns.getAttrs().containsKey("discordrpc")){
            AppInfo.LOAD_RPC = true;
        }

        new Main();
    }

    private static File configFile = new File("config.yml");
    public static YamlConfiguration config;
    public static UiBooster uiBooster;
    public static ResponsiveScheduler scheduler;
    public static TmdbApi tmdbApi;
    public static HikariSQLiteInstance sqlite;
    public static DiscordOAuth discordOAuth;
    public static boolean firstLoad = true;
    public static boolean allLoaded = true;
    public static Window window;
    public static MovieManager movieManager;
    public static AccountManager accountManager;
    public static WatchManager watchManager;

    public Main() throws Exception{
        uiBooster = new UiBooster();
        scheduler = new ResponsiveScheduler();
        loadConfig();
        SqliteConfig c = new SqliteConfig(new File("database.db"));
        c.setMaxRows(20);
        c.createFile();
        this.sqlite = new HikariSQLiteInstance(c);


        discordOAuth = new DiscordOAuth(config.getString("discordClient"),
                config.getString("discordSecret"),  "https://netflixclone.com/netflix/discordlogin", SupportedScopes.values());

        tmdbApi = new TmdbApi(config.getString("apiKey"));
        movieManager = new MovieManager();
        accountManager = new AccountManager(config.getString("loggedId"));
        watchManager = new WatchManager();

        window = new Window();

        if(AppInfo.LOAD_RPC) {
            System.out.println("*** AVISO IMPORTANTE ***\n" +
                    "O DiscordRPC foi ativado, tentaremos conectar ao seu Discord pelo Aplicaito.\n" +
                    "Caso o aplicativo feche imediatamente, DESATIVE esta feature.");
            loadDiscordRPC();
        }

        movieManager.loadAll();

        if(!AppInfo.LOGGED && AppInfo.USE_LOGIN){
            accountManager.openDiscordLogin();
        } else {
            window.loadMainView();
        }
    }

    private void loadConfig() throws Exception{
        if(!configFile.exists()){
            firstLoad = true;
            Files.copy(this.getClass().getResourceAsStream("/config.yml"), configFile.toPath());
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        if(config.getString("apiKey").equals("null")){
            firstLoad = true;
            Form keyForm = uiBooster.createForm("Suas chaves de API")
                    .addLabel("Seja bem-vindo ao Netflix Clone!")
                    .addLabel("Informe suas credenciais do The Movie Database.")
                    .addText("Insira a chave da API:")
                    .addText("Insira a chave de autenticação Bearer:")
                    .addText("Insira o Discord Client ID:")
                    .addText("Insira o Discord Secret:")
                    .addCheckbox("Salvar e não exibir mais?")
                    .addButton("Não tem uma chave? Crie uma!", () -> {
                        try {
                            URI link = new URI("https://www.themoviedb.org/settings/api");
                            Desktop.getDesktop().browse(link);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    })
                    .show();
            config.set("apiKey", keyForm.getByIndex(2).asString());
            config.set("apiBearer", keyForm.getByIndex(3).asString());
            config.set("discordClient", keyForm.getByIndex(4).asString());
            config.set("discordSecret", keyForm.getByIndex(5).asString());
            if((boolean) keyForm.getByIndex(6).getValue()) {
                config.save(configFile);
            }
        }
    }

    public static Core discordCore;

    public void loadDiscordRPC() {
        try {
            window.getLoadView().setLoadText("Loading native libraries and trying to connect you automatically...");
            Core.initFromClasspath();
            CreateParams params = new CreateParams();
            params.setClientID(Long.parseLong(config.getString("discordClient")));
            params.setFlags(CreateParams.Flags.DEFAULT);
            params.registerEventHandler(new DiscordEventAdapter() {
                @Override
                public void onCurrentUserUpdate() {
                    if(!AppInfo.LOGGED) {
                        AppInfo.LOGGED = true;
                        DiscordUser currentUser = discordCore.userManager().getCurrentUser();
                        accountManager.loadUser(currentUser);
                        if(allLoaded){
                            window.loadMainView();
                        }
                    }

                }
            });

            scheduler.runAsyncTask(new RSTask() {
                @Override
                public void run() {
                    discordCore = new Core(params);
                    while(true){
                        discordCore.runCallbacks();
                    }
                }
            });
        } catch (Exception e ){
            e.printStackTrace();
        }
    }

    public static void setConfig(String key, Object obj){
        try {
            config.set(key, obj);
            config.save(configFile);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
