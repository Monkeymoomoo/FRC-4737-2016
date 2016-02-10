package org.usfirst.frc.team4737.robot.auton;

/**
 * An autonomous task to aim at the high goal using vision processing information.
 *
 * @author Brian Semrau
 * @version 2/6/2016
 */
public class AimTask extends AutonTask {

    public AimTask() {
        super("Aim at Goal");
    }

    @Override
    public void periodic() {

    }

    @Override
    public boolean completed() {
        return false;
    }
}
