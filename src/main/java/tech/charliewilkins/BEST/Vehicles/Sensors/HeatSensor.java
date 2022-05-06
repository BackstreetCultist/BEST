package tech.charliewilkins.BEST.Vehicles.Sensors;

import java.awt.Color;
import java.util.ArrayList;

import tech.charliewilkins.BEST.World.Sources.HeatSource;
import tech.charliewilkins.BEST.World.Sources.Source;

public class HeatSensor extends Sensor {

    public HeatSensor(int size) {
        super(size, Color.RED);
    }

    // This code borrowed from lab 1
    public double sense (ArrayList<Source> sources) {
        double light = 0.0;
        for (Source source: sources) {
            if (source.getClass().equals(HeatSource.class)) {
                int lx = source.getX();
                int ly = source.getY();
    
                double distance = Math.sqrt(((lx-x)*(lx-x)) + ((ly-y)*(ly-y)));
                light += 200000.0/(distance * distance);
            }
        }
        return light;
    }
}
