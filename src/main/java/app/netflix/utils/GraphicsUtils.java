package app.netflix.utils;

import java.awt.*;

public class GraphicsUtils {

    public static void fillGradientRect(Graphics2D offLineBuffer,
                                        Color startColor, Color endColor, int x, int y, int width,
                                        int height) {
        Paint prevPaint = offLineBuffer.getPaint();
        GradientPaint gradPaint = new GradientPaint(x, y, startColor, x + width, y, endColor, false);
        offLineBuffer.setPaint(gradPaint);
        offLineBuffer.fillRect(x, y, width, height);

        offLineBuffer.setPaint(prevPaint);
    }

    public static void fillRect(Graphics offLineBuffer, Color color, int x,
                                int y, int width, int height) {
        offLineBuffer.setColor(color);
        offLineBuffer.fillRect(x, y, width, height);
    }
}
