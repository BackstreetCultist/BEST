package tech.charliewilkins.BEST.World;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tech.charliewilkins.BEST.Vehicles.Microbe;
import tech.charliewilkins.BEST.World.Sources.LightSource;

public class World extends JPanel implements Runnable {
    private final int W_WIDTH = 350;
    private final int W_HEIGHT = 350;
    private final int INITIAL_X = 100;
    private final int INITIAL_Y = 100;
    private final int DELAY = 25;

    private Thread animator;
    private Microbe m;
    private LightSource l;
    private Random rng;

    public World() {
        rng = new Random();

        m = new Microbe(INITIAL_X, INITIAL_Y, W_WIDTH, W_HEIGHT);

        // Create a LightSource at 50-(n-50)
        l = new LightSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50);

        initWorld();
    }

    private void initWorld() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(W_WIDTH, W_HEIGHT));
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        m.draw(g);
        l.draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void cycle() {
        m.step();
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

        while (true) {
            cycle();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("World.run: Thread interrupted: %s", e.getMessage());
                
                JOptionPane.showMessageDialog(this, msg, "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
            beforeTime = System.currentTimeMillis();
        }
    }
}
