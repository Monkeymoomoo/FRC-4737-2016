package org.usfirst.frc.team4737.robot.auton;

/**
 * @author Brian Semrau
 * @version 1/10/2016
 */
public abstract class AutonTask {

    private AutonTaskOrganizer organizer;

    private String name;

    public AutonTask(String name) {
        this.name = name;
    }

    public final void setOrganizer(AutonTaskOrganizer organizer) {
        this.organizer = organizer;
    }

    public final void reportFailure() {
        organizer.reportFailure();
    }

    public final String name() {
        return name;
    }

    public abstract void periodic();

    public abstract boolean completed();

}
