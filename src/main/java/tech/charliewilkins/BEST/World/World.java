package tech.charliewilkins.BEST.World;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tech.charliewilkins.BEST.Vehicles.Microbe;
import tech.charliewilkins.BEST.Vehicles.MicrobeBuilder;
import tech.charliewilkins.BEST.World.Sources.LightSource;
import tech.charliewilkins.BEST.World.Sources.Source;

public class World extends JPanel implements Runnable {
    private final int W_WIDTH = 1000;
    private final int W_HEIGHT = 1000;
    private final int INITIAL_X = 100;
    private final int INITIAL_Y = 100;
    private final int DELAY = 25;
    private final int SIMULATION_SPEED = 1;
    private final int MICROBE_SIZE = 30;

    private Thread animator;
    private LightSource l;
    private Random rng;
    private ArrayList<Source> sources;

    private ArrayList<Microbe> microbes;
    MicrobeBuilder builder;

    public World() {
        rng = new Random();
        sources = new ArrayList<>();
        builder = new MicrobeBuilder(this);
        microbes = new ArrayList<>();

        // Create a LightSource at 50-(n-50)
        l = new LightSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50);
        sources.add(l);

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

        for (Microbe microbe : microbes) {
            microbe.draw(g);
        }
        
        l.draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public int getWorldHeight() {
        return W_HEIGHT;
    }

    public int getWorldWidth() {
        return W_WIDTH;
    }

    public int getSimSpeed() {
        return SIMULATION_SPEED;
    }

    public int getMicrobeSize() {
        return MICROBE_SIZE;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    private void cycle() {
        // Create microbes if not enough
        if (microbes.size() < 1) {
            microbes.add(builder.build(INITIAL_X, INITIAL_Y, ""));
        }

        for (Microbe microbe: microbes){
            microbe.step();
        }
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
