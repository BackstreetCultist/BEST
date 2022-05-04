package tech.charliewilkins.BEST.Vehicles.Sensors;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import tech.charliewilkins.BEST.World.Sources.Source;

public class Sensor {
    //Coordinates
    protected int x;
    protected int y;

    protected final int size;
    protected final Color color;

    public Sensor(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        this.size = 10;
    }

    public double sense(ArrayList<Source> sources) {
        return 0.0;
    }

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Draw the sensor as a square with black outline
        double squareX = x - size / 2.0; // As Rectangle2D takes upper left co-ord
        double squareY = y - size / 2.0;
        Rectangle2D.Double square = new Rectangle2D.Double(squareX, squareY, size, size);
        g2d.setPaint(color);
        g2d.fill(square);
        g2d.setPaint(Color.BLACK);
        g2d.draw(square);
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
