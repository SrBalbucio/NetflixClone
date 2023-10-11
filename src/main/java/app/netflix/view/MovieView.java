package app.netflix.view;

import app.netflix.AppInfo;
import app.netflix.Icons;
import app.netflix.Main;
import app.netflix.lib.JPanelLambda;
import app.netflix.model.Movie;
import app.netflix.utils.GraphicsUtils;
import balbucio.org.ejsl.component.JImage;
import balbucio.org.ejsl.component.panel.JCornerPanel;
import balbucio.org.ejsl.utils.ImageUtils;
import javafx.scene.media.Media;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class MovieView extends JPanel {

    private Color FIRST_COLOR = new Color(18, 18, 29, 255);
    private Color SECOND_COLOR = new Color(17, 24, 37, 255);
    private Movie movie;

    public MovieView(Movie movie) {
        this.movie = movie;
        this.setVisible(false);
        this.setLayout(new BorderLayout());
        JPanel left = getLeft();
        this.add(left, BorderLayout.WEST);
        JPanel center = getCenter();
        this.add(center, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public JPanel getLeft() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(FIRST_COLOR);
        /**
         BoxLayout box = new BoxLayout(content, BoxLayout.Y_AXIS);
         content.setLayout(box);**/

        // IMAGEM LOGO
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setPreferredSize(new Dimension(180, 100));
        logoPanel.setBackground(FIRST_COLOR);
        JImage logo = new JImage(AppInfo.NETFLIX_LOGO);
        logo.setMaxSize(true);
        logo.setCenter(true);
        logo.setPreferredSize(new Dimension(160, 60));
        logoPanel.add(logo, new GridBagConstraints());
        content.add(logoPanel, BorderLayout.NORTH);

        // OPTIONS
        JPanel options = new JPanel();
        options.setBackground(FIRST_COLOR);
        BoxLayout layout = new BoxLayout(options, BoxLayout.Y_AXIS);
        options.setLayout(layout);

        {
            JCornerPanel homep = new JCornerPanel(FIRST_COLOR, 30);
            // homep.setBorder(new EmptyBorder(10,10,10,10));
            homep.setLayout(new FlowLayout(FlowLayout.CENTER));
            homep.setPreferredSize(new Dimension(270, 24));
            JImage homeIcon = new JImage(Icons.HOME);
            homeIcon.setPreferredSize(new Dimension(18, 18));
            homeIcon.setMaxSize(true);
            homeIcon.setCenter(true);
            homep.add(homeIcon);
            JLabel homeLabel = new JLabel("Home");
            homeLabel.setFont(homeLabel.getFont().deriveFont(16f));
            homeLabel.setForeground(Color.GRAY);
            homep.add(homeLabel);
            options.add(homep);
            JPanel space = new JPanelLambda(new BorderLayout(), FIRST_COLOR);
            space.setPreferredSize(new Dimension(10,15));
            options.add(space);
        }

        {
            JCornerPanel foryoup = new JCornerPanel(FIRST_COLOR, 30);
            //foryoup.setBorder(new EmptyBorder(10,10,10,10));
            foryoup.setLayout(new FlowLayout(FlowLayout.CENTER));
            foryoup.setPreferredSize(new Dimension(270, 24));
            JImage listaIcon = new JImage(Icons.GOAL);
            listaIcon.setPreferredSize(new Dimension(18, 18));
            listaIcon.setMaxSize(true);
            listaIcon.setCenter(true);
            foryoup.add(listaIcon);
            JLabel listaLabel = new JLabel("Para você");
            listaLabel.setFont(listaLabel.getFont().deriveFont(16f));
            listaLabel.setForeground(Color.GRAY);
            foryoup.add(listaLabel);
            options.add(foryoup);
            JPanel space = new JPanelLambda(new BorderLayout(), FIRST_COLOR);
            space.setPreferredSize(new Dimension(10,15));
            options.add(space);
        }

        {
            JCornerPanel listp = new JCornerPanel(FIRST_COLOR, 30);
            //listp.setBorder(new EmptyBorder(10,10,10,10));
            listp.setLayout(new FlowLayout(FlowLayout.CENTER));
            listp.setPreferredSize(new Dimension(270, 24));
            JImage listaIcon = new JImage(Icons.BOOKMARK);
            listaIcon.setPreferredSize(new Dimension(18, 18));
            listaIcon.setMaxSize(true);
            listaIcon.setCenter(true);
            listp.add(listaIcon);
            JLabel listaLabel = new JLabel("Sua lista");
            listaLabel.setFont(listaLabel.getFont().deriveFont(16f));
            listaLabel.setForeground(Color.GRAY);
            listp.add(listaLabel);
            options.add(listp);
            JPanel space = new JPanelLambda(new BorderLayout(), FIRST_COLOR);
            space.setPreferredSize(new Dimension(10,15));
            options.add(space);
        }

        JPanel capsulaOptions = new JPanel(new BorderLayout());
        capsulaOptions.setBackground(FIRST_COLOR);
        capsulaOptions.add(options, BorderLayout.NORTH);
        content.add(capsulaOptions, BorderLayout.CENTER);
        JPanel extraPanel = new JPanel(new BorderLayout());
        content.add(extraPanel, BorderLayout.SOUTH);

        return content;
    }

    private boolean isPlayed = false;

    public JPanel getCenter(){
        System.out.println(this.getHeight());
        JPanel content = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                RenderingHints rh = new RenderingHints(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setRenderingHints(rh);
                Image original = movie.getBackdropImage();
                //Image image = ImageUtils.setFitCenter(original, this.getWidth(), this.getHeight());
                g2.drawImage(original, 0, 0, this.getWidth(), this.getHeight(), SECOND_COLOR, null);

                GraphicsUtils.fillGradientRect(g2, Color.BLACK, new Color(0, 0, 0, 0), 0, 0, this.getWidth(), this.getHeight());
            }
        };;
        content.setBorder(new EmptyBorder(40,40,40,40));
        content.setLayout(new BorderLayout());
        content.setBackground(new Color(0,0,0,0));;
        JPanel options = new JPanel(new BorderLayout());
        options.setBackground(new Color(0,0,0,0));

        {
            JCornerPanel voltar = new JCornerPanel(Color.DARK_GRAY, 20);
            voltar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Main.window.show("HOME");
                }
            });
            voltar.setLayout(new FlowLayout(FlowLayout.CENTER));
            JImage backIcon = new JImage(Icons.ARROW_LEFT);
            backIcon.setCenter(true);
            backIcon.setPreferredSize(new Dimension(24, 24));
            voltar.add(backIcon);
            JLabel label = new JLabel("Voltar");
            label.setBackground(new Color(0,0,0,0));
            label.setFont(label.getFont().deriveFont(18f));
            voltar.add(label);
            options.add(voltar, BorderLayout.WEST);
        }
        content.add(options, BorderLayout.NORTH);
        {
            JPanel info = new JPanel();
            info.setBackground(new Color(0,0,0,0));
            BoxLayout box = new BoxLayout(info, BoxLayout.Y_AXIS);
            info.setLayout(box);

            JPanel tf = new JPanel(new FlowLayout(FlowLayout.LEFT));
            tf.setBackground(new Color(0,0,0,0));
            JLabel title = new JLabel(movie.getName());
            title.setForeground(Color.WHITE);
            title.setBackground(new Color(0,0,0,0));
            title.setFont(title.getFont().deriveFont(Font.BOLD, 48f));
            tf.add(title);

            JPanel td = new JPanel(new FlowLayout(FlowLayout.LEFT));
            td.setPreferredSize(new Dimension(300, 120));
            td.setBackground(new Color(0,0,0,0));
            JTextArea description = new JTextArea(movie.getDescription());
            description.setFont(description.getFont().deriveFont(14f));
            description.setEditable(false);
            description.setPreferredSize(new Dimension(400, 300));
            description.setBorder(new EmptyBorder(0,0,0,0));
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            description.setForeground(Color.WHITE);
            description.setBackground(new Color(0,0,0,0));
            td.add(description);
            info.add(tf);
            info.add(td);
            JPanel capsula = new JPanel(new BorderLayout());
            capsula.setBackground(new Color(0,0,0,0));
            capsula.add(info, BorderLayout.NORTH);
            capsula.add(new JPanelLambda(new BorderLayout(), new Color(0,0,0,0)), BorderLayout.CENTER);
            JPanel south = new JPanel();
            south.setBackground(new Color(0,0,0,0));

            {
                JCornerPanel assistir = new JCornerPanel(Color.RED, 20);
                assistir.setPreferredSize(new Dimension(200, 32));
                assistir.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(!isPlayed) {
                            Random random = new Random();
                            Main.watchManager.addWatched(AppInfo.ACCOUNT.getId(), String.valueOf(movie.getId()));
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
                            int iv = random.nextInt(4);
                            if (iv < 2) {
                                iv = 2;
                            }
                            PlayerView view = new PlayerView(new Media(new File("trailer0" + iv + ".mp4").toURI().toString()), () -> {
                                Main.window.show(movie.getName());
                                assistir.setBackground(Color.RED);
                            }, () -> {
                                Main.window.show(movie.getName());
                            });
                            isPlayed = true;
                            Main.window.getContent().add(view, movie.getName());
                            assistir.setBackground(Color.DARK_GRAY);
                        }
                    }
                });
                assistir.setLayout(new FlowLayout(FlowLayout.CENTER));
                JImage backIcon = new JImage(Icons.PLAY);
                backIcon.setCenter(true);
                backIcon.setPreferredSize(new Dimension(24, 24));
                assistir.add(backIcon);
                JLabel label = new JLabel("Assistir");
                label.setBackground(new Color(0,0,0,0));
                label.setFont(label.getFont().deriveFont(18f));
                assistir.add(label);
                south.add(assistir, BorderLayout.NORTH);
            }
            capsula.add(south, BorderLayout.SOUTH);
            content.add(capsula, BorderLayout.CENTER);
        }
        content.setPreferredSize(new Dimension(this.getWidth(), 700));
        return content;
    }
}
