package tech.charliewilkins.BEST.Vehicles.Sensors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import tech.charliewilkins.BEST.World.World;
import tech.charliewilkins.BEST.World.Sources.Source;

// A connector connects a sensor to a motor
// Its effect can be excitative, depressive, ...
// Thus each connector has a target sensor, a target motor and an effect
public class Connector {
    public enum Transferance {
        EXCITE,
        INHIBIT,
        CURVE,
        STEP
    }

    public enum Motor {
        LEFT,
        RIGHT
    }

    private final Sensor targetSensor;
    private final Motor targetMotor;
    private final Transferance transferance;
    private final int config;
    private final int factor;

    public Connector (Sensor targetSensor, Motor targetMotor, Transferance transferance, int config, int factor) {
        this.targetSensor = targetSensor;
        this.targetMotor = targetMotor;
        this.transferance = transferance;
        this.config = config;
        this.factor = factor;
    }

    public double activate(ArrayList<Source> sources) {
        double velocity = 0;

        // This is a safe way to set graphLength with a possibly null world or set of sources
        int graphLength = 0;
        for (Source source : sources) {
            World world = source.getWorld();
            if (world != null) {
                graphLength = world.getWidth() / 2;
            }
            if (graphLength != 0) {
                break;
            }
        }
        

        switch(transferance) {
            case EXCITE:
                velocity = (targetSensor.sense(sources) / factor);
                break;

            case INHIBIT:
                velocity = -(targetSensor.sense(sources) / factor);
                break;

            case CURVE:
                velocity = (targetSensor.curveSense(sources, graphLength, config) / factor);
                break;
            
            case STEP:
                velocity = (targetSensor.stepSense(sources, graphLength, config) / factor);
                break;
        }

        return velocity;
    }

    public void draw(Graphics g, int microbeX, int microbeY, double microbeTheta, int microbeDiameter){
        Graphics2D g2d = (Graphics2D) g;

        int mx, my;

        if (this.targetMotor == Motor.LEFT) {
            mx = microbeX + (int) (microbeDiameter*Math.cos(microbeTheta + (Math.toRadians(225))));
            my = microbeY + (int) (microbeDiameter*Math.sin(microbeTheta + (Math.toRadians(225))));

        }
        else {
            mx = microbeX + (int) (microbeDiameter*Math.cos(microbeTheta + (Math.toRadians(135))));
            my = microbeY + (int) (microbeDiameter*Math.sin(microbeTheta + (Math.toRadians(135))));

        }

        Line2D la = new Line2D.Double();
        la.setLine(targetSensor.getX(),targetSensor.getY(),mx,my);

        switch (transferance) {
            case EXCITE:
                g2d.setPaint(Color.GREEN);
                break;
            case INHIBIT:
                g2d.setPaint(Color.RED);
                break;
            case CURVE:
                g2d.setPaint(Color.BLUE);
                break;
            case STEP:
                g2d.setPaint(Color.MAGENTA);
                break;
        }
        
        g2d.draw(la);
    }

    public Sensor getSensor() {
        return targetSensor;
    }

    public Motor getMotor() {
        return targetMotor;
    }

    public Transferance getTransferance() {
        return transferance;
    }
}
