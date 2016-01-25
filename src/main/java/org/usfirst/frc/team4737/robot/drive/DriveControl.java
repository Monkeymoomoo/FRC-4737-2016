package org.usfirst.frc.team4737.robot.drive;

import edu.wpi.first.wpilibj.*;

/**
 * @author Brian Semrau
 * @version 1/11/2016
 */
public class DriveControl {

    /**
     * The weight of the x-axis in arcade drive calculations.
     * A lower value means turning is slower at faster forward speeds, and a higher value
     * means turning is faster at faster forward speeds.
     */
    private static double xAxisWeight = 1.0;

    private CANTalon[] talons;
    private Encoder[] encoders;

    // TODO add position tracking & position relocation functions

    public DriveControl(CANTalon fl, CANTalon fr, CANTalon rl, CANTalon rr, Encoder efl, Encoder efr, Encoder erl,Encoder err) {
        // FL : 0
        // FR : 1
        // RL : 2
        // RR : 3
        talons = new CANTalon[] {
                fl, fr, rl, rr
        };
        encoders = new Encoder[] {
                efl, efr, erl, err
        };
    }

    public void tankControl(double left, double right) {
        // TODO
    }

    public void arcadeControl(double xAxis, double yAxis) {
        // x=1, y=1 -> L=1, R=0.5
        // x=0, y=1 -> L=1, R=1
        // x=1, y=0 -> L=1, R=-1

        // Calculate the rates for the left and right sides
        double left = yAxis + xAxis * xAxisWeight;
        double right = yAxis - xAxis * xAxisWeight;

        // Normalize the values to < 1
        double largestVal = Math.max(Math.abs(left), Math.abs(right));
        if (largestVal > 1) {
            double mod = 1.0 / largestVal;
            left *= mod;
            right *= mod;
        }

        tankControl(left, right);
    }

}
