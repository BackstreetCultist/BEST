package tech.charliewilkins.BEST.World;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tech.charliewilkins.BEST.Vehicles.Microbe;
import tech.charliewilkins.BEST.Vehicles.MicrobeBuilder;
import tech.charliewilkins.BEST.World.Sources.FoodSource;
import tech.charliewilkins.BEST.World.Sources.HeatSource;
import tech.charliewilkins.BEST.World.Sources.LightSource;
import tech.charliewilkins.BEST.World.Sources.ScentSource;
import tech.charliewilkins.BEST.World.Sources.Source;

public class World extends JPanel implements Runnable {
    private final int W_WIDTH = 1000;
    private final int W_HEIGHT = 1000;
    private final int DELAY = 25;
    private final int SIMULATION_SPEED = 1;
    private final int MICROBE_SIZE = 30;
    private final int MAX_MICROBES = 16;
    private final int SOURCE_COUNT = 16;
    private final int SOURCE_DIAMETER = 10;

    private int ticks = 10000;
    private Thread animator;
    private Random rng;
    private ArrayList<Source> worldSources; // Collects sources not tied to other objects

    private ArrayList<Microbe> microbes;
    private ArrayList<Microbe> microbesToDelete;
    private ArrayList<Microbe> microbesToAdd;
    MicrobeBuilder builder;

    public World() {
        rng = new Random();
        worldSources = new ArrayList<>();
        microbes = new ArrayList<>();
        microbesToDelete = new ArrayList<>();
        microbesToAdd = new ArrayList<>();
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
        
        for (Source source : worldSources) {
            source.draw(g);
        }

        for (Microbe microbe : microbes) {
            microbe.draw(g);
        }

        Toolkit.getDefaultToolkit().sync();

        drawInfo(g);
    }

    public void drawInfo(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Serif", Font.PLAIN, 14);

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.setPaint(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(("Microbes: " + Integer.toString(microbes.size())), W_WIDTH - 100, W_HEIGHT - 50);
        g2d.drawString(("Time: " + Integer.toString(ticks)), W_WIDTH - 100, W_HEIGHT - 25);
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
        microbesToDelete = new ArrayList<>();

        // Add new microbes
        // Note: doesn't like to use addAll()
        for (Microbe microbe : microbesToAdd){
            if (microbes.size() < MAX_MICROBES) {
                microbes.add(microbe);
            }
        }
        microbesToAdd = new ArrayList<>();

        // Delete any sources that have expired
        ArrayList<Source> sourcesToDelete = new ArrayList<>();
        for (Source source : worldSources) {
            source.reduceHealth();
            if (source.getHealth() <= 0) {
                sourcesToDelete.add(source);
            }
        }
        worldSources.removeAll(sourcesToDelete);

        // Add new sources
        while (worldSources.size() < SOURCE_COUNT) {
            int k = rng.nextInt(5);
            switch(k) {
                case 0:
                    worldSources.add(new LightSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, rng.nextInt(750) + 250, this));
                    break;
                case 1:
                    worldSources.add(new HeatSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, rng.nextInt(750) + 250, this));
                    break;
                case 2:
                    worldSources.add(new ScentSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, rng.nextInt(750) + 250, this));
                    break;
                case 3:
                    worldSources.add(new FoodSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, rng.nextInt(750) + 250, this));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

        // Sources
        for (int i = 0; i < SOURCE_COUNT; i++) {
            int k = rng.nextInt(5);
            switch(k) {
                case 0:
                    worldSources.add(new LightSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, rng.nextInt(750) + 250, this));
                    break;
                case 1:
                    worldSources.add(new HeatSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, rng.nextInt(750) + 250, this));
                    break;
                case 2:
                    worldSources.add(new ScentSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, rng.nextInt(750) + 250, this));
                    break;
                case 3:
                    worldSources.add(new FoodSource(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, rng.nextInt(750) + 250, this));
                    break;
                default:
                    break;
            }
        }

        // Microbes
        for (int i = 0; i < MAX_MICROBES; i++) {
            microbes.add(builder.build(rng.nextInt((W_WIDTH-100))+50, rng.nextInt(W_HEIGHT-100)+50, Evolve.generateGenome(rng)));
        }
        
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

            ticks--;
            if (ticks <= 0) {
                break;
            }
        }
    }

    public void reproduce(String dna, String dna2, int x, int y) {
        String newGenome = Evolve.evolve(dna, dna2, rng);
        microbesToAdd.add(builder.build(x, y, newGenome));
    }

    public ArrayList<Microbe> getMicrobes() {
        return microbes;
    }

    public int getSourceDiameter() {
        return SOURCE_DIAMETER;
    }

    public static class Evolve {
        public static String generateGenome(Random rng) {
            char[] genome = new char[64];

            for (int i = 0; i < 48; i++) {
                genome[i] = (rng.nextInt() % 2 == 0) ? '0' : '1';
            }

            return new String(genome);
        }

        public static String evolve(String dna, String dna2, Random rng) {
            // Mutate 1/4 of the genome on a 1/4 chance
            if (rng.nextInt(4) == 0) {
                return mutate(kPointCrossover(dna, dna2, 4, rng), rng);
            }
            else {
                return kPointCrossover(dna, dna2, 4, rng);
            }
        }

        public static String mutate(String genome, Random rng) {
            char[] genomeArr = genome.toCharArray();
            for (int i = 0; i < genome.length(); i++) {
                if (rng.nextInt(4) == 0) {
                    genomeArr[i] = (genomeArr[i] == '0') ? '1' : '0'; 
                }
            }
            return new String(genomeArr);
        }

        public static String kPointCrossover(String a, String b, int k, Random rng) {
            String[] newGen = {a,b};
            for (int i = 0; i < k; i++) {
                newGen = onePointCrossover(newGen[0], newGen[1], rng);
            }
            return newGen[0];
        }

        public static String[] onePointCrossover(String a, String b, Random rng) {
            String[] newGen = {a,b};
            int index = rng.nextInt(a.length());
            newGen[0] = a.substring(0, index) + b.substring(index, b.length());
            newGen[1] = b.substring(0, index) + a.substring(index, b.length());
            return newGen;
        }
    }
}
