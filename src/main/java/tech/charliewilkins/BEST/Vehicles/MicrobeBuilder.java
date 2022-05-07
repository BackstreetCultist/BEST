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

    // DNA for Aggression:
    // 00 01 00 00
    // 00 01 00 00
    // 00 01 00 00
    // 00 00 00 00
    // 00010000000100000001000000000000

    // DNA for Fear:
    // 00 01 00 00
    // 00 01 00 00
    // 00 00 00 00
    // 00 00 00 00
    // 00010000000100000000000000000000

    // DNA for Love:
    // 00 01 00 00
    // 00 01 00 00
    // 00 01 00 00
    // 00 01 00 00
    // 00010000000100000001000000010000

    // DNA for Exploration:
    // 00 01 00 00
    // 00 01 00 00
    // 00 00 00 00
    // 00 01 00 00
    // 00010000000100000000000000010000

    public Microbe build(int x, int y, String genome) {
        String sensorDNA = genome.substring(0,8);
        String connectorActivationDNA = genome.substring(8,16);
        String connectorCrossoverDNA = genome.substring(16,24);
        String connectorConfigDNA = genome.substring(24,32);

        final Sensor[] sensors = buildSensorList(sensorDNA);
        ArrayList<Connector> connectors = buildConnectorList(connectorActivationDNA, connectorCrossoverDNA, connectorConfigDNA, sensors);

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

    private ArrayList<Connector> buildConnectorList(String connectorActivationDNA, String connectorCrossoverDNA, String connectorConfigDNA, Sensor[] sensors) {
        ArrayList<Connector> connectors = new ArrayList<>();

        // For each pair of connectors
        for (int i = 0; i < 8; i+=2) {
            String activationChromosome = connectorActivationDNA.substring(i, i+2);
            // If the sensors are live and the connectors are activated
            if (sensors[i/2] != null && binaryStringToInt(activationChromosome) % 2 != 0) {
                String crossoverChromosome = connectorCrossoverDNA.substring(i, i+2);
                String configChromosome = connectorConfigDNA.substring(i, i+2);
                boolean crossover = (binaryStringToInt(crossoverChromosome) % 2 != 0);
                Transferance transferance = (binaryStringToInt(configChromosome) % 2 != 0) ? Transferance.INHIBIT : Transferance.DRIVE;
                if (crossover){
                    connectors.add(new Connector(sensors[i/2], Motor.LEFT, transferance));
                    connectors.add(new Connector(sensors[7-(i/2)], Motor.RIGHT, transferance));
                }
                else {
                    connectors.add(new Connector(sensors[i/2], Motor.RIGHT, transferance));
                    connectors.add(new Connector(sensors[7-(i/2)], Motor.LEFT, transferance));
                }
            }
        }

        return connectors;
    }

    private int binaryStringToInt(String binary) {
        return Integer.parseInt(binary, 2);
    }
}
