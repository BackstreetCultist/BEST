package tech.charliewilkins.BEST.Vehicles;

import java.util.ArrayList;

import tech.charliewilkins.BEST.Vehicles.Sensors.Connector;
import tech.charliewilkins.BEST.Vehicles.Sensors.LightSensor;
import tech.charliewilkins.BEST.Vehicles.Sensors.Sensor;
import tech.charliewilkins.BEST.Vehicles.Sensors.Connector.Motor;
import tech.charliewilkins.BEST.Vehicles.Sensors.Connector.Transferance;
import tech.charliewilkins.BEST.World.World;

public class MicrobeBuilder {
    private final World worldRef;

    public MicrobeBuilder(World worldRef) {
        this.worldRef = worldRef;
    }

    // DNA for Hate: 0001000000000001001000000000000100000000000000000000000000000000
    // DNA for Fear: 0001000000000001000100000000001000000000000000000000000000000000
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
                    sensors[i/2] = null;
                    break;
                case 3:
                    sensors[i/2] = null;
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
                    // TODO configure connector rather than just making DRIVE
                    Connector connector = (i % 2 == 0) ? new Connector(sensors[i/2], Motor.LEFT, Transferance.DRIVE) : new Connector(sensors[i/2], Motor.RIGHT, Transferance.DRIVE);
                    connectors.add(connector);
                }
            }
        }

        return connectors;
    }

    private int binaryStringToInt(String binary) {
        return Integer.parseInt(binary, 2);
    }
}
