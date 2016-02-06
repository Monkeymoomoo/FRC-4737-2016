package org.usfirst.frc.team4737.robot.auton;

import org.usfirst.frc.team4737.robot.Robot;

/**
 * @author Brian Semrau
 * @version 2/6/2016
 */
public class ShootTask extends AutonTask {

    private static final double SPIN_TIME = 0.6;
    private static final double SHOOT_TIME = 0.2;

    private enum State {
        START, SPINNING, SHOOTING, DONE
    }

    private State state;
    private double startTime;

    public ShootTask() {
        super("Shoot");
        state = State.START;
    }

    @Override
    public void periodic() {
        switch (state) {
            case START:
                startTime = System.nanoTime() / 1000000000.0;
            case SPINNING:
                Robot.instance.shootControl.spinupShooter();
                if (System.nanoTime() / 1000000000.0 - startTime > SPIN_TIME) {
                    state = State.SHOOTING;
                }
                break;
            case SHOOTING:
                Robot.instance.shootControl.spinupShooter();
                Robot.instance.shootControl.releaseChamber();
                if (System.nanoTime() / 1000000000.0 - startTime > SPIN_TIME + SHOOT_TIME) {
                    state = State.DONE;
                    Robot.instance.shootControl.enterSafeState();
                }
                break;
        }
    }

    @Override
    public boolean completed() {
        return state == State.DONE;
    }

}
