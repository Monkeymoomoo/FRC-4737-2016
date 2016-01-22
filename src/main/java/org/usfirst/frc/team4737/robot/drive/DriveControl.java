package org.usfirst.frc.team4737.robot.drive;

import edu.wpi.first.wpilibj.*;

/**
 * @author Brian Semrau
 * @version 1/11/2016
 */
public class DriveControl {

    private CANTalon[] talons;
    private Encoder[] encoders;

    public DriveControl(CANTalon[] talons, Encoder[] encoders) {

    }

    public void arcadeControl(double xAxis, double yAxis) {

        // x=1, y=1 -> L=1, R=0.5
        // x=0, y=1 -> L=1, R=1
        // x=1, y=0 -> L=1, R=-1

        double left = yAxis + xAxis;
        double right = yAxis - xAxis;


    }

}
