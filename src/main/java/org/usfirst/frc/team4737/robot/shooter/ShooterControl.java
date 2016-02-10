package org.usfirst.frc.team4737.robot.shooter;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * @author Brian Semrau
 * @version 2/3/2016
 */
public class ShooterControl {

    private CANTalon[] talons;

    private boolean updatedChamber;
    private boolean updatedShooter;

    public ShooterControl(CANTalon chamberL, CANTalon chamberR, CANTalon shooterL, CANTalon shooterR) {
        talons = new CANTalon[]{
                chamberL, chamberR,
                shooterL, shooterR
        };
    }

    /**
     * Periodic update for disabling talons that haven't been set recently.
     * This is to be called during the commonPeriodic function.
     */
    public void periodic() {
        if (!updatedChamber) {
            talons[0].set(0);
            talons[1].set(0);
        }
        if (!updatedShooter) {
            talons[2].set(0);
            talons[3].set(0);
        }
        updatedChamber = false;
        updatedShooter = false;
    }

    public void intake() {
        // Back .75
        // Front .5
        talons[0].set(0.35);
        talons[1].set(-0.35);
        talons[2].set(-0.5);
        talons[3].set(0.5);
        updatedChamber = true;
        updatedShooter = true;
    }

    public void dropBall() {
        // Reverse intake
        talons[0].set(-0.2);
        talons[1].set(0.2);
        talons[2].set(0.5);
        talons[3].set(-0.5);
        updatedChamber = true;
        updatedShooter = true;
    }

    public void spinupShooter() {
        // Front 1.0
        talons[2].set(1.0);
        talons[3].set(-1.0);
        updatedShooter = true;
    }

    public void releaseChamber() {
        // Back 1.0
        talons[0].set(-1.0);
        talons[1].set(1.0);
        updatedChamber = true;
    }

    public void enterSafeState() {
        for (CANTalon talon : talons) {
            talon.set(0);
        }
    }

}
