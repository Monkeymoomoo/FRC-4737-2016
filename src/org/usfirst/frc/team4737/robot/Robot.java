
package org.usfirst.frc.team4737.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser chooser;

	Joystick driveStick;
	Joystick operator;
	RobotDrive drive;
	Talon fl, bl, fr, br;

	CANTalon shooterL, shooterR, arm, intake;

	DigitalInput ballIndicator;

	boolean intaking;

	final double shooterSpinTime = 0.5;
	final double shooterReleaseTime = 0.2;
	boolean shooting;
	double currentShooterSpinTime;

	double lastTime;
	double currentTime;

	final String goalviewCam = "cam0";
	final String ballviewCam = "cam1";
	String cameraInUse = null;
	int session;
	Image frame;

	boolean releasing;

	// auton
	double autonStartTime;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		//		chooser = new SendableChooser();
		//		chooser.addDefault("Default Auto", defaultAuto);
		//		chooser.addObject("My Auto", customAuto);
		//		SmartDashboard.putData("Auto choices", chooser);

		driveStick = new Joystick(0);
		operator = new Joystick(1);
		fl = new Talon(2);
		bl = new Talon(3);
		fr = new Talon(1);
		br = new Talon(0);
		drive = new RobotDrive(fl, bl, fr, br);

		shooterL = new CANTalon(25);
		shooterR = new CANTalon(24);
		arm = new CANTalon(22);
		intake = new CANTalon(23);

		ballIndicator = new DigitalInput(6);

		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

		//		ballviewCam = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		//		NIVision.IMAQdxConfigureGrab(ballviewCam);

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	public void autonomousInit() {
		//		autoSelected = (String) chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		//		System.out.println("Auto selected: " + autoSelected);

		//		autonStartTime = System.nanoTime() / 1000000000.0;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		updateCamera(goalviewCam);

//		if (autonStartTime < 0.95) {
//			arm.set(-0.3);
//		} else {
//			arm.set(0);
//		}
		//				
//		if (autonStartTime < 6) {
//			drive.arcadeDrive(-.5, 0);
//		}
		
		drive.arcadeDrive(-0.1, 0.1);

		//		switch (autoSelected) {
		//		case customAuto:
		//			// Low bar
		//			
		//			break;
		//		case defaultAuto:
		//		default:
		//			// Put default auto code here
		//			break;
		//		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		currentTime = System.nanoTime() / 1000000000.0;
		if (lastTime == 0)
			lastTime = currentTime;

		// Tell camera to update
		updateCamera();

		// Drive code
		double scale = driveStick.getRawAxis(3) * 0.8;
		double moveRate = -driveStick.getRawAxis(1) * scale;
		double turnRate = -driveStick.getRawAxis(2) * scale;
		if (scale < 0)
			turnRate = -turnRate;
		drive.arcadeDrive(moveRate, turnRate);

		// Ball intake
		//		if (operator.getRawButton(4)) {
		//			// Input
		//			intake.set(-0.2);
		//			intaking = true;
		//		} else if (operator.getRawButton(5)) {
		//			// Output
		//			intake.set(0.2);
		//			
		//			intaking = true;
		//		} else if (intaking) {
		//			intake.set(0);
		//			intaking = false;
		//		}

		if (!operator.getRawButton(6)) {
			double yAxis = operator.getRawAxis(1);
			if (Math.abs(yAxis) > 0.05) {
				intake.set(yAxis * yAxis * (yAxis > 0 ? 1 : -0.3)
				//						* (operator.getRawAxis(2) + 1) / 3
				);
				if (yAxis > 0) {
					if (!ballIndicator.get()) {
						shooterL.set(-1);
						shooterR.set(1);
					}
				} else {
					//					shooterL.set(-.2);
					//					shooterR.set(.2);
				}
				intaking = true;
			} else if (intaking) {
				intake.set(0);
				intaking = false;
			}
		}

		if (operator.getRawButton(2)) {
			shooterL.set(-1);
			shooterR.set(1);
			releasing = true;
		} else
			releasing = false;

		// Arms
		if (operator.getRawButton(6)) {
			arm.set(operator.getRawAxis(1) / 3);
		} else {
			arm.set(0);
		}

		// Ball alignment before shooting
		if (operator.getRawButton(9)) {

			if (!ballIndicator.get()) {
				intake.set(.3);
			} else {
				intake.set(0);
			}

		}

		// Shooting
		
		if (operator.getRawButton(1)) {
			shooting = true;
		} else {
			shooting = false;
			if (!intaking && !releasing) {
				shooterL.set(0);
				shooterR.set(0);
			}
		}
		
		if (shooting) {
			shooterL.set(1.0);
			shooterR.set(-1.0);
			
			if (operator.getRawButton(3)) {
				intake.set(-0.5);
			} else {
				intake.set(0);
			}
		} else if (!intaking && !releasing) {
			intake.set(0);
		}
		
//		if (operator.getRawButton(1)) {
//			if (!shooting) {
//				shooting = true;
//				currentShooterSpinTime = 0;
//			}
//		} else {
//			shooting = false;
//			if (!intaking && !releasing) {
//				shooterL.set(0);
//				shooterR.set(0);
//			}
//		}
//
//		if (shooting) {
//			shooterL.set(0.8);
//			shooterR.set(-0.8);
//			currentShooterSpinTime += currentTime - lastTime;
//
//			if (currentShooterSpinTime >= shooterSpinTime + shooterReleaseTime) {
//				// After the ball has been released
//				shooting = false;
//				intake.set(0);
//			} else if (currentShooterSpinTime >= shooterSpinTime) {
//				// After the shooters have spun up
//				intake.set(-0.5);
//			}
//		}

		lastTime = currentTime;
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		//		if (driveStick.getRawButton(6)) {
		//			fl.set(1);
		//			bl.set(1);
		//			fr.set(1);
		//			br.set(1);
		//		} else if (driveStick.getRawButton(4)) {
		//			fl.set(-1);
		//			bl.set(-1);
		//			fr.set(-1);
		//			br.set(-1);
		//		}
		updateCamera();
	}

	private void updateCamera() {
		updateCamera(driveStick.getRawButton(1) ? goalviewCam : ballviewCam);
	}

	private void updateCamera(String cam) {
		// Cameras not plugged in
		if (true) return;
		
		if (!cam.equals(cameraInUse)) {
			if (cameraInUse != null) {
				NIVision.IMAQdxStopAcquisition(session);
				NIVision.IMAQdxCloseCamera(session);
			}
			session = NIVision.IMAQdxOpenCamera(cam, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);

			NIVision.IMAQdxStartAcquisition(session);
			cameraInUse = cam;
		}

		NIVision.IMAQdxGrab(session, frame, 1);

		if (cam == goalviewCam) {
			int left = (int) SmartDashboard.getNumber("left", 0);
			int width = (int) SmartDashboard.getNumber("width", 10);
			int top = (int) SmartDashboard.getNumber("top", 0);
			int height = (int) SmartDashboard.getNumber("height", 10);
			NIVision.Rect rect = new NIVision.Rect(top, left, height, width);
			NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.0f);
		}

		CameraServer.getInstance().setImage(frame);
	}

}
