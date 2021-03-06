package tech.charliewilkins.BEST.World.Sources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import tech.charliewilkins.BEST.World.World;

public class Source {
    // Coordinates
    protected int x;
    protected int y;

    // Have reference to world its in
    protected World world;

    protected final int diameter;
    protected final Color color;
    private int health;

    public Source(int x, int y, int health, World world, Color color) {
        this.x = x;
        this.y = y;
        this.health = health;

        this.diameter = world.getSourceDiameter();
        this.color = color;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Draw the source as a circle with black outline
        double circleX = this.x - diameter / 2.0; // As Ellipse2D takes upper left co-ord
        double circleY = y - diameter / 2.0;
        Ellipse2D.Double circle = new Ellipse2D.Double(circleX,circleY,diameter,diameter);
        g2d.setPaint(color);
        g2d.fill(circle);
        g2d.setPaint(Color.BLACK);
        g2d.draw(circle);
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void reduceHealth() {
        health--;
    }

    public int getHealth() {
        return health;
    }

    public int getDiameter() {
        return diameter;
    }

    public World getWorld() {
        return world;
    }
}
