package app.netflix;

import app.netflix.manager.MovieManager;
import balbucio.responsivescheduler.ResponsiveScheduler;
import balbucio.sqlapi.sqlite.HikariSQLiteInstance;
import balbucio.sqlapi.sqlite.SqliteConfig;
import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.model.Form;
import info.movito.themoviedbapi.TmdbApi;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main();
    }

    private File configFile = new File("config.yml");
    private YamlConfiguration config;
    public static UiBooster uiBooster;
    public static ResponsiveScheduler scheduler;
    public static TmdbApi tmdbApi;
    public static HikariSQLiteInstance sqlite;
    public static boolean firstLoad = true;
    public static Window window;
    public static MovieManager movieManager;

    public Main() throws Exception{
        uiBooster = new UiBooster();
        scheduler = new ResponsiveScheduler();
        loadConfig();
        SqliteConfig c = new SqliteConfig(new File("database.db"));
        c.setMaxRows(20);
        c.createFile();
        this.sqlite = new HikariSQLiteInstance(c);

        tmdbApi = new TmdbApi(config.getString("apiKey"));
        movieManager = new MovieManager();

        window = new Window();

        movieManager.loadAll();
        window.loadMainView();
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
            System.out.println(config.get("apiKey"));
            if((boolean) keyForm.getByIndex(4).getValue()) {
                config.save(configFile);
            }
        }
    }

    private void loadMovies(){
    }
}
