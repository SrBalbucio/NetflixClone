package app.netflix.lib;

import app.netflix.AppInfo;
import app.netflix.Main;
import app.netflix.model.Movie;
import app.netflix.view.MovieView;
import balbucio.org.ejsl.component.JImage;
import balbucio.org.ejsl.utils.ImageUtils;
import lombok.Data;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.VolatileImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
public class CardCellRenderer implements ListCellRenderer<Movie> {

    private Map<Integer, JPanel> CACHED_COMP = new HashMap<>();
    private Color FIRST_COLOR = new Color(18, 18, 29, 255);
    private Color SECOND_COLOR = new Color(17, 24, 37, 255);
    private int f = 0, e = 8;
    private boolean ready = false;

    @Override
    public Component getListCellRendererComponent(JList<? extends Movie> list, Movie value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected && list.getCursor() != AppInfo.LEFT_CURSOR && list.getCursor() != AppInfo.RIGHT_CURSOR) {
            list.clearSelection();
            Main.window.getContent().add(new MovieView(value), String.valueOf(value.getId()));
            Main.window.show(String.valueOf(value.getId()));
        }
        JPanel panel = new JPanel(new BorderLayout());
        try {
            if (index >= f && e >= index) {
                if (!CACHED_COMP.containsKey(value.getId())) {
                    panel = new JPanel(new BorderLayout());
                    panel.setBackground(SECOND_COLOR);
                    panel.setPreferredSize(new Dimension(150, 200));
                    panel.setBorder(new EmptyBorder(3, 3, 3, 3));
                    JImage image = new JImage(value.getPosterImage());
                    image.setPreferredSize(new Dimension(147, 297));
                    image.setCenter(true);
                    panel.add(image, BorderLayout.CENTER);
                    CACHED_COMP.put(value.getId(), panel);
                } else {
                    panel = CACHED_COMP.get(value.getId());
                }
            }
        } catch (Exception e) {
        }
        return panel;
    }

}
