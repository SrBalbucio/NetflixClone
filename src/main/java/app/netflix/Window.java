package app.netflix;

import app.netflix.view.LoadView;
import balbucio.glasslibrary.GlassFrame;

import javax.swing.*;
import java.awt.*;

public class Window extends GlassFrame {

    private CardLayout cardLayout = new CardLayout();

    public Window(){
        super("Netflix Clone - Unoficial Client");
        this.setSize(640, 480);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel content = new JPanel(cardLayout);
        content.add(new LoadView(), "LOAD");
        this.layout(new BorderLayout());
        this.addComponent(content, BorderLayout.CENTER);
        this.setVisible(true);
        cardLayout.show(content, "LOAD");
    }
}
