package app.netflix.view;

import app.netflix.AppInfo;
import app.netflix.Icons;
import app.netflix.Main;
import app.netflix.lib.CardCellRenderer;
import app.netflix.lib.JPanelLambda;
import app.netflix.lib.JScrollable;
import app.netflix.listener.MouseListListener;
import app.netflix.listener.WheelListener;
import app.netflix.manager.MovieManager;
import app.netflix.model.Genre;
import app.netflix.model.Movie;
import app.netflix.utils.GraphicsUtils;
import balbucio.org.ejsl.component.JImage;
import balbucio.org.ejsl.component.panel.JCornerPanel;
import balbucio.org.ejsl.utils.ImageUtils;
import javafx.scene.media.Media;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainView extends JPanel {

    private ArrayList<JScrollPane> scrolls = new ArrayList<>();
    private ArrayList<MouseListListener> mouseListeners = new ArrayList<>();
    private Color FIRST_COLOR = new Color(18, 18, 29, 255);
    private Color SECOND_COLOR = new Color(17, 24, 37, 255);
    private JScrollPane scroll;

    public MainView() {
        this.setVisible(false);
        this.setLayout(new BorderLayout());
        JPanel left = getLeft();
        this.add(left, BorderLayout.WEST);
        JPanel center = getCenter();
        this.add(center, BorderLayout.CENTER);
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                genreJList.values().forEach(l -> {
                    ((CardCellRenderer) l.getCellRenderer()).setReady(true);
                });
            }

            @Override
            public void componentResized(ComponentEvent e) {
                int w = e.getComponent().getWidth() - left.getWidth();
                mouseListeners.forEach(m -> m.setRealWidth(w));
                for (JScrollPane value : scrolls) {
                    value.setWheelScrollingEnabled(false);
                    Dimension d = value.getSize();
                    d.width = w;
                    value.getViewport().setPreferredSize(d);
                }
            }
        });
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
            homeLabel.setForeground(Color.WHITE);
            homep.add(homeLabel);
            options.add(homep);
            JPanel space = new JPanelLambda(new BorderLayout(), FIRST_COLOR);
            space.setPreferredSize(new Dimension(10, 15));
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
            space.setPreferredSize(new Dimension(10, 15));
            options.add(space);
            foryoup.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Main.window.show("FORYOU");
                }
            });
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
            space.setPreferredSize(new Dimension(10, 15));
            options.add(space);
            listp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Main.window.show("LISTA");
                }
            });
        }

        JPanel capsulaOptions = new JPanel(new BorderLayout());
        capsulaOptions.setBackground(FIRST_COLOR);
        capsulaOptions.add(options, BorderLayout.NORTH);
        content.add(capsulaOptions, BorderLayout.CENTER);
        JPanel acc = new JPanel(new GridBagLayout());
        content.add(acc, BorderLayout.SOUTH);

        return content;
    }

    DefaultListModel<Movie> popularesModel = new DefaultListModel<>();
    DefaultListModel<Movie> watchedModel = new DefaultListModel<>();
    DefaultListModel<Movie> favoritedModel = new DefaultListModel<>();
    Map<Genre, DefaultListModel<Movie>> genreList = new HashMap<>();
    Map<Genre, JList<Movie>> genreJList = new HashMap<>();
    private boolean isPlayed = false;

    public JPanel getCenter() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(SECOND_COLOR);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(SECOND_COLOR);
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(SECOND_COLOR);
        center.setBorder(new EmptyBorder(10, 10, 10, 10));
        JCornerPanel searchCorner = new JCornerPanel(new Color(23, 30, 42, 255), 30);
        JImage seIcon = new JImage(Icons.SEARCH);
        seIcon.setMaxSize(true);
        seIcon.setCenter(true);
        seIcon.setPreferredSize(new Dimension(16, 16));
        seIcon.setBackground(new Color(23, 30, 42, 255));
        searchCorner.add(seIcon);
        JTextField searchField = new JTextField();
        searchField.setText("Pesquisar...");
        searchField.setToolTipText("Pesquise por filmes e séries!");
        searchField.setBorder(new EmptyBorder(0, 0, 0, 0));
        searchField.setPreferredSize(new Dimension(400, 24));
        searchField.setBackground(new Color(23, 30, 42, 255));
        searchCorner.add(searchField);
        center.add(searchCorner, new GridBagConstraints());
        searchPanel.add(center, BorderLayout.CENTER);

        content.add(searchPanel, BorderLayout.NORTH);

        JPanel conteudo = new JPanel();
        conteudo.setBackground(SECOND_COLOR);
        conteudo.setBorder(new EmptyBorder(10, 10, 10, 0));
        BoxLayout box = new BoxLayout(conteudo, BoxLayout.Y_AXIS);
        conteudo.setLayout(box);

        /**
         * FIRST ALTERATION
         */

        Movie featuredMovie = Main.movieManager.getFeaturedMovie();

        JPanel mainMovie = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                RenderingHints rh = new RenderingHints(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setRenderingHints(rh);
                Image original = featuredMovie.getBackdropImage();
                Image image = ImageUtils.setFitCenter(original, this.getWidth(), this.getHeight());
                g2.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), SECOND_COLOR, null);

                GraphicsUtils.fillGradientRect(g2, Color.BLACK, new Color(0, 0, 0, 0), 0, 0, this.getWidth(), this.getHeight());
                g2.dispose();
            }
        };

        mainMovie.setBackground(new Color(0,0,0,0));
        mainMovie.setPreferredSize(new Dimension(700, 450));
        mainMovie.setBorder(new EmptyBorder(10, 10,10,10));


        JPanel info = new JPanel();
        info.setBackground(new Color(0, 0, 0, 0));
        BoxLayout bi = new BoxLayout(info, BoxLayout.Y_AXIS);
        info.setLayout(bi);

        JPanel tf = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tf.setBackground(new Color(0, 0, 0, 0));
        JLabel title = new JLabel(featuredMovie.getName());
        title.setForeground(Color.WHITE);
        title.setBackground(new Color(0, 0, 0, 0));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 48f));
        tf.add(title);

        JPanel td = new JPanel(new FlowLayout(FlowLayout.LEFT));
        td.setPreferredSize(new Dimension(300, 120));
        td.setBackground(new Color(0, 0, 0, 0));
        JTextArea description = new JTextArea(featuredMovie.getDescription());
        description.setFont(description.getFont().deriveFont(14f));
        description.setEditable(false);
        description.setPreferredSize(new Dimension(400, 300));
        description.setBorder(new EmptyBorder(0, 0, 0, 0));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setForeground(Color.WHITE);
        description.setBackground(new Color(0, 0, 0, 0));
        td.add(description);
        info.add(tf);
        info.add(td);
        JPanel capsula = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                RenderingHints rh = new RenderingHints(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setRenderingHints(rh);
                Image original = featuredMovie.getBackdropImage();
                //Image image = ImageUtils.setFitCenter(original, this.getWidth(), this.getHeight());
                g2.drawImage(original, 0, 0, this.getWidth(), this.getHeight(), SECOND_COLOR, null);

                GraphicsUtils.fillGradientRect(g2, Color.BLACK, new Color(0, 0, 0, 0), 0, 0, this.getWidth(), this.getHeight());
            }
        };
        capsula.setBackground(new Color(0, 0, 0, 0));
        capsula.add(info, BorderLayout.NORTH);
        capsula.add(new JPanelLambda(new BorderLayout(), new Color(0, 0, 0, 0)), BorderLayout.CENTER);
        JPanel south = new JPanel();
        south.setBackground(new Color(0, 0, 0, 0));

        {
            JCornerPanel assistir = new JCornerPanel(Color.RED, 20);
            assistir.setPreferredSize(new Dimension(200, 32));
            assistir.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!isPlayed) {
                        Random random = new Random();
                        Main.watchManager.addWatched(AppInfo.ACCOUNT.getId(), String.valueOf(featuredMovie.getId()));
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
                            Main.window.show(featuredMovie.getName());
                            assistir.setBackground(Color.RED);
                        }, () -> {
                            Main.window.show(featuredMovie.getName());
                        });
                        isPlayed = true;
                        Main.window.getContent().add(view, featuredMovie.getName());
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
            label.setBackground(new Color(0, 0, 0, 0));
            label.setFont(label.getFont().deriveFont(18f));
            assistir.add(label);
            south.add(assistir, BorderLayout.NORTH);
        }
        capsula.add(south, BorderLayout.SOUTH);
        mainMovie.add(capsula, BorderLayout.CENTER);
        conteudo.add(mainMovie);

        JPanel lp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lp1.setBackground(SECOND_COLOR);
        lp1.setBorder(new EmptyBorder(3, 3, 3, 3));
        JLabel l1 = new JLabel("Filmes populares hoje:");
        l1.setBackground(SECOND_COLOR);
        l1.setFont(l1.getFont().deriveFont(18f));
        lp1.add(l1);
        conteudo.add(lp1);

        JList<Movie> populares = new JList<>(popularesModel);
        MouseListListener listener = new MouseListListener(populares);
        listener.setConsumer((o) -> {
            return movieManager.getMoviesAndExclude(null, o);
        });
        mouseListeners.add(listener);
        populares.addMouseListener(listener);
        populares.addMouseMotionListener(listener);
        populares.setBorder(new EmptyBorder(0, 0, 0, 0));
        populares.setBackground(SECOND_COLOR);
        populares.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        populares.setVisibleRowCount(1);
        populares.setCellRenderer(new CardCellRenderer());
        genreJList.put(new Genre(-1, "Populares"), populares);

        JScrollPane scrollPop = new JScrollPane(populares);
        listener.setScroll(scrollPop);
        scrollPop.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPop.getVerticalScrollBar().setUnitIncrement(16);
        scrollPop.addMouseWheelListener(e -> {
            Point p = scroll.getViewport().getViewPosition();
            p.y += (10 * e.getUnitsToScroll());
            if (p.y >= 0) {
                scroll.getViewport().setViewPosition(p);
            } else {
                p.y = 0;
                scroll.getViewport().setViewPosition(p);
            }
        });
        scrollPop.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPop.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPop.setBackground(SECOND_COLOR);
        scrolls.add(scrollPop);

        conteudo.add(scrollPop);
        conteudo.add(new JPanelLambda(new BorderLayout(), SECOND_COLOR));

        loadModels(conteudo);

        scroll = new JScrollPane(conteudo);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroll.setBackground(SECOND_COLOR);
        scroll.getVerticalScrollBar().setUnitIncrement(30);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));

        content.add(scroll);
        return content;
    }

    private MovieManager movieManager = Main.movieManager;

    private void loadModels(JPanel conteudo) {
        movieManager.getPopularToday().forEach(m -> popularesModel.addElement(m));
        movieManager.getGenries().forEach(g -> {
            DefaultListModel<Movie> mg = new DefaultListModel<>();
            movieManager.getMovies(g).forEach(mg::addElement);
            genreList.put(g, mg);

            JPanel lp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lp2.setBackground(SECOND_COLOR);
            lp2.setBorder(new EmptyBorder(3, 3, 3, 3));
            JLabel l2 = new JLabel(g.getName() + ":");
            l2.setBackground(SECOND_COLOR);
            l2.setFont(l2.getFont().deriveFont(18f));
            lp2.add(l2);
            conteudo.add(lp2);

            JList<Movie> genreList = new JList<>(mg);
            MouseListListener listener = new MouseListListener(genreList);
            listener.setConsumer((o) -> {
                return movieManager.getMoviesAndExclude(g, o);
            });
            mouseListeners.add(listener);
            genreList.addMouseListener(listener);
            genreList.addMouseMotionListener(listener);
            genreList.setBorder(new EmptyBorder(0, 0, 0, 0));
            genreList.setBackground(SECOND_COLOR);

            genreList.setCellRenderer(new CardCellRenderer());
            genreList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            genreList.setVisibleRowCount(1);

            genreJList.put(g, genreList);

            JScrollPane genreScroll = new JScrollPane(genreList);
            genreScroll.getHorizontalScrollBar().setUnitIncrement(16);
            genreScroll.getVerticalScrollBar().setUnitIncrement(16);
            genreScroll.addMouseWheelListener(e -> {
                Point p = scroll.getViewport().getViewPosition();
                p.y += (10 * e.getUnitsToScroll());
                if (p.y >= 0) {
                    scroll.getViewport().setViewPosition(p);
                } else {
                    p.y = 0;
                    scroll.getViewport().setViewPosition(p);
                }
            });
            genreScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            genreScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
            genreScroll.setBackground(SECOND_COLOR);
            listener.setScroll(genreScroll);
            scrolls.add(genreScroll);

            conteudo.add(genreScroll);
            conteudo.add(new JPanelLambda(new BorderLayout(), SECOND_COLOR));
        });
    }

}
