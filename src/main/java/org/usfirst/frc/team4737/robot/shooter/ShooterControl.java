package org.usfirst.frc.team4737.robot.shooter;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * @author Brian Semrau
 * @version 2/3/2016
 */
public class ShooterControl {

    private CANTalon[] talons;

    public ShooterControl(CANTalon chamberL, CANTalon chamberR, CANTalon shooterL, CANTalon shooterR) {
        talons = new CANTalon[]{
                chamberL, chamberR,
                shooterL, shooterR
        };
    }

    public void intake() {
        // Back .75
        // Front .5
        talons[0].set(0.75);
        talons[1].set(0.75);
        talons[2].set(0.5);
        talons[3].set(0.5);
    }

    public void dropBall() {
        // Reverse intake
        talons[0].set(-0.75);
        talons[1].set(-0.75);
        talons[2].set(-0.5);
        talons[3].set(-0.5);
    }

    public void spinupShooter() {
        // Front 1.0
        talons[2].set(1.0);
        talons[3].set(1.0);
    }

    public void releaseChamber() {
        // Back 1.0
        talons[0].set(1.0);
        talons[1].set(1.0);
    }

}
