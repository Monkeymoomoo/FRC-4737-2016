package org.usfirst.frc.team4737.robot.networktables;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.*;

import java.util.Vector;

/**
 * @author Brian Semrau
 * @version 2/7/2016
 */
public class JetsonComm implements ITableListener {

    private static JetsonComm instance;

    public static void init() {
        instance = new JetsonComm();

        // Code on Jetson TK1
        /*
        NetworkTable.setIPAddress("roborio-4737-frc.local");
        NetworkTable table = NetworkTable.getTable("jetson");
         */
    }

    private NetworkTable table;

    public double[] goalAngles;
    public double[] goalDistances;
    public double[] ballAngles;
    public double[] ballDistances;

    private JetsonComm() {
        table = NetworkTable.getTable("jetson");
    }

    @Override
    public void valueChanged(ITable source, String key, Object value, boolean isNew) {
        switch (key) {
            case "goalangle":
                goalAngles = (double[]) value;
                break;
            case "goaldist":
                goalDistances = (double[]) value;
                break;
            case "ballangles":
                ballAngles = (double[]) value;
                break;
            case "balldists":
                ballDistances = (double[]) value;
                break;
            default:
                break;
        }
    }

}
