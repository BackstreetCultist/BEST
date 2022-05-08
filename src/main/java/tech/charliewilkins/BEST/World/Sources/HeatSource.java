package tech.charliewilkins.BEST.World.Sources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import tech.charliewilkins.BEST.World.World;

public class HeatSource extends Source {
    public HeatSource(int x, int y, World world) {
        super(x, y, world, Color.RED);
    }

    public void draw (Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Draw the source as a circle with black outline
        double circleX = this.x - ((diameter / 2.0)*10); // As Ellipse2D takes upper left co-ord
        double circleY = y - ((diameter / 2.0)*10);
        Ellipse2D.Double circle = new Ellipse2D.Double(circleX,circleY,diameter*10,diameter*10);
        g2d.setPaint(Color.PINK);
        g2d.fill(circle);
        super.draw(g);
    }
}
