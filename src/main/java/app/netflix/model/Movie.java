package app.netflix.model;

import balbucio.org.ejsl.utils.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@AllArgsConstructor
@Data
public class Movie {

    private int id;
    private String name;
    private String description;
    private int genre;
    private float popularity;
    private String poster;
    private String backdrop;
    private String data;
    private JSONObject json;

    public Movie(int id, String name, String description, int genre, float popularity, String poster, String backdrop, String data) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.popularity = popularity;
        this.poster = poster;
        this.backdrop = backdrop;
        this.data = data;
        this.json = new JSONObject(data);
    }

    public Image getPosterImage() {
        System.out.println(poster);
        File imgFile = new File("cache"+poster);
        imgFile.getParentFile().mkdirs();
        if(!imgFile.exists()) {
            try (BufferedInputStream in = new BufferedInputStream(new URL("https://image.tmdb.org/t/p/w200" + poster).openStream())) {
                imgFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
                byte dataBuffer[] = new byte[1024];

                int bytesRead;

                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
        return ImageUtils.getImage(imgFile);
    }

    public Image getBackdropImage() {
        File imgFile = new File("cache"+backdrop);
        if(!imgFile.exists()) {
            try (BufferedInputStream in = new BufferedInputStream(new URL("https://image.tmdb.org/t/p/w200" + backdrop).openStream())) {

                FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
                byte dataBuffer[] = new byte[1024];

                int bytesRead;

                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {}
        }
        return ImageUtils.getImage(imgFile);
    }
}
