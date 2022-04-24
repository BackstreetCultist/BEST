package tech.charliewilkins.BEST.World.Sources;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class LightSource extends Source {
    public LightSource(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Draw the light source as a yellow circle with black outline
        double circleX = this.x - diameter / 2.0; // As Ellipse2D takes upper left co-ord
        double circleY = y - diameter / 2.0;
        Ellipse2D.Double circle = new Ellipse2D.Double(circleX,circleY,diameter,diameter);
        g2d.setPaint(Color.YELLOW);
        g2d.fill(circle);
        g2d.setPaint(Color.BLACK);
        g2d.draw(circle);
    }
}
