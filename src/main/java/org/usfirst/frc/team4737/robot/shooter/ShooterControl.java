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

}
