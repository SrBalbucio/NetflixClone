package app.netflix;

import app.netflix.view.ListaView;
import app.netflix.view.LoadView;
import app.netflix.view.MainView;
import balbucio.glasslibrary.GlassFrame;
import balbucio.org.ejsl.utils.ImageUtils;
import javafx.scene.image.Image;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class Window extends GlassFrame {

    private CardLayout cardLayout = new CardLayout();
    @Getter
    private LoadView loadView = new LoadView();
    @Getter
    private MainView mainView;
    @Getter
    private ListaView listaView;
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
        content.add(mainView, "HOME");
        content.add(listaView, "LISTA");
        loadView.setVisible(false);
        show("HOME");
    }

    public void show(String page) {
        cardLayout.show(content, page);
    }
}
