package org.usfirst.frc.team4737.robot;

import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.vision.*;
import org.usfirst.frc.team4624.robot.input.XboxController;
import org.usfirst.frc.team4737.robot.auton.AutonTaskOrganizer;
import org.usfirst.frc.team4737.robot.drive.DriveControl;

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

    private XboxController controller;
    private Joystick joystick;

    // Control Electronics

    private PowerDistributionPanel pdp;
    private Compressor compressor;

    // Sensors

    private AxisCamera dscamera;
    private AnalogInput proximityUDS_RL;
    private AnalogInput proximityUDS_RR;

    // Actuators

    private CANTalon frontLeftTalon;
    private CANTalon frontRightTalon;
    private CANTalon rearLeftTalon;
    private CANTalon rearRightTalon;

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

        // Initialize input
        Log.info("Initializing input..");

        if (DriverStation.getInstance().getStickAxisCount(0) == 6) {
            controller = new XboxController(0);
            joystick = new Joystick(1);
        } else {
            controller = new XboxController(1);
            joystick = new Joystick(0);
        }


        // Initialize hardware
        Log.info("Initializing hardware..");

        pdp = new PowerDistributionPanel();
        compressor = new Compressor(0);
        compressor.start();

        // Initialize sensors
        Log.info("Initializing sensors..");

        dscamera = new AxisCamera("10.47.37.11");
        dscamera.writeResolution(AxisCamera.Resolution.k320x240);

//        proximityUDS_RL = new AnalogInput(0);
//        proximityUDS_RR = new AnalogInput(1);

        // Initialize actuators
        Log.info("Initializing actuators..");

        // TODO init talons
//        frontLeftTalon = new CANTalon(10);
//        frontRightTalon = new CANTalon(11);
//        rearLeftTalon = new CANTalon(12);
//        rearRightTalon = new CANTalon(13);

        // Initialize robot controllers
        Log.info("Creating robot controllers..");

        driveControl = new DriveControl(new CANTalon[]{
                frontLeftTalon, frontRightTalon,
                rearLeftTalon, rearRightTalon
        }, new Encoder[]{
                // TODO list encoders
        });
        // autonomousController is initialized in autonomousInit()

        // Initialize SmartDashboard items
        Log.info("Creating SmartDashboard..");

        autonomousChooser = new SendableChooser();
        AutonStrategy[] strategies = AutonStrategy.values();
        autonomousChooser.addDefault(strategies[0].name, strategies[0]);
        for (int i = 1; i < strategies.length; i++) {
            autonomousChooser.addObject(strategies[i].name, strategies[i]);
        }
        SmartDashboard.putData("Autonomous Strategy", autonomousChooser);

        // TODO add more dashboard items

        Log.info("Robot init complete!");
    }

    public void resetSystems() {
        // Reset all components to a safe, idle state.

    }

    @Override
    public void autonomousInit() {
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
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        // Display camera to driver station

        NIVision.Image image = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        dscamera.getImage(image);
        CameraServer.getInstance().setImage(image);
    }

    /**
     * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

}
