package org.usfirst.frc.team4737.robot.auton;

import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.drive.DriveControl;

/**
 * @author Brian Semrau
 * @version 2/4/2016
 */
public class DriveTask extends AutonTask {

    /**
     * The maximum reasonable acceleration value for the robot to drive
     */
    private static final double MAX_ACCEL = 1.0; // TODO tune
    private static final double MAX_VEL = 1.0; // TODO tune

    private double driveDistance;

    private double startTime;
    private int state;

    private double accelTime;
    private double constVTime;
    private double accelDist;
    private double constVDist;

    private PIDController pidController;

    public DriveTask(double distance) {
        super("Drive " + (int) (distance * 5) / 5.0);

        driveDistance = distance;

        // Calculate time required for each segment of drive control
        accelTime = 2 * MAX_VEL / MAX_ACCEL;
        accelDist = MAX_ACCEL * accelTime * accelTime;
        if (accelDist > driveDistance) {
            accelTime = Math.sqrt(driveDistance / MAX_ACCEL);
        } else if (accelDist < driveDistance) {
            constVDist = driveDistance - accelDist;
            constVTime = constVDist / MAX_VEL;
        }

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
        double val;
        double currentTime;

        switch (state) {
            case 1: // Startup
                pidController.setSetpoint(MAX_ACCEL);
                pidController.enable();
                Robot.instance.driveControl.setPidOutputMode(DriveControl.OutputMode.DRIVING);
                startTime = System.nanoTime() / 1000000000.0;
                state++;
            case 2: // Accelerating
                currentTime = System.nanoTime() / 1000000000.0;
                if (currentTime - startTime > accelTime) {
                    pidController.setSetpoint(0);
                    state++;
                }

                val = pidController.get();
                Robot.instance.driveControl.tankControl(val, val, false);

                break;
            case 3: // Driving
                currentTime = System.nanoTime() / 1000000000.0;
                if (currentTime - startTime > accelTime + constVTime) {
                    pidController.setSetpoint(-MAX_ACCEL);
                    state++;
                }

                val = pidController.get();
                Robot.instance.driveControl.tankControl(val, val, false);

                break;
            case 4: // Decelerating
                currentTime = System.nanoTime() / 1000000000.0;
                if (currentTime - startTime > accelTime * 2 + constVTime) {
                    pidController.disable();
                    state++;
                }

                val = pidController.get();
                Robot.instance.driveControl.tankControl(val, val, false);

                break;
            case 5: // Finished
                break;
        }
    }

    @Override
    public boolean completed() {
        return state == 5;
    }

}
