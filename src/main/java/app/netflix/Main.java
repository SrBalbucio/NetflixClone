package app.netflix;

import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.model.Form;
import info.movito.themoviedbapi.TmdbApi;
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
    public static TmdbApi tmdbApi;
    private Window window;

    public Main() throws Exception{
        uiBooster = new UiBooster();
        loadConfig();
        this.window = new Window();
        loadMovies();
    }

    private void loadConfig() throws Exception{
        if(!configFile.exists()){
            Files.copy(this.getClass().getResourceAsStream("/config.yml"), configFile.toPath());
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        if(config.getString("apiKey").equals("null")){
            Form keyForm = uiBooster.createForm("Suas chaves de API")
                    .addLabel("Seja bem-vindo ao Netflix Clone!")
                    .addLabel("Informe suas credenciais do The Movie Database.")
                    .addText("Insira a chave da API:")
                    .addText("Insira a chave de autenticação Bearer:")
                    .addCheckbox("Manter salvo para não requisitar novamente?")
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
        tmdbApi = new TmdbApi(config.getString("apiKey"));

    }
}
