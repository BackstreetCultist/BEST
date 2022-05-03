package tech.charliewilkins.BEST.Vehicles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import org.ejml.simple.SimpleMatrix;

import tech.charliewilkins.BEST.Vehicles.Sensors.Sensor;

import java.util.Random;

public class Microbe {
    // World
    private int worldWidth;
    private int worldHeight;

    // Microbe
    private int diameter;

    // Coordinates
    private int x;
    private int y;

    // Movement
    private int vl;
    private int vr;
    private double theta;

    // Random walk
    private Boolean randomTurning;
    private int randomCount;
    private int randomLimit;

    // Other
    private Random rnd;

    // SO it has eight sensor positions,
    // with 0 being 'dead ahead' e.g where the line terminates
    private Sensor[] sensors = new Sensor[8];

    public Microbe(int x,int y, int worldWidth, int worldHeight) {
        // World
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // Microbe
        this.diameter = 50;

        // Coordinates
        this.x = x;
        this.y = y;

        // Movement
        this.vl = 0;
        this.vr = 0;
        this.theta = 0.0;

        //Random walk
        this.randomTurning = true;
        this.randomCount = 0;
        this.randomLimit = 0;

        // Other
        this.rnd = new Random();
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
        g2d.setPaint(Color.RED);
        g2d.fill(circle);
        g2d.setPaint(Color.BLACK);
        g2d.draw(circle);

        // Draw a line indicating direction of travel
        Line2D line = new Line2D.Double();
        line.setLine(x,y,x+((diameter/2)*Math.cos(theta)),y+((diameter/2)*Math.sin(theta)));
        g2d.draw(line);

        // Draw sensors
        for (int i = 0; i < 8; i++){
            if (sensors[i] != null){
                // We want the sensors arranged in a circle, at 22.5 degree angles around the perimeter of the microbe
                int sx = x + (int) ((diameter/2)*Math.cos(theta + (Math.toRadians(45 * i))));
                int sy = y + (int) ((diameter/2)*Math.sin(theta + (Math.toRadians(45 * i))));
                sensors[i].setCoords(sx, sy);
                sensors[i].draw(g);
            }
        }
    }

    public void step(){
        // Set microbe vl and vr
        transferFunction();
        // Set microbe x and y
        move();

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

    public void transferFunction() {
        randomMove();
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

    private void randomMove(){
        // Default movement - move up to 5 in each direction
        // x += (rnd.nextInt(11)-5);
        // y += (rnd.nextInt(11)-5);
        
        // Swap behaviours if limit elapsed
        if (randomCount == randomLimit) {
            randomCount = 0;
            randomLimit = 1+rnd.nextInt(50);
            randomTurning = (randomTurning) ? false : true;
        }

        //Set values
        if (randomTurning) {
            vr = 1;
            vl = -1;
        }
        else {
            vr = 5;
            vl = 5;
        }
        randomCount++;
    }

    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }
}
