package org.usfirst.frc.team4737.robot.auton;

import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.drive.DriveControl;

/**
 * @author Brian Semrau
 * @version 2/19/2016
 */
public class TurnTask extends AutonTask {

    private PIDController pidController;

    public TurnTask(double yaw, boolean isRelativeAngle) {
        super("Turn " + yaw + " degrees");

//        double targetAngle = isRelativeAngle ? (Robot.instance.navXSensor.getYaw() : ();

        pidController = new PIDController(0.1, 0, 0, new PIDSource() {
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return null;
            }

            @Override
            public double pidGet() {
                return Robot.instance.navXSensor.getYaw();
            }
        }, Robot.instance.driveControl);

        Robot.instance.driveControl.setPidOutputMode(DriveControl.OutputMode.TURNING);
    }

    @Override
    public void periodic() {
        if (!pidController.isEnabled()) {
            // TODO
        }
    }

    @Override
    public boolean completed() {
        return false;
    }

}
