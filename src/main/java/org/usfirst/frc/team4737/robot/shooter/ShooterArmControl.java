package org.usfirst.frc.team4737.robot.shooter;

import edu.wpi.first.wpilibj.*;

/**
 * @author Brian Semrau
 * @version 2/10/2016
 */
public class ShooterArmControl {

    public CANTalon talon;
    public PIDController pidController;

    public ShooterArmControl(CANTalon talon) {
        this.talon = talon;
        this.talon.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
        this.talon.setPIDSourceType(PIDSourceType.kDisplacement);

        pidController = new PIDController(0.001, 0, 0, this.talon, this.talon); // TODO tune
        pidController.setInputRange(0, 1023);
        pidController.setOutputRange(-1.0, 1.0);
        pidController.setAbsoluteTolerance(5); // TODO tune
    }

    public void periodic() {
        if (pidController.isEnabled())
            talon.set(pidController.get());
        else talon.set(0);
    }

    public void enable() {
        pidController.enable();
    }

    public void disable() {
        pidController.disable();
    }

    public void setTarget(double angle) {
        pidController.setSetpoint(angle * 1024.0 / 270.0);
    }

}
