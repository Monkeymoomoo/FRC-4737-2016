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

    public DriveControl(CANTalon fl, CANTalon fr, CANTalon rl, CANTalon rr) {
        // FL : 0
        // FR : 1
        // RL : 2
        // RR : 3
        talons = new CANTalon[]{
                fl, fr, rl, rr
        };

    }

    public void tankControl(double left, double right, boolean squareInput) {
        // Square inputs
        if (squareInput) {
            boolean neg = left < 0;
            left *= left;
            if (neg) left = -left;
            neg = right < 0;
            right *= right;
            if (neg) right = -right;
        }

        talons[0].set(left);
        talons[1].set(right);
        talons[2].set(left);
        talons[3].set(right);
    }

    public void arcadeControl(double xAxis, double yAxis, boolean squareInput) {
        // x=1, y=1 -> L=1, R=0.5
        // x=0, y=1 -> L=1, R=1
        // x=1, y=0 -> L=1, R=-1

        // Square inputs
        if (squareInput) {
            boolean neg = xAxis < 0;
            xAxis *= xAxis;
            if (neg) xAxis = -xAxis;
            neg = yAxis < 0;
            yAxis *= yAxis;
            if (neg) yAxis = -yAxis;
        }

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

        tankControl(left, right, false);
    }

    /**
     * Turns off all drive motors. This does not guarantee that they will not follow a new
     * command to resume working, but only instructs them to stop.
     */
    public void enterSafeState() {
        for (CANTalon talon : talons)
            talon.set(0);
    }

}
