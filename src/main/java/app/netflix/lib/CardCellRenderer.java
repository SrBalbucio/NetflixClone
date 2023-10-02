package app.netflix.lib;

import app.netflix.model.Movie;
import balbucio.org.ejsl.component.JImage;
import balbucio.org.ejsl.utils.ImageUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CardCellRenderer implements ListCellRenderer<Movie> {

    private Map<Integer, JPanel> CACHED_COMP = new HashMap<>();
    private Color FIRST_COLOR = new Color(18,18,29,255);
    private Color SECOND_COLOR = new Color(17,24,37,255);

    @Override
    public Component getListCellRendererComponent(JList<? extends Movie> list, Movie value, int index, boolean isSelected, boolean cellHasFocus) {
        try {
            if(!CACHED_COMP.containsKey(value.getId())) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(SECOND_COLOR);
                panel.setPreferredSize(new Dimension(150, 200));
                panel.setBorder(new EmptyBorder(3, 3, 3, 3));
                if (!cellHasFocus) {
                    JImage image = new JImage(value.getPosterImage());
                    image.setPreferredSize(new Dimension(147, 297));
                    ;
                    image.setCenter(true);
                    panel.add(image, BorderLayout.CENTER);
                }
                CACHED_COMP.put(value.getId(), panel);
                return panel;
            } else{
                return CACHED_COMP.get(value.getId());
            }
        } catch (Exception e) {
        }
        return new JPanel();
    }
}
