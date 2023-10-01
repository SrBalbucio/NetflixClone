package app.netflix.lib;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class JPanelLambda extends JPanel {

    public JPanelLambda(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public JPanelLambda(LayoutManager layout) {
        super(layout);
        this.setBackground(Color.BLACK);
    }

    public JPanelLambda(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public JPanelLambda() {
    }

    public JPanel addC(Component comp) {
        this.add(comp);
        return this;
    }

    public JPanel addC(Component comp, Object constraints) {
        this.add(comp, constraints);
        return this;
    }
}
