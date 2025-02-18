/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.mcqueencode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/*
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */



public class BaseOpMode extends LinearOpMode {

	// Declare OpMode members.
	protected ElapsedTime runtime = new ElapsedTime();

	public DcMotor frontRight   = null;
	public DcMotor frontLeft  = null;
	public DcMotor backRight = null;
	public DcMotor backLeft = null;
	public DcMotor armLift = null;
	public DcMotor armExtend = null;
	public DcMotor elbow = null;
	public Servo claw = null;
	public Servo rotate = null;
	public Servo wrist = null;


	// Calculate the COUNTS_PER_INCH for your specific drive train.
	// Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
	// For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
	// For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
	// This is gearing DOWN for less speed and more torque.
	// For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
	static final double	 COUNTS_PER_MOTOR_REV	= 550 ;	// eg: TETRIX Motor Encoder
	static final double	 DRIVE_GEAR_REDUCTION	= 0.705 ;	 // No External Gearing.
	static final double	 WHEEL_DIAMETER_INCHES   = 4.0 ;	 // For figuring circumference
	static final double	 COUNTS_PER_INCH		 = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
	static final double	 DRIVE_SPEED			 = 0.3; //0.6;
	static final double	 TURN_SPEED			  = 0.5; //0.5;

	static final double	 STRAFE_SPEED			= 0.25; // 0.5;

	static final double	 INCHES_PER_NINETY_DEGREES = 21.0; //TODO calibrate later
	static final double	 STRAFE_CORRECTION_FACTOR = 0.869; //TODO calibrate later

	public double clawDefault = 0.5;
	public double claw_speed = 0.01;//0.01;
	public double minClaw = 0.5; // open
	public double maxClaw = 0.0; // closed

	public double maxArm = 1700; // Max extension limit
	public double minArm = 0;
	public double armSpeed = 0.25;

	public double maxShoulder = 1300; // 1300 ticks hardware stop
	public double minShoulder = 0;
	public double shoulderSpeed = 0.25;

	public double wristDefault = 0.5;
	public double WRIST_ROTATE_SPEED = 0.1;//0.01;
	public double maxWristR = 1.0;
	public double minWristR = 0.0;

	public double WRIST_LIFT_SPEED = 0.1;//0.01;
	public double maxWristL = 0.75;  	//Point out
	public double minWristL = 0.5; 	//Point down

	// 
	public double ELBOW_SPEED = 0.1;
	public double maxElbow = 30;
	public double minElbow = -30;
	
	public double ROTATE_SPEED = 0.2;
	public double minRotate = 0.25;
	public double maxRotate = 0.75;

	public static boolean encodersNeedInitializing = true;

	@Override
	public void runOpMode() {
	}

	public void initialize() {
		telemetry.addData("Status", "Initialized");
		telemetry.update();

		// Initialize the hardware variables. Note that the strings used here as parameters
		// to 'get' must correspond to the names assigned during the robot configuration
		// step (using the FTC Robot Controller app on the phone).
		frontRight  = hardwareMap.get(DcMotor.class, "frontRight");
		frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
		backRight  = hardwareMap.get(DcMotor.class, "backRight");
		backLeft = hardwareMap.get(DcMotor.class, "backLeft");

		armLift = hardwareMap.get(DcMotor.class, "armLift"); // Arm joint?
		armExtend = hardwareMap.get(DcMotor.class, "armExtend");
		elbow = hardwareMap.get(DcMotor.class, "elbow");

		claw = hardwareMap.get(Servo.class, "claw");
		rotate = hardwareMap.get(Servo.class, "rotate");
		wrist = hardwareMap.get(Servo.class, "wrist");


		// To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
		// Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
		// Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
		frontLeft.setDirection(DcMotor.Direction.REVERSE);
		backLeft.setDirection(DcMotor.Direction.REVERSE);
		frontRight.setDirection(DcMotor.Direction.FORWARD);
		backRight.setDirection(DcMotor.Direction.FORWARD);

		armLift.setDirection(DcMotor.Direction.FORWARD);
		armExtend.setDirection(DcMotor.Direction.REVERSE);
		elbow.setDirection(DcMotor.Direction.REVERSE);

		if (encodersNeedInitializing) {
			armLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			armExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // todo: change this to 2 motors
			elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

			frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			//encodersNeedInitializing = false;
		}
		// Set the motors to RUN_USING_ENCODER
		frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		armLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		armExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // todo: change this to 2 motors
		elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		armExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		armLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

	} // End of initialize()

// Auto functions!
/*
NEW VERSION BELOW
	public void encoderDrive(double speed,
							 double frontLeftInches, double frontRightInches,
							 double backLeftInches, double backRightInches)
	{



		int newFrontLeftTarget;
		int newFrontRightTarget;
		int newBackRightTarget;
		int newBackLeftTarget;


		// Ensure that the OpMode is still active
		if (opModeIsActive()) {

			// Determine new target position, and pass to motor controller
			newFrontLeftTarget = frontLeft.getCurrentPosition() + (int)(frontLeftInches * COUNTS_PER_INCH);
			newFrontRightTarget = frontRight.getCurrentPosition() + (int)(frontRightInches * COUNTS_PER_INCH);
			newBackLeftTarget = backLeft.getCurrentPosition() + (int)(backLeftInches * COUNTS_PER_INCH);
			newBackRightTarget = backRight.getCurrentPosition() + (int)(backRightInches * COUNTS_PER_INCH);

			frontLeft.setTargetPosition(newFrontLeftTarget);
			frontRight.setTargetPosition(newFrontRightTarget);
			backLeft.setTargetPosition(newBackLeftTarget);
			backRight.setTargetPosition(newBackRightTarget);

			// Turn On RUN_TO_POSITION
			frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

			// start motion.
			frontLeft.setPower(Math.abs(speed));
			frontRight.setPower(Math.abs(speed));
			backLeft.setPower(Math.abs(speed));
			backRight.setPower(Math.abs(speed));

			// keep looping while we are still active, and there is time left, and both motors are running.
			// Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
			// its target position, the motion will stop.  This is "safer" in the event that the robot will
			// always end the motion as soon as possible.
			// However, if you require that BOTH motors have finished their moves before the robot continues
			// onto the next step, use (isBusy() || isBusy()) in the loop test.
			while (opModeIsActive() &&
					(frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {

				// Display it for the driver.
				telemetry.addData("Running to",  " %7d :%7d", newFrontLeftTarget,  newFrontRightTarget);
				telemetry.addData("Currently at",  " at %7d :%7d",
						frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
				telemetry.update();
			}

			// Stop all motion;
			frontLeft.setPower(0);
			frontRight.setPower(0);
			backLeft.setPower(0);
			backRight.setPower(0);

			// Turn off RUN_TO_POSITION
			frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

			sleep(250);   // optional pause after each move.
		}
	}
*/

	public void encoderDrive(double speed,
							 double frontLeftInches, double frontRightInches,
							 double backLeftInches, double backRightInches) {

		// Declare target variables
		int newFrontLeftTarget = 0;
		int newFrontRightTarget = 0;
		int newBackLeftTarget = 0;
		int newBackRightTarget = 0;
		
		double maxDistance = Math.max(Math.abs(frontLeftInches), Math.abs(frontRightInches));
		maxDistance = Math.max(maxDistance, Math.abs(backLeftInches));
		maxDistance = Math.max(maxDistance, Math.abs(backRightInches));
		
		double frSpeed = Math.abs(speed) * Math.abs(frontRightInches) / maxDistance;
		double flSpeed = Math.abs(speed) * Math.abs(frontLeftInches) / maxDistance;
		double brSpeed = Math.abs(speed) * Math.abs(backRightInches) / maxDistance;
		double blSpeed = Math.abs(speed) * Math.abs(backLeftInches) / maxDistance;

		// Ensure that the OpMode is still active
		if (opModeIsActive()) {
			// Reset encoders for all motors that will be moving
			if (frontLeftInches != 0) frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			if (frontRightInches != 0) frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			if (backLeftInches != 0) backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			if (backRightInches != 0) backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

			// Calculate new targets
			if (frontLeftInches != 0) {
				newFrontLeftTarget = (int)(frontLeftInches * COUNTS_PER_INCH);
				frontLeft.setTargetPosition(newFrontLeftTarget);
				frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
				frontLeft.setPower(flSpeed);
			}

			if (frontRightInches != 0) {
				newFrontRightTarget = (int)(frontRightInches * COUNTS_PER_INCH);
				frontRight.setTargetPosition(newFrontRightTarget);
				frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
				frontRight.setPower(frSpeed);
			}

			if (backLeftInches != 0) {
				newBackLeftTarget = (int)(backLeftInches * COUNTS_PER_INCH);
				backLeft.setTargetPosition(newBackLeftTarget);
				backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
				backLeft.setPower(blSpeed);
			}

			if (backRightInches != 0) {
				newBackRightTarget = (int)(backRightInches * COUNTS_PER_INCH);
				backRight.setTargetPosition(newBackRightTarget);
				backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
				backRight.setPower(brSpeed);
			}

			// Add telemetry for debugging
			telemetry.addData("Target FL:FR:BL:BR", "%7d:%7d:%7d:%7d",
					newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget);
			telemetry.update();

			// Keep looping while we are still active and at least one motor is running
			while (opModeIsActive() &&
					(frontLeftInches == 0 || frontLeft.isBusy()) &&
							(frontRightInches == 0 || frontRight.isBusy()) &&
							(backLeftInches == 0 || backLeft.isBusy()) &&
							(backRightInches == 0 || backRight.isBusy()) ) 
			{
				// Display current positions
				telemetry.addData("Current FL:FR:BL:BR", "%7d:%7d:%7d:%7d",
						frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
						backLeft.getCurrentPosition(), backRight.getCurrentPosition());
				telemetry.update();
			}

			// Stop all motors
			frontLeft.setPower(0);
			frontRight.setPower(0);
			backLeft.setPower(0);
			backRight.setPower(0);

			// Reset all motors to RUN_USING_ENCODER mode
			frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//		sleep(100);   // Short pause after movement
		}
	}
	public void driveForward(double distance, double speed){

		encoderDrive(speed, distance, distance, distance, distance);
	}

	// Purely a convenience function - with this, we can just call driveForward(12)
	// without bothering to state a speed
	public void driveForward(double distance) {
		driveForward(distance, DRIVE_SPEED);
	}

	public void strafeLeft(double distanceIn, double speed){
		double distance = distanceIn * STRAFE_CORRECTION_FACTOR;
		encoderDrive(speed, -distance, distance,
				distance, -distance);
	}

	public void strafeLeft(double distance){
		strafeLeft(distance, STRAFE_SPEED); //
	}

	public void turnRight(double degrees, double speed){
		double inches = (degrees / 90.0) * INCHES_PER_NINETY_DEGREES;
		encoderDrive (speed, inches, -inches, inches, -inches);
	}

	public void turnRight(double degrees){
		turnRight (degrees, TURN_SPEED);
	}

	public void driveForwardDiagonalRight(double inches, double speed){
		encoderDrive (speed, inches, 0, 0, inches);
	}
	
	public void driveForwardDiagonalRight(double inches){
		driveForwardDiagonalRight(inches, DRIVE_SPEED);

	}

	public void driveForwardDiagonalLeft(double inches, double speed){
		encoderDrive (speed, 0, inches, inches, 0);
		
	}

	public void driveForwardDiagonalLeft(double inches){
		driveForwardDiagonalLeft(inches, DRIVE_SPEED);
	}

	public void moveArmExtend(int targetPosition, double speed)
	{
		armExtend.setTargetPosition(targetPosition);

		armExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		armExtend.setPower(Math.abs(speed));

		while (opModeIsActive() &&
				armExtend.isBusy())


		{
			telemetry.addData("Running to", targetPosition);
			telemetry.addData("Currently at", armExtend.getCurrentPosition());
			telemetry.update();
		}
 //


		//Stop all motors
		armExtend.setPower(0);

		//Turn off RUN_AT_POSITION
		armExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}
	
	public void armLiftAndExtend(int liftTarget, int extendTarget, int elbowTarget, int wristTarget, int extendStartsAt)
	{
		armLift.setTargetPosition(liftTarget);
		armExtend.setTargetPosition(extendTarget);
		elbow.setTargetPosition(elbowTarget);

		armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		armExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		armLift.setPower(shoulderSpeed);

		while (opModeIsActive() 
			&& armLift.isBusy() 
			&& armLift.getCurrentPosition() < extendStartsAt)
		{
			telemetry.addData("Running to", liftTarget);
			telemetry.addData("Currently at", armLift.getCurrentPosition());
			telemetry.update();
		}
		
		armExtend.setPower(armSpeed);
		elbow.setPower(ELBOW_SPEED);
		
		while (opModeIsActive() 
			&& armLift.isBusy() 
			)
		{
			telemetry.addData("Running to", liftTarget);
			telemetry.addData("Currently at", armLift.getCurrentPosition());
			telemetry.update();
		}
 //


		//Stop all motors
		armExtend.setPower(0);

		//Turn off RUN_AT_POSITION
		armExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}
} // End of the OpMode class
