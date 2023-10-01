package app.netflix.view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PlayerView extends JFXPanel {

    private Stage stage;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    public PlayerView(Media media){
        stage.setTitle("mpay Viewer");
        stage.setResizable(true);
        Group root = new Group();
        Scene scene = new Scene(root, this.getWidth(), this.getHeight());
        this.mediaPlayer = new MediaPlayer(media);
        this.mediaView = new MediaView(mediaPlayer);
        stage.setScene(scene);
        root.getChildren().add(mediaView);
        mediaView.setFitHeight(this.getHeight());
        mediaView.setFitWidth(this.getWidth());
        this.setScene(scene);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mediaView.setFitHeight(getHeight());
                mediaView.setFitWidth(getWidth());
            }
        });
    }
}
