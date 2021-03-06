/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6057.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.IterativeRobotBase;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	
	private static final String kCenterAuto = "Center Auto";
	private static final String kLeftAuto = "Left Auto";
	private static final String kRightAuto = "Right Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser=new SendableChooser<>();
	
	// This line comments out the beginning code
	/*
	 * private static final String kDefaultAuto = "Default"; private static final
	 * String kCustomAuto = "My Auto"; private String m_autoSelected; private
	 * SendableChooser<String> m_chooser = new SendableChooser<>();
	 * 
	 * /** This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	Joystick controller = new Joystick(0);
	double rightTog;
	double leftTog;
	double conveyorIn;
	double conveyorOut;

	PWMVictorSPX frontLeft = new PWMVictorSPX(0);
	PWMVictorSPX rearLeft = new PWMVictorSPX(1);
	PWMVictorSPX frontRight = new PWMVictorSPX(2);
	PWMVictorSPX rearRight = new PWMVictorSPX(3);

	// Instantiation of collection and output systems
	// Conveyor Belt
	PWMVictorSPX leftConvey = new PWMVictorSPX(4);
	PWMVictorSPX rightConvey = new PWMVictorSPX(5);
	// Collector Arm
	PWMVictorSPX roller = new PWMVictorSPX(7);
	//PWMVictorSPX rightArm = new PWMVictorSPX(7);

	SpeedControllerGroup rightMotors = new SpeedControllerGroup(frontRight, rearRight);
	SpeedControllerGroup leftMotors = new SpeedControllerGroup(frontLeft, rearLeft);

	// Defines the variables as members of our Robot class

	Timer timer;
	private int switchside;

	// Initializes the variables in the robotInit method, this method is called when
	// the robot is initializing
	@Override
	public void robotInit() {
		m_chooser.addDefault(kLeftAuto, kLeftAuto);
		m_chooser.addObject(kCenterAuto, kCenterAuto);
		m_chooser.addObject(kRightAuto, kRightAuto);
		SmartDashboard.putData("Auto Choice", m_chooser);
	}

	// @Override

	/*
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */

	@Override

	public void autonomousInit() {
		
		m_autoSelected=m_chooser.getSelected();
		
		System.out.println("Auto Selected: "+m_autoSelected);
		
		timer = new Timer();
		timer.reset();
		timer.start();
		String get_signal = null;

		while (get_signal == null) {
			
			//Timer.delay(0.020);
			get_signal = DriverStation.getInstance().getGameSpecificMessage();
			
			if (get_signal.charAt(0) == 'L')

			{
				switchside = 1;// signal indicates left switch is ours
				DriverStation.reportWarning("Left detected", false);

			} else {
				switchside = 0; // signal indicates right switch is ours
				DriverStation.reportWarning("Right detected", false);
			}

		}
		return;

	}

	public int get_switchside() {

		return switchside;

	}

	/*
	 * This function is called periodically during autonomous.
	 */
	 @Override
	
	 public void autonomousPeriodic() {

switch (m_autoSelected) {

case kLeftAuto:
	default:
	if(switchside==1)
	{
		if(timer.get()<4)
			backUp();
		if(timer.get()>4)
		{
			driveStop();
			conveyor(1);
		}
		else
			conveyor(0);
	}
	else
	{
		if(timer.get()<4)
			backUp();
		if(timer.get()>4)
		{
			driveStop();	
		}
	}	
	
	break;

case kRightAuto:
	if(switchside==0)
	{
		if(timer.get()<4)
			backUp();
		if(timer.get()>4&& timer.get()<5)
		{
			driveStop();
			conveyor(1);
		}
		else
			conveyor(0);
	}
	else
	{
		if(timer.get()<4)
			backUp();
		if(timer.get()>4)
		{
			driveStop();
		}
	}
	break;
case kCenterAuto:
			if(timer.get()<2)
			{
				if(switchside == 1)
					turnLeft();
				else
					turnRight();	
			}
			if(timer.get()>2 &&timer.get()<5)
			{
				backUp();
			}
			if(timer.get()>5 &&timer.get()<6)
			{
				conveyor(1);
				driveStop();
			}
			else
				conveyor(0);
		break;
		}
}
	//driving direction methods

	public void backUp() {
	
		rearLeft.set(-.25);
		frontLeft.set(-.25);

		rearRight.set(.25);
		frontRight.set(.25);

	}

	public void turnRight() {

		rearLeft.set(.25);
		frontLeft.set(.25);

		rearRight.set(.3);
		frontRight.set(.3);
	}

	public void turnLeft() {

		rearLeft.set(-.3);
		frontLeft.set(-.3);

		rearRight.set(0.2);
		frontRight.set(0.2);
	}

	public void driveForward() {
		
		rearLeft.set(.25);
		frontLeft.set(.25);

		rearRight.set(-.25);
		frontRight.set(-.25);
	
	}
	
	public void driveStop()
	
	{
		rearLeft.set(0);
		frontLeft.set(0);

		rearRight.set(0);
		frontRight.set(0);
		
	}
	public void conveyor( int speed)
	{
		leftConvey.set(speed);
		rightConvey.set(-speed);
	}
	
	

	void drive() {
		leftTog = controller.getRawAxis(1) * -0.75;
		rightTog = controller.getRawAxis(5) * 0.75;

		// DriverStation.reportWarning("$L" + leftTog, false);
		// DriverStation.reportWarning("$R" + rightTog, false);

		rearLeft.set(leftTog);
		frontLeft.set(leftTog);

		rearRight.set(rightTog);
		frontRight.set(rightTog);
	}

	void conveyorBelt() {
		conveyorIn = controller.getRawAxis(3);
		conveyorOut = controller.getRawAxis(2);
/*


		if (conveyorIn >= 0.1) {
			leftConvey.set(controller.getRawAxis(3));
			rightConvey.set(controller.getRawAxis(3)*-1);
			//conveyorIn = controller.getRawAxis(3);
		}
		else if (conveyorOut >= 0.1) {
			leftConvey.set(controller.getRawAxis(2) * -1);
			rightConvey.set(controller.getRawAxis(2));
			//conveyorOut = controller.getRawAxis(2);
		}
		else {
			leftConvey.set(0);
			rightConvey.set(0);
		}
		 */
		if (conveyorIn >= 0.1) {
			leftConvey.set(1);
			rightConvey.set(-1);
			//conveyorIn = controller.getRawAxis(3);
		}
		else if (conveyorOut >= 0.1) {
			leftConvey.set(-1);
			rightConvey.set(1);
			//conveyorOut = controller.getRawAxis(2);
		}
		else {
			leftConvey.set(0);
			rightConvey.set(0);
		}
		
	}

	void moveRoller() {
		if (controller.getRawButton(6) && !controller.getRawButton(5)) // Checks if RB is pressed
		{
			roller.set(1);
			return;
		}
		if (controller.getRawButton(5) && !controller.getRawButton(6)) // Checks if LB is pressed
		{
			roller.set(-1);
			return;
		}
		else
		{
		roller.set(0);
		roller.set(0);
		return;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override

	public void teleopPeriodic() {
		drive();
		conveyorBelt();
		moveRoller();

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void testPeriodic() {

		LiveWindow.run();
		teleopPeriodic();

	}

}
