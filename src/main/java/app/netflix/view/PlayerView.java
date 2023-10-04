package app.netflix.view;

import app.netflix.Main;
import com.sun.javafx.application.PlatformImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import lombok.Getter;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerView extends JFXPanel {

    private Stage stage;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private Dimension backDimension = new Dimension(1280, 680);

    public PlayerView(Media media, Runnable e, Runnable a){
        PlatformImpl.startup(() -> {
            stage = new Stage();
            stage.setTitle("Viewer");
            stage.setResizable(true);
            Group root = new Group();
            Scene scene;
            if(getWidth() > 0 && getHeight() > 0) {
                scene = new Scene(root, this.getWidth(), this.getHeight());
            } else{
                scene = new Scene(root);
            }
            this.mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            this.mediaView = new MediaView(mediaPlayer);
            stage.setScene(scene);
            root.getChildren().add(mediaView);
            this.setScene(scene);

            mediaPlayer.setOnEndOfMedia(e);
            mediaPlayer.setOnStopped(e);
            mediaPlayer.setOnPaused(e);
            mediaPlayer.setOnReady(a);
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    try {
                        if(media.getWidth() > 0 && media.getHeight() > 0) {
                            backDimension = (Dimension) Main.window.getSize();
                            Main.window.setSize(new Dimension(media.getWidth(), media.getHeight()));
                        }
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if(e.getKeyChar() == 'e'){
                        mediaPlayer.stop();
                    }
                }
            });
        });
    }
}
