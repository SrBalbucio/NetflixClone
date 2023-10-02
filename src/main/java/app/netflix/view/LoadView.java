package app.netflix.view;

import app.netflix.AppInfo;
import app.netflix.lib.JPanelLambda;
import balbucio.org.ejsl.component.JImage;
import balbucio.org.ejsl.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class LoadView extends JPanel{

    private JImage logo;
    private JLabel progressBar;
    private JLabel loadText;

    public LoadView(){
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.BLACK);
        JPanel content = new JPanel();
        content.setBackground(Color.BLACK);
        BoxLayout bl = new BoxLayout(content, BoxLayout.Y_AXIS);
        content.setLayout(bl);
        logo = new JImage(AppInfo.NETFLIX_SYMBOL);
        logo.setMaxSize(true);
        logo.setCenter(true);
        logo.setPreferredSize(new Dimension(240, 240));
        progressBar = new JLabel(AppInfo.LOADER_GIF);
        progressBar.setPreferredSize(new Dimension(40, 40));
        content.add(new JPanelLambda(new FlowLayout(FlowLayout.CENTER)).addC(logo));
        content.add(new JPanelLambda(new FlowLayout(FlowLayout.CENTER)).addC(progressBar));
        loadText = new JLabel("Loading...");
        content.add(new JPanelLambda(new FlowLayout(FlowLayout.CENTER)).addC(loadText));
        this.add(content, new GridBagConstraints());
    }

    public void setLoadText(String text){
        loadText.setText(text);
    }
}
