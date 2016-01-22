package org.usfirst.frc.team4737.robot.auton;

import org.usfirst.frc.team4737.robot.*;

/**
 * @author Brian Semrau
 * @version 1/10/2016
 */
public class AutonTaskOrganizer {

    private int currentTask;
    private AutonTask[] tasks;

    private boolean stopped;

    public AutonTaskOrganizer(AutonTask... tasks) {
        // Check if there are any tasks given.
        // If there are none, stop
        if (tasks.length == 0) {
            stopped = true;
            return;
        }

        this.tasks = tasks;
        currentTask = 0;

    }

    public void periodic() {
        if (stopped) return;

        // If we have reached the end of our tasks, stop running.
        if (currentTask > tasks.length) {
            stopped = true;
            return;
        }

        // Run our current task
        tasks[currentTask].periodic();

        // If the task is completed, move on to the next one.
        if (tasks[currentTask].completed()) {
            Log.info("Completed autonomous " + tasks[currentTask].name() + " task.");
            currentTask++;
        }
    }

    public void reportFailure() {
        // Stop everything and reset all systems to a safe idling state
        stopped = true;
        Robot.instance.resetSystems();
    }

}
