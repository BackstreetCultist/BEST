package tech.charliewilkins.BEST.Vehicles.Sensors;

import java.awt.Color;
import java.util.ArrayList;

import tech.charliewilkins.BEST.World.Sources.FoodSource;
import tech.charliewilkins.BEST.World.Sources.ScentSource;
import tech.charliewilkins.BEST.World.Sources.Source;

public class ScentSensor extends Sensor {

    public ScentSensor(int size) {
        super(size, Color.GREEN);
    }

    // This code borrowed from lab 1
    public double sense (ArrayList<Source> sources) {
        double light = 0.0;
        for (Source source: sources) {
            if (source.getClass().equals(ScentSource.class) || source.getClass().equals(FoodSource.class)) {
                int lx = source.getX();
                int ly = source.getY();
    
                double distance = Math.sqrt(((lx-x)*(lx-x)) + ((ly-y)*(ly-y)));
                
                // Need to check this scent is not 'us'
                // The size of the sensor is a fifth of the diameter of the microbe
                // So if distance greater than radius?
                if (distance > ((size * 5)/2)+1){
                    light += 200000.0/(distance * distance);
                }
            }
        }
        return light;
    }

    public double curveSense (ArrayList<Source> sources, double graphLength, int factor) {
        double magnitude = 0.0;
        double pointOnGraph = (graphLength / 4.0) * factor;
        for (Source source: sources) {
            if (source.getClass().equals(ScentSource.class) || source.getClass().equals(FoodSource.class)) {
                int lx = source.getX();
                int ly = source.getY();
    
                double distance = Math.sqrt(((lx-x)*(lx-x)) + ((ly-y)*(ly-y)));
                
                // Need to check this scent is not 'us'
                // The size of the sensor is a fifth of the diameter of the microbe
                // So if distance greater than radius?
                if (distance > ((size * 5)/2)+1){
                    // We want the magnitude to be highest when we are closest to the top of the bell curve
                    // So when the different between us and that target distance is the smallest
                    magnitude += 200000.0/((pointOnGraph - distance) * (pointOnGraph - distance));
                }
            }
        }
        return magnitude;
    }
}
