package tech.charliewilkins.BEST.World;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class LightSource {
    // Coordinates
    private final int x;
    private final int y;

    private final int diameter;

    public LightSource(int x, int y) {
        this.x = x;
        this.y = y;

        this.diameter = 10;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Draw the light source as a yellow circle with black outline
        double circleX = x - diameter / 2.0; // As Ellipse2D takes upper left co-ord
        double circleY = y - diameter / 2.0;
        Ellipse2D.Double circle = new Ellipse2D.Double(circleX,circleY,diameter,diameter);
        g2d.setPaint(Color.YELLOW);
        g2d.fill(circle);
        g2d.setPaint(Color.BLACK);
        g2d.draw(circle);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
