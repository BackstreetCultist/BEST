package tech.charliewilkins.BEST.Vehicles.Sensors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

// A connector connects a sensor to a motor
// Its effect can be excitative, depressive, ...
// Thus each connector has a target sensor, a target motor and an effect
public class Connector {
    public enum Transferance {
        ATTRACT,
        REPEL
    }

    public enum Motor {
        LEFT,
        RIGHT
    }

    private final Sensor targetSensor;
    private final Motor targetMotor;
    private final Transferance transferance;

    public Connector (Sensor targetSensor, Motor targetMotor, Transferance transferance) {
        this.targetSensor = targetSensor;
        this.targetMotor = targetMotor;
        this.transferance = transferance;
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
        if (this.transferance == Transferance.ATTRACT) {
            g2d.setPaint(Color.GREEN);
        }
        else {
            g2d.setPaint(Color.RED);
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
