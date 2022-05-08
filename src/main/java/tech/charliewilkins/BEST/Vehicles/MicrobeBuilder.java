package tech.charliewilkins.BEST.Vehicles;

import java.util.ArrayList;

import tech.charliewilkins.BEST.Vehicles.Sensors.Connector;
import tech.charliewilkins.BEST.Vehicles.Sensors.HeatSensor;
import tech.charliewilkins.BEST.Vehicles.Sensors.LightSensor;
import tech.charliewilkins.BEST.Vehicles.Sensors.ScentSensor;
import tech.charliewilkins.BEST.Vehicles.Sensors.Sensor;
import tech.charliewilkins.BEST.Vehicles.Sensors.Connector.Motor;
import tech.charliewilkins.BEST.Vehicles.Sensors.Connector.Transferance;
import tech.charliewilkins.BEST.World.World;

public class MicrobeBuilder {
    private final World worldRef;

    public MicrobeBuilder(World worldRef) {
        this.worldRef = worldRef;
    }

    // New DNA Structure:
    // 00 00 00 00 - four pairs of sensors
    // 00 00 00 00 - activated or not
    // 00 00 00 00 - crossed or not
    // 00 00 00 00 - profile
    // 00 00 00 00 - profile config
    // 00 00 00 00 - drive factor (divide by +1)

    // Profiles:
    // 0 - EXCITE - increases speed
        // Config - none
    // 1 - INHIBIT - reduces speed
        // Config - none
    // 2 - CURVE - increases around a bell curve - Vehicle 4a
        // Config - places peak on line of WIDTH/2
        // (So if 1, its at (WIDTH/2)+(((WIDTH/2)/4)*1) distance from object, reducing as far from that distance in either direction)
    // 3 - STEP - drive steps up and down - Vehicle 4a
        // Config - number of steps up or down uniformly distributed along WIDTH/2
        // (So 1 results in it activating at half that, 2 results in it stepping up at 2/3 and down at 1/3 etc)

    // DNA for Aggression:
    // 00 01 00 00
    // 00 01 00 00
    // 00 01 00 00
    // 00 00 00 00
    // 00 00 00 00
    // 00 00 00 00
    // 000100000001000000010000000000000000000000000000d

    // DNA for Fear:
    // 00 01 00 00
    // 00 01 00 00
    // 00 00 00 00
    // 00 00 00 00
    // 00 00 00 00
    // 00 00 00 00
    // 000100000001000000000000000000000000000000000000

    // DNA for Love:
    // 00 01 00 00
    // 00 01 00 00
    // 00 01 00 00
    // 00 01 00 00
    // 00 00 00 00
    // 00 00 00 00
    // 000100000001000000010000000100000000000000000000

    // DNA for Exploration:
    // 00 01 00 00
    // 00 01 00 00
    // 00 00 00 00
    // 00 01 00 00
    // 00 00 00 00
    // 00 00 00 00
    // 000100000001000000000000000100000000000000000000

    public Microbe build(int x, int y, String genome) {
        String sensorDNA = genome.substring(0,8);
        String activationDNA = genome.substring(8,16);
        String crossoverDNA = genome.substring(16,24);
        String profileDNA = genome.substring(24,32);
        String configDNA = genome.substring(32,40);
        String factorDNA = genome.substring(32,40);

        final Sensor[] sensors = buildSensorList(sensorDNA);
        ArrayList<Connector> connectors = buildConnectorList(activationDNA, crossoverDNA, profileDNA, configDNA, factorDNA, sensors);

        Microbe microbe = new Microbe(x, y, worldRef, sensors, connectors, genome);
        return microbe;
    }

    private Sensor[] buildSensorList(String sensorDNA) {
        Sensor[] sensors = new Sensor[8];

        for (int i = 0; i < 8; i += 2) {
            String chromosome = sensorDNA.substring(i, i+2);
            switch (binaryStringToInt(chromosome)){
                case 0:
                    sensors[i/2] = null;
                    sensors[7-(i/2)] = null;
                    break;
                case 1:
                    sensors[i/2] = new LightSensor(worldRef.getMicrobeSize() / 5);
                    sensors[7-(i/2)] = new LightSensor(worldRef.getMicrobeSize() / 5);
                    break;
                case 2:
                    sensors[i/2] = new HeatSensor(worldRef.getMicrobeSize() / 5);
                    sensors[7-(i/2)] = new HeatSensor(worldRef.getMicrobeSize() / 5);
                    break;
                case 3:
                    sensors[i/2] = new ScentSensor(worldRef.getMicrobeSize() / 5);
                    sensors[7-(i/2)] = new ScentSensor(worldRef.getMicrobeSize() / 5);
                    break;
            }
        }

        return sensors;
    }

    private ArrayList<Connector> buildConnectorList(String activationDNA, String crossoverDNA, String profileDNA, String configDNA, String factorDNA, Sensor[] sensors) {
        ArrayList<Connector> connectors = new ArrayList<>();

        // For each pair of connectors
        for (int i = 0; i < 8; i+=2) {
            ArrayList<Connector> connectorPair = buildConnectorPair(activationDNA.substring(i, i+2), crossoverDNA.substring(i, i+2), profileDNA.substring(i, i+2), configDNA.substring(i, i+2), factorDNA.substring(i, i+2), sensors[7-(i/2)], sensors[i/2]);
            connectors.addAll(connectorPair);
        }

        return connectors;
    }

    private ArrayList<Connector> buildConnectorPair (String activationChromosome, String crossoverChromosome, String profileChromosome, String configChromosome, String factorChromosome, Sensor leftSensor, Sensor rightSensor) {
        ArrayList<Connector> connectors = new ArrayList<>();
        
        // The sensors may not be there
        if (leftSensor == null || rightSensor == null) {
            return connectors;
        }
        
        // Have a minor chance that the sensors do not come online
        if (binaryStringToInt(activationChromosome) == 0) {
            return connectors;
        }
        
        // Do the connectors cross over?
        boolean crossover = (binaryStringToInt(crossoverChromosome) % 2 != 0);

        // What profiles do they have?
        Transferance transferance;
        switch (binaryStringToInt(profileChromosome)) {
            case 0:
                transferance = Transferance.EXCITE;
                break;
            case 1:
                transferance = Transferance.INHIBIT;
                break;
            case 2:
                transferance = Transferance.STEP;
                break;
            case 3:
                transferance = Transferance.CURVE;
                break;
            default: // This should actually never happen
                transferance = Transferance.CURVE;
                break;
        }

        // What is the config factor? (For Step and Curve)
        int config = binaryStringToInt(configChromosome) + 1;

        // What is the drive factor? (Division amount)
        int factor = binaryStringToInt(factorChromosome) + 1;

        // Build connectors
        if (crossover){
            connectors.add(new Connector(rightSensor, Motor.LEFT, transferance, config, factor));
            connectors.add(new Connector(leftSensor, Motor.RIGHT, transferance, config, factor));
        }
        else {
            connectors.add(new Connector(rightSensor, Motor.RIGHT, transferance, config, factor));
            connectors.add(new Connector(leftSensor, Motor.LEFT, transferance, config, factor));
        }

        return connectors;
    }

    private int binaryStringToInt(String binary) {
        return Integer.parseInt(binary, 2);
    }
}
