package tech.charliewilkins.BEST.Vehicles.Sensors;

import tech.charliewilkins.BEST.World.Sources.Source;

public class Sensor {
    //Coordinates
    protected int x;
    protected int y;

    protected final int size;

    public Sensor(int x, int y) {
        this.x = x;
        this.y = y;

        this.size = 10;
    }

    public double Sense (Source[] sources) {
        return 0.0;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
