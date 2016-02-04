package org.usfirst.frc.team4737.robot.auton;

import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4737.robot.Robot;

/**
 * @author Brian Semrau
 * @version 2/4/2016
 */
public class DriveTask extends AutonTask {

    /**
     * The maximum reasonable acceleration value for the robot to drive
     */
    private static final double MAX_ACCEL = 1.0; // TODO tune

    private double driveDistance;

    private double startTime;
    private int state;

    private double totalTime;
    private double accelTime;

    private PIDController pidController;

    public DriveTask(double distance) {
        super("Drive " + (int) (distance * 5) / 5.0);

        driveDistance = distance;
//        totalTime = 2.0 * Math.sqrt(driveDistance / MAX_ACCEL);
        state = 1;

        // TODO tune
        pidController = new PIDController(.5, .00001, 0, new PIDSource() {
            @Override
            public void setPIDSourceType(PIDSourceType pidSource) {
            }

            @Override
            public PIDSourceType getPIDSourceType() {
                return PIDSourceType.kDisplacement;
            }

            @Override
            public double pidGet() {
                double x = Robot.instance.navXSensor.getWorldLinearAccelX();
                double y = Robot.instance.navXSensor.getWorldLinearAccelX();
                double magnitude = Math.hypot(x, y);

                double angle = Math.atan2(y, x);

                if (Math.abs(angle - Robot.instance.navXSensor.getYaw()) > Math.PI / 2) {
                    magnitude = -magnitude;
                }

                return magnitude;
            }
        }, Robot.instance.driveControl);
    }

    @Override
    public void periodic() {
        switch (state) {
            case 1:
                if (!pidController.isEnabled()) {
                    pidController.enable();
                    startTime = System.nanoTime() / 1000000000.0;
                } else {
                    double currentTime = System.nanoTime() / 1000000000.0;
//                    if (currentTime > startTime + midTime) {
//                        state++;
//                    }
                }
                double val = pidController.get();
                Robot.instance.driveControl.tankControl(val, val, false);

                break;
            case 2:

                break;
        }
    }

    @Override
    public boolean completed() {
        return state == 3;
    }

}
