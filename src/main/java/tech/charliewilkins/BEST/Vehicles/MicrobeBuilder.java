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

    // DNA for Agression:
    // 00 01 00 00 00 00 00 01
    // 0  0  1  0  0  0  0  0  0  0  0  0  0  0  0  1
    // 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    // 0001000000000001001000000000000100000000000000000000000000000000

    // DNA for Fear:
    // 00 01 00 00 00 00 00 01
    // 0  0  0  1  0  0  0  0  0  0  0  0  0  0  1  0
    // 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    // 0001000000000001000100000000001000000000000000000000000000000000

    // DNA for Love:
    // 00 01 00 00 00 00 00 01
    // 0  0  0  1  0  0  0  0  0  0  0  0  0  0  1  0
    // 00 00 00 01 00 00 00 00 00 00 00 00 00 00 01 00
    // 0001000000000001000100000000001000000001000000000000000000000100

    // DNA for Exploration:
    // 00 01 00 00 00 00 00 01
    // 0  0  1  0  0  0  0  0  0  0  0  0  0  0  0  1
    // 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 01
    // 0001000000000001001000000000000100000100000000000000000000000001
    public Microbe build(int x, int y, String genome) {
        String sensorDNA = genome.substring(0,16);
        String connectorDNA = genome.substring(16,32);
        String connectorConfigDNA = genome.substring(32);

        final Sensor[] sensors = buildSensorList(sensorDNA);
        ArrayList<Connector> connectors = buildConnectorList(connectorDNA, connectorConfigDNA, sensors);

        Microbe microbe = new Microbe(x, y, worldRef, sensors, connectors);
        return microbe;
    }

    private Sensor[] buildSensorList(String sensorDNA) {
        Sensor[] sensors = new Sensor[8];

        for (int i = 0; i < 16; i += 2) {
            String chromosome = sensorDNA.substring(i, i+2);
            switch (binaryStringToInt(chromosome)){
                case 0:
                    sensors[i/2] = null;
                    break;
                case 1:
                    sensors[i/2] = new LightSensor(worldRef.getMicrobeSize() / 5);
                    break;
                case 2:
                    sensors[i/2] = new HeatSensor(worldRef.getMicrobeSize() / 5);
                    break;
                case 3:
                    sensors[i/2] = new ScentSensor(worldRef.getMicrobeSize() / 5);
                    break;
            }
        }

        return sensors;
    }

    private ArrayList<Connector> buildConnectorList(String connectorDNA, String connectorConfigDNA, Sensor[] sensors) {
        ArrayList<Connector> connectors = new ArrayList<>();

        // For each connector
            // If the sensor in position floor(i/2) is not null
            // Connect to left if i mod 2 == 0
            // Connect to right if not
        for (int i = 0; i < 16; i++) {
            if (connectorDNA.charAt(i) == '1') {
                if (sensors[i/2] != null){
                    String chromosome = connectorConfigDNA.substring(i*2, (i*2)+2);
                    Motor motor = (i % 2 == 0) ? Motor.LEFT : Motor.RIGHT;
                    Transferance transferance = (binaryStringToInt(chromosome) % 2 == 0) ? Transferance.DRIVE : Transferance.INHIBIT;
                    connectors.add(new Connector(sensors[i/2], motor, transferance));
                }
            }
        }

        return connectors;
    }

    private int binaryStringToInt(String binary) {
        return Integer.parseInt(binary, 2);
    }
}
