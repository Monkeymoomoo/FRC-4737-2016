package org.usfirst.frc.team4737.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.vision.*;
import org.usfirst.frc.team4624.robot.input.XboxController;
import org.usfirst.frc.team4737.robot.auton.*;
import org.usfirst.frc.team4737.robot.drive.DriveControl;
import org.usfirst.frc.team4737.robot.networktables.JetsonComm;
import org.usfirst.frc.team4737.robot.shooter.*;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as
 * described in the IterativeRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the manifest file in the resource directory.
 *
 * @author Brian Semrau
 * @version 1/5/2016
 */
public class Robot extends IterativeRobot {

    // Robot instance

    public static Robot instance;

    // Version numbering

    public static final String ROBOT_NAME = "Scorpio";

    // Enumerations

    private enum Obstacle {
        NONE("None"),
        PORTCULLIS("Portcullis"),
        CHEVAL_DE_FRISE("Cheval de Frise"),
        MOAT("Moat"),
        RAMPARTS("Ramparts"),
        DRAWBRIDGE("Drawbridge"),
        SALLY_PORT("Sally Port"),
        ROCK_WALL("Rock Wall"),
        ROUGH_TERRAIN("Rough Terrain"),
        LOW_BAR("Low Bar");

        public final String name;

        Obstacle(String name) {
            this.name = name;
        }
    }

    // SmartDashboard items

    private SendableChooser autonomousChooser;

    // Input devices

    // TODO determine if we're using an Xbox controller
    private Joystick driveStick;
    private Joystick opStick;

    // Sensors

    public AxisCamera dscamera;
    public BuiltInAccelerometer builtInAccelerometer;
    public AHRS navXSensor;

    // Talons

//    private CANTalon chamberLeftTalon;  // B
//    private CANTalon chamberRightTalon; // A
//    private CANTalon shooterLeftTalon;  // C
//    private CANTalon shooterRightTalon; // D

    // Pneumatics

//    private Compressor compressor;

    // Robot controllers

    public DriveControl driveControl;
    public ShooterControl shootControl;
    public ShooterArmControl armControl;
    private AutonTaskOrganizer autonomousProgram;
    private ShootTask shootTask;

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    @Override
    public void robotInit() {
        Log.info(ROBOT_NAME);
        instance = this;

        // ############################################################################
        // Initialize input
        // ############################################################################

        Log.info("Initializing input..");

        driveStick = new Joystick(0);
        opStick = new Joystick(1);

        // ############################################################################
        // Initialize sensors
        // ############################################################################

        Log.info("Initializing sensors..");

//        dscamera = new AxisCamera("10.47.37.11");
//        dscamera.writeResolution(AxisCamera.Resolution.k320x240);
//        dscamera.writeCompression(30);

        builtInAccelerometer = new BuiltInAccelerometer();

        navXSensor = new AHRS(SPI.Port.kMXP);
        navXSensor.startLiveWindowMode();

        // ############################################################################
        // Initialize motors/pneumatics
        // ############################################################################

        Log.info("Initializing actuators..");

        CANTalon frontLeftTalon = new CANTalon(10);
        CANTalon frontRightTalon = new CANTalon(11);
        CANTalon midLeftTalon = new CANTalon(12);
        CANTalon midRightTalon = new CANTalon(13);
        CANTalon rearLeftTalon = new CANTalon(14);
        CANTalon rearRightTalon = new CANTalon(15);

//        CANTalon chamberLeftTalon = new CANTalon(16);
//        CANTalon chamberRightTalon = new CANTalon(17);
//        CANTalon shooterLeftTalon = new CANTalon(18);
//        CANTalon shooterRightTalon = new CANTalon(19);
//
//        CANTalon armTalon = new CANTalon(20);

//        compressor = new Compressor(0);
//        compressor.start();

        // ############################################################################
        // Initialize robot controllers
        // ############################################################################

        Log.info("Creating robot controllers..");

        driveControl = new DriveControl(
                frontLeftTalon, frontRightTalon,
                midLeftTalon, midRightTalon,
                rearLeftTalon, rearRightTalon);

//        shootControl = new ShooterControl(
//                chamberLeftTalon, chamberRightTalon,
//                shooterLeftTalon, shooterRightTalon);
//
//        armControl = new ShooterArmControl(armTalon);

        // ############################################################################
        // Initialize SmartDashboard items
        // ############################################################################

        Log.info("Creating SmartDashboard..");

        // Init JetsonComm here, because it's related to NetworkTables
        JetsonComm.init();

        autonomousChooser = new SendableChooser();
        Obstacle[] strategies = Obstacle.values();
        autonomousChooser.addDefault(strategies[0].name, strategies[0]);
        for (int i = 1; i < strategies.length; i++) {
            autonomousChooser.addObject(strategies[i].name, strategies[i]);
        }
        SmartDashboard.putData("Autonomous Strategy", autonomousChooser);

        // ############################################################################

        Log.info("Robot init complete!");
    }

    /**
     * Calmly brings all active mechanical systems to a state where the robot is likely to cause no harm.
     * This disables drive systems, shooters, and anything creating major mechanical motion.
     */
    public void idleSystems() {
        // Reset all components to a safe, idle state.
        // - Idle drive motors
        // - Idle all shooter motors
        // - Leave compressor alone
        driveControl.enterSafeState();
//        shootControl.enterSafeState();
    }

    /**
     * Execute any functions that should be running whenever the robot is supplied power, and not emergency stopped.
     * This is called at the end of any other periodic function, giving them priority.
     */
    public void commonPeriodic() {
        // Tasks
//        if (shootTask != null)
//            shootTask.periodic();

        // Controllers
        driveControl.periodic();
//        shootControl.periodic();
//        armControl.periodic();
    }

    @Override
    public void autonomousInit() {
        // TODO rework how auton strategies are done
//        Obstacle strategy = (Obstacle) autonomousChooser.getSelected();
//        switch (strategy) {
//            case NONE:
//                autonomousProgram = new AutonTaskOrganizer();
//                break;
//            case PORTCULLIS:
//                break;
//            case CHEVAL_DE_FRISE:
//                break;
//            case MOAT:
//                break;
//            case RAMPARTS:
//                break;
//            case DRAWBRIDGE:
//                break;
//            case SALLY_PORT:
//                break;
//            case ROCK_WALL:
//                break;
//            case ROUGH_TERRAIN:
//                break;
//            case LOW_BAR:
//                break;
//        }
    }

    @Override
    public void autonomousPeriodic() {
//        autonomousProgram.periodic();

        commonPeriodic();
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        // Arcade drive

        double deadzone = 0.05;
        double xAxis = driveStick.getRawAxis(0);
        double yAxis = driveStick.getRawAxis(1);
        if (Math.abs(xAxis) < deadzone) xAxis = 0;
        if (Math.abs(yAxis) < deadzone) yAxis = 0;
        driveControl.arcadeControl(xAxis, yAxis, true);
//
//        // Shooter control
//
//        if (opStick.getRawButton(5)) {
//            shootControl.intake();
//        } else if (opStick.getRawButton(6)) {
//            shootControl.dropBall();
//        } else {
//            if (opStick.getRawButton(1)) {
//                if (shootTask == null)
//                    shootTask = new ShootTask();
//            } else if (shootTask != null) {
//                shootTask.reportFailure();
//                shootTask = null;
//            }
//        }

//        armControl.setTarget(45);
//        armControl.enable();
//        System.out.println(armControl.talon.getAnalogInPosition());
//        System.out.println(armControl.pidController.get());

        commonPeriodic();
    }

    /**
     * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {
//        armControl.disable();
    }

    @Override
    public void disabledPeriodic() {
        commonPeriodic();
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
        commonPeriodic();
    }

}
