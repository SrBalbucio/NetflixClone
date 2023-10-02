package app.netflix.listener;

import app.netflix.model.Movie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class WheelListener implements MouseWheelListener {
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        JScrollPane component = (JScrollPane) e.getComponent();
        Point p = component.getViewport().getViewPosition();
        p.x += e.getUnitsToScroll();
        component.getViewport().setViewPosition(p);
    }
}
