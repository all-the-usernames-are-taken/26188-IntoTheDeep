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

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.mcqueencode.BaseOpMode;

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

@TeleOp


public class Teleop_McQueen extends BaseOpMode {

	@Override
	public void runOpMode() {

		initialize();

		waitForStart();
		runtime.reset();

		// end of setup

		boolean wasAPressed = false;
		boolean wasYPrsed = false;
		boolean wasLBpressed = false;
		boolean wasRBpressed = false;
		boolean clawIsOpen = false; //todo: check if claw is open at the start

		int shoulderHalfway = 650; // Above this limit the wrist can go straight without breaking the size limit.
		int slideLimit = 1350; // Beyond here the claw *could* break the 42-inch limit

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {

			
//NEW CONTROLS CODE  - CEP

/*
########################################
 		GAMEPAD 1 - Driver Control
########################################
#
# DPAD UP 		- 	 
# DPAD DOWN 	- 
# DPAD LEFT 	- 	
# DPAD RIGHT 	- 
#
# LEFT STICK Y	- Forward / Reverse
# LEFT STICK X	- Left / Right
#
# RIGHT STICK Y -
# RIGHT STICK X - Turn Left / Right
#
# A				-
# B				-
# Y				-
# X				-
#
# L Bumper 		-
# R Bumper 		-
#
# L Trigger		-
# R Trigger		-
#
########################################
*/
			double horizontal = -gamepad1.left_stick_y; // L-stick up/down - Remember, Y stick value is reversed
			double vertical = gamepad1.left_stick_x * 1.1; // L-stick left/right - Counteract imperfect strafing (by multiplying by 1.1)
			double turn = gamepad1.right_stick_x; // R-stick left/right

			// Denominator is the largest motor power (absolute value) or 1
			// This ensures all the powers maintain the same ratio,
			// but only if at least one is out of the range [-1, 1]
			double denominator = Math.max(Math.abs(horizontal) + Math.abs(vertical) + Math.abs(turn), 1);
			// Denominator
			double frontLeftPower = (horizontal + vertical + turn) / denominator;
			double backLeftPower = (horizontal - vertical + turn) / denominator;
			double frontRightPower = (horizontal - vertical - turn) / denominator;
			double backRightPower = (horizontal + vertical - turn) / denominator;

			frontLeft.setPower(frontLeftPower);
			backLeft.setPower(backLeftPower);
			frontRight.setPower(frontRightPower);
			backRight.setPower(backRightPower);

/*
#################### TODO
# Key binds for [1]:
# [DPAD UP] - raise shoulder	 /\
# [DPAD DOWN] - lower shoulder <   >
# [DPAD LEFT] - extend arm		\/
# [DPAD RIGHT - detract arm
# 
####################

		Idea:
			   Make RB a shift button so we can cram more controls on the robot
			   Consider putting macros on RB?
			
			double horizontal = -gamepad1.left_stick_y; // L-stick up/down - Remember, Y stick value is reversed
			double vertical = gamepad1.left_stick_x * 1.1; // L-stick left/right - Counteract imperfect strafing (by multiplying by 1.1)
			double turn = gamepad1.right_stick_x; // R-stick left/right

			// Denominator is the largest motor power (absolute value) or 1
			// This ensures all the powers maintain the same ratio,
			// but only if at least one is out of the range [-1, 1]
			double denominator = Math.max(Math.abs(horizontal) + Math.abs(vertical) + Math.abs(turn), 1);
			// Denominator
			double frontLeftPower = (horizontal + vertical + turn) / denominator;
			double backLeftPower = (horizontal - vertical + turn) / denominator;
			double frontRightPower = (horizontal - vertical - turn) / denominator;
			double backRightPower = (horizontal + vertical - turn) / denominator;

			frontLeft.setPower(frontLeftPower);
			backLeft.setPower(backLeftPower);
			frontRight.setPower(frontRightPower);
			backRight.setPower(backRightPower);

*/
/*
########################################
 		GAMEPAD 2 - ARM CONTROLS
########################################
#
# DPAD UP 		- raise arm	 
# DPAD DOWN 	- lower arm
# DPAD LEFT 	- extend arm	
# DPAD RIGHT 	- retract arm
# 
# A				-
# B				-
# Y				-
# X				-
#
# L Bumper 		-
# R Bumper 		-
#
# L Trigger		-
# R Trigger		-
#
########################################
*/
	// EXTEND ARM
		if (gamepad2.dpad_left && armExtend.getCurrentPosition() < maxArm) {
			armExtend.setPower(armSpeed);
	// SOFTWARE LIMITER p1 - automatically lower the wrist
		if (armLift.getCurrentPosition() < shoulderHalfway) {
			wrist.setPosition(minWristL);
						}
			}
	// RETRACT ARM
		else if (gamepad2.dpad_right && armExtend.getCurrentPosition() > minArm) { 
			armExtend.setPower(-armSpeed);
			}
		else {
			armExtend.setPower(0);
			}
		
	// RAISE ARM
		if (gamepad2.dpad_up && armLift.getCurrentPosition() < maxShoulder) {
			armLift.setPower(shoulderSpeed);
			}
	// LOWER ARM
		else if (gamepad2.dpad_down && armLift.getCurrentPosition() > minShoulder) { 
			armLift.setPower(-shoulderSpeed);
			}
		else {
			armLift.setPower(0);
			}
			
	// CLAW OPEN
		if (gamepad2.right_trigger > 0.5 && clawIsOpen) {
			claw.setPosition(minClaw); // Sets the claw to the closed position.
			clawIsOpen = false; 
			}

	// CLAW CLOSE
		if (gamepad2.left_trigger > 0.5 && !clawIsOpen) {
			claw.setPosition(maxClaw); // Sets the claw to the open position.
			clawIsOpen = true; 
			}
			
	// WRIST
		if (wrist.getPosition() < minWristL && wrist.getPosition() > maxWristL) {
			wrist.setPosition(wrist.getPosition() + gamepad2.left_stick_y);
			}
			
	// ELBOW		
		if (gamepad2.left_bumper && elbow.getCurrentPosition() > 0) {
			elbow.setPower(-ELBOW_SPEED);
			}
			
		if (gamepad2.right_bumper && elbow.getCurrentPosition() < maxElbow) {
			elbow.setPower(ELBOW_SPEED);
			}

		// macro for claw
		if (gamepad2.b) {
			armLift.setTargetPosition(0); // reset elbow/wrist/armLift to 0
			elbow.setTargetPosition(0);
			armLift.setTargetPosition(0);

			claw.setPosition(minClaw); // open claw in case
			armExtend.setTargetPosition(1121 - armExtend.getCurrentPosition()); // arm extend goes to 1121
			wrist.setPosition(wristDefault); // this is assuming that wristDefault is in the middle
		}
		else if (!gamepad2.b) { // activates the MOMENT the driver lets go of B (please note)
			claw.setPosition(maxClaw); // close it!
			armExtend.setTargetPosition(0); // note: i suspect that you need to move the wrist sideways to prevent hitting the side of the sub
		}

		// hanging a specimen macro
		if (gamepad2.x) {
			armLift.setTargetPosition(1075); // Set armLift to up/down
			armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			armLift.setPower(0.8);

			armLift.setTargetPosition(1200);
			armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			armLift.setPower(0.3); // slow it down a bit

			armExtend.setTargetPosition(900); // TODO: 900 IS A PLACEHOLDER VALUE! PLEASE GET THE VALUE FOR THE HIGH BAR LATER!
			elbow.setTargetPosition(72);
			wrist.setPosition(0.25); // lower wrist TODO: SAME WITH THIS
		}
				
	// ROTATE
	//	if (rotate.getPosition() < minRotateL && rotate.getPosition() > maxRotateL) {
	//		rotate.setPosition(rotate.getPosition() + gamepad2.right_stick_y);
	//		}
			
	// ROTATE LEFT
	//	if (gamepad2.right_stick_x && rotate.getPosition() > minRotate) {
	//		rotate.setPosition(-ROTATE_SPEED + rotate.getPosition());
	//		}
	//		
	// ROTATE RIGHT
	//	if (gamepad2.right_stick_x && rotate.getPosition() < maxRotate) {
	//		rotate.setPosition(ROTATE_SPEED + rotate.getPosition());
	//		}
// END Controler Configuration.	


/*
			// A [1] - Opens/closes claw
			if (gamepad1.a && !wasAPressed) {
				if (clawIsOpen) {
					claw.setPosition(minClaw); // Sets the claw to the closed position.
												// This way, we don't have to slide a value for the claw!
					clawIsOpen = false; // hate how these are the opposite ;_;
				}
				else {
					claw.setPosition(maxClaw); // Sets the claw to the open position.
					clawIsOpen = true;
				}
			}
			wasAPressed = gamepad1.a;

			// DPAD LEFT - extend arm
			if (gamepad1.dpad_left && armExtend.getCurrentPosition() < maxArm) {
				armExtend.setPower(armSpeed);
				// SOFTWARE LIMITER p1 - automatically lower the wrist
				if (armLift.getCurrentPosition() < shoulderHalfway) {
					wrist.setPosition(minWristL);
				}
			}
			else if (gamepad1.dpad_right && armExtend.getCurrentPosition() > minArm) { // DPAD RIGHT - retract arm
				armExtend.setPower(-armSpeed);
			}
			else {
				armExtend.setPower(0);
			}

			// DPAD UP - raise shoulder
			if (gamepad1.dpad_up && armLift.getCurrentPosition() < maxShoulder) {
				armLift.setPower(shoulderSpeed);
			}
			else if (gamepad1.dpad_down && armLift.getCurrentPosition() > minShoulder) { // DPAD DOWN - lower shoulder
				armLift.setPower(-shoulderSpeed);
			}
			else {
				armLift.setPower(0);
			}

			// *********** B - rotates wrist ******************
			if (!gamepad1.left_bumper && gamepad1.b && rotate.getPosition() < maxWristR) {
				rotate.setPosition(rotate.getPosition() + WRIST_ROTATE_SPEED);
			}
			else if (gamepad1.left_bumper && gamepad1.b && rotate.getPosition() > minWristR) {
				rotate.setPosition(rotate.getPosition() - WRIST_ROTATE_SPEED);
			}

			// *********** Y - lifts wrist ******************
			if (!gamepad1.left_bumper && gamepad1.y && wrist.getPosition() < maxWristL
						// SOFTWARE LIMITER p2 (wrist doesn't move when illegal)
					&& (armExtend.getCurrentPosition() < slideLimit || armLift.getCurrentPosition() > shoulderHalfway)) {
				wrist.setPosition(wrist.getPosition() + WRIST_LIFT_SPEED);
			}
			else if (gamepad1.left_bumper && gamepad1.y && wrist.getPosition() > minWristL) {
				wrist.setPosition(wrist.getPosition() - WRIST_LIFT_SPEED);
			}

			// *********** X - lifts elbow ******************
			if (!gamepad1.left_bumper && gamepad1.x && elbow.getCurrentPosition() < maxElbow) {
				elbow.setPower(ELBOW_SPEED);
			}
			else if (gamepad1.left_bumper && gamepad1.x && elbow.getCurrentPosition() > minElbow) {
				elbow.setPower(-ELBOW_SPEED);
			}
			else {
				elbow.setPower(0);
			}
			
			//*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*\\
			
			// Closes claw - RB [2]
			if (gamepad2.right_trigger > 0.5 && clawIsOpen) {
					claw.setPosition(minClaw); // Sets the claw to the closed position.
												// This way, we don't have to slide a value for the claw!
					clawIsOpen = false; // hate how these are the opposite ;_;
			}
			
			// Opens claw - LB [2]
			if (gamepad2.left_trigger > 0.5 && !clawIsOpen) {
					claw.setPosition(maxClaw); // Sets the claw to the open position.
												// This way, we don't have to slide a value for the claw!
					clawIsOpen = true; // hate how these are the opposite ;_;
			}
			
			// move wrist up/down - left stick y axis [2]
			
			if (wrist.getPosition() < minWristL && wrist.getPosition() > maxWristL) {
			wrist.setPosition(wrist.getPosition() + gamepad2.left_stick_y);
			}
			
			// moves elbow left - LB [2]
			
			if (gamepad2.left_bumper && elbow.getCurrentPosition() > 0) {
				elbow.setPower(ELBOW_SPEED);
			}
			
			if (gamepad2.right_bumper && elbow.getCurrentPosition() < maxElbow) {
				elbow.setPower(-ELBOW_SPEED);
			}
			
			// Y [2] - Opens/closes claw
			if (gamepad2.y && !wasAPressed) {
				if (clawIsOpen) {
					claw.setPosition(minClaw); // Sets the claw to the closed position.
												// This way, we don't have to slide a value for the claw!
					clawIsOpen = false; // hate how these are the opposite ;_;
				}
				else {
					claw.setPosition(maxClaw); // Sets the claw to the open position.
					clawIsOpen = true;
				}
			}
			
			// DPAD LEFT [2] - rotate to the left
			if (gamepad2.dpad_left && rotate.getPosition() > minRotate) {
				rotate.setPosition(-ROTATE_SPEED + rotate.getPosition());
			}
			
			// DPAD RIGHT [2] - rotate to the left
			if (gamepad2.dpad_right && rotate.getPosition() < maxRotate) {
				rotate.setPosition(ROTATE_SPEED + rotate.getPosition());
			}
			
			//*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*[2]*\\
*/			
			// Show the elapsed game time and wheel power.
			telemetry.addData("Status", "Run Time: " + runtime.toString());
			//telemetry.addData("Motors", "left (%.f0), right (%.f0)",
			//	(double)(leftDrive.getCurrentPosition()), (double)(rightDrive.getCurrentPosition()));
			/*telemetry.addData("Front Left Motor", frontLeft.getCurrentPosition());
			telemetry.addData("Front Right Motor", frontRight.getCurrentPosition());
			telemetry.addData("Back Left Motor", backLeft.getCurrentPosition());
			telemetry.addData("Back Right Motor", backRight.getCurrentPosition());
			telemetry.addData(" ", " ");*/
// Motors
			telemetry.addLine("=== Motor Positions ===");
			telemetry.addData("Arm lift", armLift.getCurrentPosition());
			telemetry.addData("Arm extension", armExtend.getCurrentPosition());
			telemetry.addData("Elbow position", elbow.getCurrentPosition());
			telemetry.addData(" ", " ");
// Servos
			telemetry.addLine("=== Servo Positions ===");
			telemetry.addData("Claw position", claw.getPosition());
			telemetry.addData("Rotation", rotate.getPosition());
			telemetry.addData("Wrist position", wrist.getPosition());
			telemetry.update();
		} 
		// End of opModeIsActive()


	} // End of runOpMode()

} // End of the OpMode class
