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
import tech.charliewilkins.BEST.World.Sources.HeatSource;
import tech.charliewilkins.BEST.World.Sources.LightSource;
import tech.charliewilkins.BEST.World.Sources.ScentSource;
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
    private Random rng;
    private ArrayList<Source> worldSources; // Collects sources not tied to other objects

    private ArrayList<Microbe> microbes;
    private ArrayList<Microbe> microbesToDelete;
    MicrobeBuilder builder;

    public World() {
        rng = new Random();
        worldSources = new ArrayList<>();
        microbes = new ArrayList<>();
        microbesToDelete = new ArrayList<>();
        builder = new MicrobeBuilder(this);

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
        
        for (Source source : worldSources) {
            source.draw(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    public void killMicrobe(Microbe microbe) {
        microbesToDelete.add(microbe);
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
        // Need to pass the world sources plus those attached to microbes
        ArrayList<Source> sources = new ArrayList<>();
        sources.addAll(worldSources);
        for (Microbe microbe : microbes) {
            sources.add(microbe.getScent());
        }
        return sources;
    }

    private void cycle() {
        for (Microbe microbe: microbes){
            microbe.step();
        }

        // Delete microbes that have died
        microbes.removeAll(microbesToDelete);
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

        // Sources
        worldSources.add(new LightSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50));
        worldSources.add(new LightSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50));
        worldSources.add(new HeatSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50));
        worldSources.add(new HeatSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50));
        worldSources.add(new ScentSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50));
        worldSources.add(new ScentSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50));

        // Microbes
        microbes.add(builder.build(INITIAL_X, INITIAL_Y, Evolve.generateGenome(rng)));
        microbes.add(builder.build(INITIAL_X, INITIAL_Y, Evolve.generateGenome(rng)));

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

    public void reproduce(String dna, String dna2) {
        String newGenome = Evolve.evolve(dna, dna2);
        //TODO
        System.out.println("New Genome: " + newGenome);
    }

    public ArrayList<Microbe> getMicrobes() {
        return microbes;
    }

    public static class Evolve {
        public static String generateGenome(Random rng) {
            char[] genome = new char[64];

            for (int i = 0; i < 32; i++) {
                genome[i] = (rng.nextInt() % 2 == 0) ? '0' : '1';
            }

            return new String(genome);
        }

        public static String evolve(String dna, String dna2){
            //TODO
            return "";
        }
    }
}
