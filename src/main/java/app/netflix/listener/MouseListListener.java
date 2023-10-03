package app.netflix.listener;

import app.netflix.AppInfo;
import app.netflix.Icons;
import app.netflix.lib.CardCellRenderer;
import app.netflix.model.Movie;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

@Data
public class MouseListListener implements MouseListener, MouseMotionListener {

    private int realWidth = 0;
    private JList<Movie> list;
    private JScrollPane scroll;
    private Toolkit ttk = Toolkit.getDefaultToolkit();
    private boolean rightHover  = false;
    private boolean leftHover = false;

    public MouseListListener(JList<Movie> list) {
        this.list = list;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(rightHover){
            Point p = scroll.getViewport().getViewPosition();
            p.x += 40;
            scroll.getViewport().setViewPosition(p);
        } else if(leftHover){
            Point p = scroll.getViewport().getViewPosition();
            p.x -= 40;
            if(p.x <= 0){
                p.x = 0;
            }
            scroll.getViewport().setViewPosition(p);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("d");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        rightHover = false;
        leftHover = false;
        list.setCursor(null);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("f");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = scroll.getMousePosition().x;
        double wf =  (realWidth % 95);
        double w =  ((realWidth - wf) - 30);
        if(x > w){
            list.setCursor(AppInfo.RIGHT_CURSOR);
            leftHover = false;
            rightHover = true;
        } else if(x < wf){
            list.setCursor(AppInfo.LEFT_CURSOR);
            rightHover = false;
            leftHover = true;
        } else{
            leftHover = false;
            rightHover = false;
            list.setCursor(null);
        }
        ((CardCellRenderer) list.getCellRenderer()).setF(list.getFirstVisibleIndex());
        ((CardCellRenderer) list.getCellRenderer()).setE(list.getLastVisibleIndex());
    }
}
