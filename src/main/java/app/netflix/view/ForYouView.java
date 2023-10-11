package app.netflix.view;

import app.netflix.AppInfo;
import app.netflix.Icons;
import app.netflix.Main;
import app.netflix.lib.CardCellRenderer;
import app.netflix.lib.JPanelLambda;
import app.netflix.listener.MouseListListener;
import app.netflix.manager.MovieManager;
import app.netflix.model.Genre;
import app.netflix.model.Movie;
import balbucio.org.ejsl.component.JImage;
import balbucio.org.ejsl.component.panel.JCornerPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForYouView extends JPanel {

    private ArrayList<JScrollPane> scrolls = new ArrayList<>();
    private ArrayList<MouseListListener> mouseListeners = new ArrayList<>();
    private Color FIRST_COLOR = new Color(18, 18, 29, 255);
    private Color SECOND_COLOR = new Color(17, 24, 37, 255);
    private JScrollPane scroll;

    public ForYouView() {
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
            homeLabel.setForeground(Color.GRAY);
            homep.add(homeLabel);
            options.add(homep);
            JPanel space = new JPanelLambda(new BorderLayout(), FIRST_COLOR);
            space.setPreferredSize(new Dimension(10,15));
            options.add(space);
            homep.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Main.window.show("HOME");
                }
            });
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
            listaLabel.setForeground(Color.WHITE);
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
        acc.setBackground(new Color(0,0,0,0));
        JPanel accPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        accPanel.setBackground(new Color(0,0,0,0));
        JImage image = new JImage(AppInfo.ACCOUNT.getImage());
        image.setCenter(true);
        image.setPreferredSize(new Dimension(32, 32));
        accPanel.add(image);
        accPanel.add(new JLabel(AppInfo.ACCOUNT.getUsername()));
        acc.add(accPanel);
        content.add(acc, BorderLayout.SOUTH);

        return content;
    }

    DefaultListModel<Movie> popularesModel = new DefaultListModel<>();
    DefaultListModel<Movie> watchedModel = new DefaultListModel<>();
    DefaultListModel<Movie> favoritedModel = new DefaultListModel<>();
    Map<Genre, DefaultListModel<Movie>> genreList = new HashMap<>();
    Map<Genre, JList<Movie>> genreJList = new HashMap<>();

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
        listener.setConsumer((o) -> { return movieManager.getMoviesAndExclude(null, o); });
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
        scrollPop.setPreferredSize(new Dimension(500, 200));
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
        movieManager.getPopularToday().forEach(m -> {
            popularesModel.addElement(m);

            DefaultListModel<Movie> mg = new DefaultListModel<>();
            movieManager.getParecido(m).forEach(mg::addElement);
            genreList.put(new Genre(-3, "Parecido1"), mg);

            JPanel lp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lp2.setBackground(SECOND_COLOR);
            lp2.setBorder(new EmptyBorder(3, 3, 3, 3));
            JLabel l2 = new JLabel( "Baseado no filme "+m.getName()+":");
            l2.setBackground(SECOND_COLOR);
            l2.setFont(l2.getFont().deriveFont(18f));
            lp2.add(l2);
            conteudo.add(lp2);

            JList<Movie> genreList = new JList<>(mg);
            MouseListListener listener = new MouseListListener(genreList);
            listener.setConsumer((o) -> {
                return movieManager.getMovies(movieManager.getGenre(m.getId()));
            });
            mouseListeners.add(listener);
            genreList.addMouseListener(listener);
            genreList.addMouseMotionListener(listener);
            genreList.setBorder(new EmptyBorder(0, 0, 0, 0));
            genreList.setBackground(SECOND_COLOR);

            genreList.setCellRenderer(new CardCellRenderer());
            genreList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            genreList.setVisibleRowCount(1);

            genreJList.put(new Genre(-3, "Parecido2"), genreList);

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
