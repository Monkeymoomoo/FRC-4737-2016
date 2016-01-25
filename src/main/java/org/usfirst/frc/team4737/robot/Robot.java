package org.usfirst.frc.team4737.robot;

import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.vision.*;
import org.usfirst.frc.team4624.robot.input.XboxController;
import org.usfirst.frc.team4737.robot.auton.AutonTaskOrganizer;
import org.usfirst.frc.team4737.robot.drive.DriveControl;

import java.io.BufferedReader;

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

    public static final String NAME = "Piece of shit robot";
    public static final int MAJOR = 1;
    public static final int MINOR = 0;
    public static final int UPDATE = 1;
    public static final String VERSION = NAME + " " + MAJOR + "." + MINOR + "." + UPDATE;

    // Enumerations

    private enum AutonStrategy {
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

        AutonStrategy(String name) {
            this.name = name;
        }
    }

    // SmartDashboard items

    private SendableChooser autonomousChooser;

    // Input devices

    // TODO determine if we're using an Xbox controller
    private XboxController controller;
    private Joystick joystick;

    // Sensors

    private AxisCamera dscamera;
    private AnalogInput proximityUDS_RL;
    private AnalogInput proximityUDS_RR;
    private BuiltInAccelerometer builtInAccelerometer;

    private Encoder frontLeftEncoder;
    private Encoder frontRightEncoder;
    private Encoder rearLeftEncoder;
    private Encoder rearRightEncoder;

    // Motors

    private CANTalon frontLeftTalon;
    private CANTalon frontRightTalon;
    private CANTalon rearLeftTalon;
    private CANTalon rearRightTalon;

    // Pneumatics

    private Compressor compressor;

    // Robot controllers

    private DriveControl driveControl;
    private AutonTaskOrganizer autonomousProgram;

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    @Override
    public void robotInit() {
        Log.info("Running " + VERSION);
        instance = this;

        // ############################################################################
        // Initialize input
        // ############################################################################

        Log.info("Initializing input..");

        if (DriverStation.getInstance().getStickAxisCount(0) == 6) {
            controller = new XboxController(0);
            joystick = new Joystick(1);
        } else {
            controller = new XboxController(1);
            joystick = new Joystick(0);
        }

        // ############################################################################
        // Initialize sensors
        // ############################################################################

        Log.info("Initializing sensors..");

        dscamera = new AxisCamera("10.47.37.11");
        dscamera.writeResolution(AxisCamera.Resolution.k320x240);
        dscamera.writeCompression(30);

//        proximityUDS_RL = new AnalogInput(0);
//        proximityUDS_RR = new AnalogInput(1);

        builtInAccelerometer = new BuiltInAccelerometer();

        // TODO init encoders
//        frontLeftEncoder = new Encoder(a, b, index?, reverse);
//        frontRightEncoder = new Encoder(a, b, index?, reverse);
//        rearLeftEncoder = new Encoder(a, b, index?, reverse);
//        rearRightEncoder = new Encoder(a, b, index?, reverse);

        // ############################################################################
        // Initialize motors/pneumatics
        // ############################################################################

        Log.info("Initializing actuators..");

        // TODO init talons
//        frontLeftTalon = new CANTalon(10);
//        frontRightTalon = new CANTalon(11);
//        rearLeftTalon = new CANTalon(12);
//        rearRightTalon = new CANTalon(13);

        compressor = new Compressor(0);
        compressor.start();

        // ############################################################################
        // Initialize robot controllers
        // ############################################################################

        Log.info("Creating robot controllers..");

        driveControl = new DriveControl(
                frontLeftTalon, frontRightTalon,
                rearLeftTalon, rearRightTalon,
                frontLeftEncoder, frontRightEncoder,
                rearLeftEncoder, rearRightEncoder);

        //) autonomousController is initialized in autonomousInit()

        // ############################################################################
        // Initialize SmartDashboard items
        // ############################################################################

        Log.info("Creating SmartDashboard..");

        autonomousChooser = new SendableChooser();
        AutonStrategy[] strategies = AutonStrategy.values();
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
        // TODO
//        driveControl.enterSafeState();
//        shooterControl.enterSafeState();
    }

    /**
     * Execute any functions that should be running whenever the robot is supplied power, and not emergency stopped.
     * This is called at the end of any other periodic function, giving them priority.
     */
    public void commonPeriodic() {
    }

    @Override
    public void autonomousInit() {
        // TODO rework how auton strategies are done
        AutonStrategy strategy = (AutonStrategy) autonomousChooser.getSelected();
        switch (strategy) {
            case NONE:
                autonomousProgram = new AutonTaskOrganizer();
                break;
            case PORTCULLIS:
                break;
            case CHEVAL_DE_FRISE:
                break;
            case MOAT:
                break;
            case RAMPARTS:
                break;
            case DRAWBRIDGE:
                break;
            case SALLY_PORT:
                break;
            case ROCK_WALL:
                break;
            case ROUGH_TERRAIN:
                break;
            case LOW_BAR:
                break;
        }
    }

    @Override
    public void autonomousPeriodic() {
        commonPeriodic();
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        // Arcade drive using Xbox controller
        // Steer w/ L thumbstick
        // Throttle with triggers
        driveControl.arcadeControl(controller.leftStick.getX(), controller.rt.getY() - controller.lt.getY());

        commonPeriodic();
    }

    /**
     * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {
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
