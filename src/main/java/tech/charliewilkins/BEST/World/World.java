package tech.charliewilkins.BEST.World;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tech.charliewilkins.BEST.Vehicles.Microbe;

public class World extends JPanel implements Runnable {
    private final int W_WIDTH = 350;
    private final int W_HEIGHT = 350;
    private final int INITIAL_X = -40;
    private final int INITIAL_Y = -40;
    private final int DELAY = 25;

    private Thread animator;
    private Microbe m;

    public World() {
        m = new Microbe(INITIAL_X, INITIAL_Y, W_WIDTH, W_HEIGHT);

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
        Toolkit.getDefaultToolkit().sync();
    }

    private void cycle() {
        m.move();
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
