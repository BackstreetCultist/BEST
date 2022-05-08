package tech.charliewilkins.BEST.Vehicles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.ejml.simple.SimpleMatrix;

import tech.charliewilkins.BEST.Vehicles.Sensors.Connector;
import tech.charliewilkins.BEST.Vehicles.Sensors.Sensor;
import tech.charliewilkins.BEST.Vehicles.Sensors.Connector.Motor;
import tech.charliewilkins.BEST.World.World;
import tech.charliewilkins.BEST.World.Sources.FoodSource;
import tech.charliewilkins.BEST.World.Sources.HeatSource;
import tech.charliewilkins.BEST.World.Sources.ScentSource;
import tech.charliewilkins.BEST.World.Sources.Source;

import java.util.ArrayList;
import java.util.Random;

public class Microbe {
    // World
    private World worldRef; // Need a ref to the world it exists in

    // Microbe
    private int diameter;
    private int health;
    private double healthLimit;
    private ScentSource scent;

    // Coordinates
    private int x;
    private int y;

    // Movement
    private double vl;
    private double vr;
    private double theta;
    private final double MIN_SPEED;
    private final double CRUISE_SPEED;
    private final double MAX_SPEED;

    // Reproduction
    private final String dna;
    private Microbe reproductionCandidate; //Another microbe this microbe is attempting to reproduction with
    private int reproductionCount; //How long these two have been in reproduction range
    private final int reproductionThreshold; //The length of time they must remain to reproduce
    private int reproductionCooldown; //Must be zero to initiate reproduction
    private int reproductionMaxCooldown; //What the cooldown is set to after reproducing
    private final int reproductionHealthLimit; //Microbe must be healthy to initiate reproduction
    private final int reproductionHealthCost; //How reproduction changes health

    // Other
    // SO it has eight sensor positions,
    // with 0 being 'dead ahead' e.g where the line terminates
    private final Sensor[] sensors;
    // It also has a variable number of connectors
    private final ArrayList<Connector> connectors;
    Font font;
    Random rng;

    public Microbe(int x, int y, World worldRef, Sensor[] sensors, ArrayList<Connector> connectors, String dna) {
        // World
        this.worldRef = worldRef;
        rng = new Random();

        // Microbe
        this.diameter = worldRef.getMicrobeSize();
        this.health = 1000;
        this.healthLimit = 1000.0;
        scent = new ScentSource(x,y);

        // Coordinates
        this.x = x;
        this.y = y;

        // Movement
        this.vl = 0;
        this.vr = 0;
        this.theta = (rng.nextDouble() * 6);
        this.MIN_SPEED = 5.0;
        this.CRUISE_SPEED = 10.0;
        this.MAX_SPEED = 20.0;

        // Reproduction
        this.dna = dna;
        this.reproductionCandidate = null;
        this.reproductionCount = 0;
        this.reproductionThreshold = 2;
        this.reproductionCooldown = reproductionMaxCooldown;
        this.reproductionMaxCooldown = 50;
        this.reproductionHealthLimit = 200;
        this.reproductionHealthCost = -50;

        // Other
        this.sensors = sensors;
        this.connectors = connectors;
        font = new Font("Serif", Font.PLAIN, 11);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Rendering Hints
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Draw the microbe as a red circle with black outline
        double circleX = x - diameter / 2.0; // As Ellipse2D takes upper left co-ord
        double circleY = y - diameter / 2.0;
        Ellipse2D.Double circle = new Ellipse2D.Double(circleX,circleY,diameter,diameter);
        g2d.setPaint(Color.LIGHT_GRAY);
        g2d.fill(circle);
        g2d.setPaint(Color.BLACK);
        g2d.draw(circle);

        // Draw a line indicating direction of travel
        Line2D line = new Line2D.Double();
        line.setLine(x,y,x+((diameter/2)*Math.cos(theta)),y+((diameter/2)*Math.sin(theta)));
        g2d.draw(line);

        // Draw some black blocks for motors
        // We want them behind the robot and offset to the left and right
        int lmx = x + (int) (diameter*Math.cos(theta + (Math.toRadians(225))));
        int lmy = y + (int) (diameter*Math.sin(theta + (Math.toRadians(225))));
        Rectangle2D.Double lm = new Rectangle2D.Double((lmx - (diameter / 10)), (lmy - (diameter / 10)), (diameter / 5), (diameter / 5));
        g2d.setPaint(Color.BLACK);
        g2d.fill(lm);

        int rmx = x + (int) (diameter*Math.cos(theta + (Math.toRadians(135))));
        int rmy = y + (int) (diameter*Math.sin(theta + (Math.toRadians(135))));
        Rectangle2D.Double rm = new Rectangle2D.Double((rmx - (diameter / 10)), (rmy - (diameter / 10)), (diameter / 5), (diameter / 5));
        g2d.setPaint(Color.BLACK);
        g2d.fill(rm);

        // Draw axles connecting the motors
        Line2D la = new Line2D.Double();
        la.setLine(x,y,lmx,lmy);
        g2d.draw(la);

        Line2D ra = new Line2D.Double();
        ra.setLine(x,y,rmx,rmy);
        g2d.draw(ra);

        // Draw sensors
        for (int i = 0; i < 8; i++){
            if (sensors[i] != null){
                // We want the sensors arranged in a circle, at 22.5 degree angles around the perimeter of the microbe
                int sx = x + (int) ((diameter/2)*Math.cos(theta + (Math.toRadians((45 * i)+22.5))));
                int sy = y + (int) ((diameter/2)*Math.sin(theta + (Math.toRadians((45 * i)+22.5))));
                sensors[i].setCoords(sx, sy);
                sensors[i].draw(g);
            }
        }

        // Draw connectors
        for (Connector connector : connectors) {
            connector.draw(g, x, y, theta, diameter);
        }

        // Add a counter for the microbe's health
        g2d.setPaint(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(Integer.toString(health), Math.round(x-((diameter/2)*Math.cos(theta))), Math.round(y-((diameter/2)*Math.sin(theta))));
    }

    public void step() {
        // Set microbe vl and vr
        transferFunction();
        // Set microbe x and y
        move();

        // Retrieve microbe if it escapes petri dish
        y = (y > worldRef.getWorldHeight()+20) ? 0 : y;
        x = (x > worldRef.getWorldHeight()+20) ? 0 : x;
        y = (y < -20) ? worldRef.getWorldHeight() : y;
        x = (x < -20) ? worldRef.getWorldWidth() : x;

        // Replenish health from food
        eatFood();

        // Take damage from heat
        heatDamage();

        // Step down health limit
        healthLimit -= 0.25;
        // Set health to limit if has healed above it
        health = (health > healthLimit) ? (int) Math.floor(healthLimit) : health;
        // Step down health and kill if dead
        health -= worldRef.getSimSpeed();
        if (health <= 0) {
            worldRef.killMicrobe(this);
        }

        // Attempt to reproduce
        reproduce(worldRef.getMicrobes());
    }

    // Sets vl and vr
    public void transferFunction() {
        // Set us back to cruising speed
        vl = CRUISE_SPEED;
        vr = CRUISE_SPEED;

        for (Connector connector: connectors) {
            if (connector.getMotor() == Motor.LEFT) {
                vl += connector.activate(worldRef.getSources());
            }
            else {
                vr += connector.activate(worldRef.getSources());
            }
        }

        // Minimum speed
        vl = (vl < MIN_SPEED) ? MIN_SPEED : vl;
        vr = (vr < MIN_SPEED) ? MIN_SPEED : vr;

        // Maximum speed
        vl = (vl > MAX_SPEED) ? MAX_SPEED : vl;
        vr = (vr > MAX_SPEED) ? MAX_SPEED : vr;

        vl = vl * worldRef.getSimSpeed();
        vr = vr * worldRef.getSimSpeed();
    }

    // Makes move in x and y based on vl and vt
    public void move() {
        // # cf. Dudek and Jenkin, Computational Principles of Mobile Robotics
        // def move(self,canvas,dt):
        //     if self.vl==self.vr:
        //         R = 0
        //     else:
        //         R = (self.ll/2.0)*((self.vr+self.vl)/(self.vl-self.vr))
        //     omega = (self.vl-self.vr)/self.ll
        double r = (vl==vr) ? 0.0 : (diameter/2.0)*((vr+vl)/(vl-vr));
        double omega = (vl-vr)/(diameter+0.0);

        //     ICCx = self.x-R*math.sin(self.theta) #instantaneous centre of curvature
        //     ICCy = self.y+R*math.cos(self.theta)
        //     m = np.matrix( [ [math.cos(omega*dt), -math.sin(omega*dt), 0], \
        //                     [math.sin(omega*dt), math.cos(omega*dt), 0],  \
        //                     [0,0,1] ] )
        //     v1 = np.matrix([[self.x-ICCx],[self.y-ICCy],[self.theta]])
        //     v2 = np.matrix([[ICCx],[ICCy],[omega*dt]])
        // Instantaneous centre of curvature
        double iccx = x-(r*Math.sin(theta));
        double iccy = y+(r*Math.cos(theta));
        SimpleMatrix m = new SimpleMatrix (new double[][] {
            new double[] {Math.cos(omega), -Math.sin(omega), 0d},
            new double[] {Math.sin(omega), Math.cos(omega), 0d},
            new double[] {0d,0d,1d}
        });
        SimpleMatrix v1 = new SimpleMatrix (new double[][] {{x-iccx}, {y-iccy},{theta}});
        SimpleMatrix v2 = new SimpleMatrix (new double[][] {{iccx},{iccy},{omega}});

        //     newv = np.add(np.dot(m,v1),v2)
        //     newX = newv.item(0)
        //     newY = newv.item(1)
        //     newTheta = newv.item(2)
        //     newTheta = newTheta%(2.0*math.pi) #make sure angle doesn't go outside [0.0,2*pi)
        SimpleMatrix newv = v2.plus(m.mult(v1));
        double newX = newv.get(0,0);
        double newY = newv.get(1,0);
        double newTheta = newv.get(2,0);
        newTheta = newTheta % (2.0*Math.PI);

        //     self.x = newX
        //     self.y = newY
        //     self.theta = newTheta        
        //     if self.vl==self.vr: # straight line movement
        //         self.x += self.vr*math.cos(self.theta) #vr wlog
        //         self.y += self.vr*math.sin(self.theta)
        //     canvas.delete(self.name)
        //     self.draw(canvas)
        x = (int) newX;
        y = (int) newY;
        theta = newTheta;
        if (vl==vr) {
            x += (int) (vr*Math.cos(theta));
            y += (int) (vr*Math.sin(theta));
        }
    }

    public void eatFood() {
        for (Source source : worldRef.getSources()) {
            if (source.getClass().equals(FoodSource.class)) {
                double distance = Math.sqrt(((source.getX()-x)*(source.getX()-x)) + ((source.getY()-y)*(source.getY()-y)));
                if (distance < (source.getDiameter() * 10)) {
                    health += (source.getDiameter() * 10) - distance;
                }
            }
        }
    }

    public void heatDamage() {
        for (Source source : worldRef.getSources()) {
            if (source.getClass().equals(HeatSource.class)) {
                double distance = Math.sqrt(((source.getX()-x)*(source.getX()-x)) + ((source.getY()-y)*(source.getY()-y)));
                if (distance < (source.getDiameter() * 10)) {
                    health -= (source.getDiameter() * 10) - distance;
                }
            }
        }
    }

    // Theoretically, both microbes should "mate" with each other,
    // So this all happens twice and we get two children
    public void reproduce(ArrayList<Microbe> microbes) {
        ArrayList<Microbe> candidates = new ArrayList<>(microbes);
        candidates.remove(this); //Can't mate with ourselves

        // Reduce cooldown
        reproductionCooldown = (reproductionCooldown > 0) ? reproductionCooldown - 1 : reproductionCooldown;

        // Can't reproduce if we recently reproduced
        if (reproductionCooldown > 0){
            return;
        }

        // Can't reproduce if we don't have enough health
        if (health < reproductionHealthLimit) {
            return;
        }

        // If mating is in progress
        if (this.reproductionCandidate != null) {
            // Check candidate still in range
            double targetDistance = Math.sqrt(((x-reproductionCandidate.getX())*(x-reproductionCandidate.getX()))+((y-reproductionCandidate.getY())*(y-reproductionCandidate.getY())));
            if (targetDistance <= diameter) {
                // If so, add one
                reproductionCount++;
                // If threshold reached, call to reproduce
                // Return
                if (reproductionCount >= reproductionThreshold){
                    worldRef.reproduce(dna, reproductionCandidate.getDNA(), x+(diameter*(rng.nextInt(10)-5)), y+(diameter*(rng.nextInt(10)-5)));
                    reproductionCount = 0;
                    reproductionCandidate = null;
                    reproductionCooldown = reproductionMaxCooldown;
                    health -= reproductionHealthCost;
                    return;
                }
                // Otherwise, return
                return;
            }
            //Else, reset counters and look for a new mate
            else {
                reproductionCount = 0;
                reproductionCandidate = null;
            }
        }

        // Otherwise, look for a new mate
        for (Microbe candidate : candidates) {
            double candidateDistance = Math.sqrt(((x-candidate.getX())*(x-candidate.getX()))+((y-candidate.getY())*(y-candidate.getY())));
            
            // If we are close enough to this candidate, begin mating
            if (candidateDistance <= diameter) {
                reproductionCandidate = candidate;
                reproductionCount++;
                // Return - we can only mate with one microbe at once
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public Source getScent() {
        scent.setCoords(x, y);
        return scent;
    }

    public String getDNA() {
        return dna;
    }

    public int getHealth() {
        return health;
    }
}
