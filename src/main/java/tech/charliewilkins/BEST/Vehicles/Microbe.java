package tech.charliewilkins.BEST.Vehicles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import java.util.Random;

public class Microbe {
    private int x;
    private int y;
    private int worldWidth;
    private int worldHeight;

    private Random randomMover = new Random();

    public Microbe(int x,int y, int worldWidth, int worldHeight) {
        this.x = x;
        this.y = y;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Draw the microbe as a red circle with black outline
        Ellipse2D.Double circle = new Ellipse2D.Double(x,y,50,50);
        g2d.setPaint(Color.RED);
        g2d.fill(circle);
        g2d.setPaint(Color.BLACK);
        g2d.draw(circle);
    }

    public void move(){
        // Transform function for Vehicle components will go here
        randomMove();

        // Retrieve microbe if it escapes petri dish
        if (y > (worldHeight+20)) {
            y = 0;
        }
        if (x > (worldWidth+20)) {
            x = 0;
        }
        if (y < (-20)) {
            y = worldHeight;
        }
        if (x < (-20)) {
            x = worldWidth;
        }
    }

    private void randomMove(){
        // Default movement - move up to 5 in each direction
        x += (randomMover.nextInt(11)-5);
        y += (randomMover.nextInt(11)-5);
    }

    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }
}
