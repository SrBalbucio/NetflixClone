package app.netflix.view;

import app.netflix.AppInfo;
import app.netflix.Icons;
import app.netflix.Main;
import app.netflix.lib.CardCellRenderer;
import app.netflix.lib.JPanelLambda;
import app.netflix.lib.JScrollable;
import app.netflix.listener.WheelListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainView extends JPanel {

    private Dimension dimension = new Dimension();
    private ArrayList<JScrollPane> scrolls = new ArrayList<>();
    private Color FIRST_COLOR = new Color(18,18,29,255);
    private Color SECOND_COLOR = new Color(17,24,37,255);
    public MainView(){
        this.setLayout(new BorderLayout());
        JPanel left = getLeft();
        this.add(left, BorderLayout.WEST);
        JPanel center = getCenter();
        //SwingAcrylic.addTransparencyToBackground(center);
        this.add(center, BorderLayout.CENTER);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = e.getComponent().getWidth() - left.getWidth();
                System.out.println(w);
                for (JScrollPane value : scrolls) {
                    value.setWheelScrollingEnabled(false);
                    Dimension d = value.getSize();
                    d.width = w;
                    value.getViewport().setPreferredSize(d);
                }
            }
        });
    }

    public JPanel getLeft(){
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(FIRST_COLOR);
        /**
        BoxLayout box = new BoxLayout(content, BoxLayout.Y_AXIS);
        content.setLayout(box);**/

        // IMAGEM LOGO
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setPreferredSize(new Dimension(200, 100));
        logoPanel.setBackground(FIRST_COLOR);
        JImage logo = new JImage(AppInfo.NETFLIX_LOGO);
        logo.setMaxSize(true);
        logo.setCenter(true);
        logo.setPreferredSize(new Dimension(180, 76));
        logoPanel.add(logo, new GridBagConstraints());
        content.add(logoPanel, BorderLayout.NORTH);

        // OPTIONS
        JPanel options = new JPanel();
        BoxLayout layout = new BoxLayout(options, BoxLayout.Y_AXIS);
        options.setLayout(layout);

        JCornerPanel homep = new JCornerPanel(Color.DARK_GRAY, 30);
        homep.setLayout(new FlowLayout(FlowLayout.CENTER));
        homep.setPreferredSize(new Dimension(270, 24));
        homep.setBackground(FIRST_COLOR);
        JImage homeIcon = new JImage(Icons.HOME);
        homeIcon.setPreferredSize(new Dimension(24, 24));
        homeIcon.setMaxSize(true);
        homeIcon.setCenter(true);
        homep.add(homeIcon);
        JLabel homeLabel = new JLabel("Home");
        homeLabel.setFont(homeLabel.getFont().deriveFont(16f));
        homeLabel.setForeground(Color.WHITE);
        homep.add(homeLabel);

        options.add(homep);

        content.add(options, BorderLayout.CENTER);
        return content;
    }
    DefaultListModel<Movie> popularesModel = new DefaultListModel<>();
    DefaultListModel<Movie> watchedModel = new DefaultListModel<>();
    DefaultListModel<Movie> favoritedModel = new DefaultListModel<>();
    Map<Genre, DefaultListModel<Movie>> genreList = new HashMap<>();
    Map<Genre, JList<Movie>> genreJList = new HashMap<>();

    public JPanel getCenter(){
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(SECOND_COLOR);
        JPanel searchPanel =  new JPanel(new GridBagLayout());
        JPanel searchContent = new JPanel(new BorderLayout());
        JCornerPanel corner = new JCornerPanel(new Color(23,30,42,255), 30);
        corner.
        searchPanel.add(searchContent);
        content.add(searchPanel);

        JPanel conteudo = new JPanel();
        conteudo.setBackground(SECOND_COLOR);
        conteudo.setBorder(new EmptyBorder(10,10,10,0));
        BoxLayout box = new BoxLayout(conteudo, BoxLayout.Y_AXIS);
        conteudo.setLayout(box);

        JPanel lp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lp1.setBackground(SECOND_COLOR);
        lp1.setBorder(new EmptyBorder(3, 3, 3,3));
        JLabel l1 = new JLabel("Filmes populares hoje:");
        l1.setBackground(SECOND_COLOR);
        l1.setFont(l1.getFont().deriveFont(18f));
        lp1.add(l1);
        conteudo.add(lp1);

        JList<Movie> populares = new JList<>(popularesModel);
        populares.setBackground(SECOND_COLOR);
        populares.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        populares.setVisibleRowCount(1);
        populares.setCellRenderer(new CardCellRenderer());
        genreJList.put(new Genre(-1, "Populares"), populares);

        JScrollPane scrollPop = new JScrollPane(populares);
        scrollPop.setBackground(SECOND_COLOR);
        scrolls.add(scrollPop);

        conteudo.add(scrollPop);
        conteudo.add(new JPanelLambda(new BorderLayout(), SECOND_COLOR));

        /**
        JList<Movie> watched = new JList<>(watchedModel);
        watched.setCellRenderer(new CardCellRenderer());

        JList<Movie> favorited = new JList<>(favoritedModel);
        favorited.setCellRenderer(new CardCellRenderer());
         **/

        loadModels(conteudo);

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBackground(SECOND_COLOR);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(new EmptyBorder(0,0,0,0));

        content.add(scroll);
        // outras listas
        return content;
    }

    private MovieManager movieManager = Main.movieManager;

    private void loadModels(JPanel conteudo){
        movieManager.getPopularToday().forEach(m -> popularesModel.addElement(m));
        movieManager.getGenries().forEach(g -> {
            DefaultListModel<Movie> mg = new DefaultListModel<>();
            movieManager.getMovies(g).forEach(mg::addElement);
            genreList.put(g, mg);

            JPanel lp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lp2.setBackground(SECOND_COLOR);
            lp2.setBorder(new EmptyBorder(3, 3, 3,3));
            JLabel l2 = new JLabel(g.getName()+":");
            l2.setBackground(SECOND_COLOR);
            l2.setFont(l2.getFont().deriveFont(18f));
            lp2.add(l2);
            conteudo.add(lp2);

            JList<Movie> genreList = new JList<>(mg);
            genreList.setBackground(SECOND_COLOR);

            genreList.setCellRenderer(new CardCellRenderer());
            genreList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            genreList.setVisibleRowCount(1);

            genreJList.put(g, genreList);

            JScrollPane genreScroll = new JScrollPane(genreList);
            genreScroll.setBackground(SECOND_COLOR);
            scrolls.add(genreScroll);

            conteudo.add(genreScroll);
            conteudo.add(new JPanelLambda(new BorderLayout(), SECOND_COLOR));
        });
    }

}
