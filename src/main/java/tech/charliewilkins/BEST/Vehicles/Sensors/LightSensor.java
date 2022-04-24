package tech.charliewilkins.BEST.Vehicles.Sensors;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import tech.charliewilkins.BEST.World.Sources.LightSource;
import tech.charliewilkins.BEST.World.Sources.Source;

public class LightSensor extends Sensor {

    public LightSensor(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Draw the light sensor as a white square with black outline
        double squareX = x - size / 2.0; // As Rectangle2D takes upper left co-ord
        double squareY = y - size / 2.0;
        Rectangle2D.Double square = new Rectangle2D.Double(squareX, squareY, size, size);
        g2d.setPaint(Color.WHITE);
        g2d.fill(square);
        g2d.setPaint(Color.BLACK);
        g2d.draw(square);
    }

    // This code borrowed from lab 1
    public double sense (Source[] sources) {
        double light = 0.0;
        for (Source source: sources) {
            if (source.getClass().equals(LightSource.class)) {
                int lx = source.getX();
                int ly = source.getY();
    
                double distance = Math.sqrt(((lx-x)^2)+((ly-y)^2));
                light += 200000.0/(distance*distance);
            }
        }
        return light;
    }
}
