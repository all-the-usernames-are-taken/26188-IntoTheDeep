package org.firstinspires.ftc.mcqueencode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

//***********************************
@Autonomous
//***********************************

public class Auto_McQueen extends BaseOpMode {

	// VARIABLES EXCLUSIVE TO AUTONOMOUS
	boolean anyMotorRunning;
	boolean netSide = false;
//	boolean redAlliance = false;  *Remove not required
	public int turnDirection = +1;
	public int startDelay = 0; 
	public boolean wasDPADpressed = false;
	int optionSelected = 0;
	int optionCount = 2;
	String[] optionNames = new String[] {"Default", "Calibration"};
	
	// VARIABLES EXCLUSIVE TO AUTONOMOUS

	@Override
	public void runOpMode(){
		initialize();
// Do anything else that's autonomous-specific here
		telemetry.addData("Status", "Initialized");
		telemetry.update();

//User interface for start of match
		while(!isStopRequested() && !gamepad1.right_bumper)
		{
//			if (gamepad1.b) redAlliance=true;   *Removed variable above
//			else if (gamepad1.x) redAlliance=false;
			if (gamepad1.y) netSide=true;
			else if (gamepad1.a) netSide=false;
			
//Start Define startDelay
			if (gamepad1.dpad_up && !wasDPADpressed) {
				startDelay += 1;
			}
			else if (gamepad1.dpad_down && !wasDPADpressed && startDelay > 0) {
				startDelay -= 1;
			}
//End Define startDelay	

//Start Define normal or calibration mode 
			if (gamepad1.dpad_left && !wasDPADpressed) {
				optionSelected = (optionSelected + 1) % optionCount;
			}
			else if (gamepad1.dpad_right && !wasDPADpressed) {
				optionSelected = (optionSelected + optionCount - 1) % optionCount;
			}
//Start Define normal or calibration mode 

			wasDPADpressed = gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right;

//Add text to Driver Hub
			telemetry.addLine ("Match setup!");
//			telemetry.addLine ("Set alliance using B or X");
			telemetry.addLine ("Set net side using Y or A");
			telemetry.addLine("Set strat by using DPAD LEFT and RIGHT");
			telemetry.addLine("Set start delay by using DPAD UP and DOWN");
			telemetry.addLine ("Press right bumper to exit setup");
//Signal that we're ready
			telemetry.addLine ("TIME TO ROCK!!");
			telemetry.addLine ("");

//Remove this code as it does not matter if we are red or blue.
//			if (redAlliance)
//			{
//				telemetry.addData("Alliance", "Red");
//			}
//
//			else
//			{
//				telemetry.addData
//						("Alliance", "Blue");
//			}


			if (netSide)
			{
				telemetry.addData
						("Side", "Net");
			}
			else
			{
				telemetry.addData
						("Side", "Observation Zone");
			}
			telemetry.addData("startDelay: ", startDelay);
			telemetry.addData("Mode: ", optionNames[optionSelected]);



			telemetry.addLine("");
			telemetry.addLine("DO NOT PRESS START!");
			telemetry.update ();
		}

//Signal that we're ready
		telemetry.addLine ("Auto ready!");
		telemetry.update();

		if (netSide) turnDirection = -1;

		waitForStart();
		sleep(startDelay * 1000);

// This allows us to run 2 things at once
		boolean anyMotorRunning = frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy();
//		rotate.setPosition(0);
//		wrist.setPosition(wristDefault);

//*************************************************************
//CALIBRATION CODE - ROBOT WILL MOVE
//*************************************************************
		if (optionSelected == 1) { // Calibration was option 1
/* Auto start
 Calibration tests!
			tele("start calibration!", 0);
			driveForward(48);
			tele("driveForward 48 inches",5000);

			strafeLeft(48);
			tele("strafeLeft 48 inches",5000);

			turnRight(360);
			tele("turnRight 360 degrees", 5000);
			telemetry.addData("claw", claw);
			telemetry.update ();
*/
		}
		else if (optionSelected == 0) { // "Default" was option 0
//*************************************************************
//AUTONOMOUS MODE CODE HERE
//*************************************************************

//*NOTE - Driving code and arm code is all seperated by a enter space
// todo: non-blocking first, THEN functions

	tele("driving forward", 2000);
	driveForward(-10, 0.5);
	tele("strafe left", 2000);
	strafeLeft(-5, 1);
	
	tele("moving armLift", 0);
	armLift.setTargetPosition(1075);					//Arm lifts while driving
	armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	armLift.setPower(0.4);
	
	strafeLeft(-5, 1);
	
	
//	elbow.setTargetPosition(60);						//Prepare to clip
//	elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//	elbow.setPower(0.5);
	
//	wrist.setPosition(wristDefault);					//Clip Specimen
//	rotate.setPosition(20);
	
	sleep(500);
	
	tele("driving diagonally to sub", 2000);
	driveForwardDiagonalLeft(-42, 0.5);		
	tele("driving diagonally away from sub", 2000);
	driveForwardDiagonalLeft(15, 0.5);					//Back away for sub
	/*
	armLift.setTargetPosition(50);						//Arm lowers while driving
	armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	armLift.setPower(0.25);
	
	turnRight(-8, 0.5);									//Center bot
	strafeLeft(29, 0.5);								//Go straight towards samples
	driveForward(-34, 0.5);								//Go past sample #1
	strafeLeft(20, 0.5);								//Get behind sample #1
	driveForward(45, 0.5);								//Go to observation zone
	driveForward(-45, 0.5);								//Go back for sample #2
	strafeLeft(16, 0.5);								//Get behind sample #2
	driveForward(38, 0.5);								//Go to obervation zone
	*/
//	elbow.setTargetPosition(60);						//Ready to pick specimen off wall
//	elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//	elbow.setPower(0.5);
	
//	wrist.setPosition(wristDefault);					//Ready to pick specimen off wall
//	rotate.setPosition(40);
	
//*Open and close of claw needed
	
	sleep(1500);
	
	strafeLeft(-35, 0.5);								//Get ready to pick specimen off wall
	driveForward(4, 0.5);								//Line up for specimen
	
//	driveForwardDiagonalLeft(-42, 0.5);					//Go to sub
//	driveForwardDiagonalLeft(42, 0.5);					//Back to pick up specimen off wall
	
	
	
//DELETE code commented below	
	
/* Drive to sub to clip
		int frTarget = (int)(-45 * COUNTS_PER_INCH);	//Verify ticks
		int blTarget = (int)(-45 * COUNTS_PER_INCH);
		frontRight.setTargetPosition(frTarget);
		backLeft.setTargetPosition(blTarget);
		frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		frontRight.setPower(0.5);
	  	backLeft.setPower(0.5);
*/
/*
//Arm lifts while driving
 		armLift.setTargetPosition(1205);
		armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		armLift.setPower(0.5);
		
//Wrist flips up while driving
		wrist.setPosition(wristDefault);
		rotate.setPosition(0);
		sleep(1000);
		
//Elbow extends ****NOT FINAL
		elbow.setTargetPosition(30);
		elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		elbow.setPower(0.1);
		
//Another wrist comamand
		
		while (opModeIsActive() && (backLeft.isBusy() || frontRight.isBusy())) {
		telemetry.addData("FR Position", frontRight.getCurrentPosition());
		telemetry.addData("BL Position", backLeft.getCurrentPosition());
		telemetry.update();
}
		frontRight.setPower(0);
		backLeft.setPower(0);
		
//Drive back
		frontRight.setTargetPosition(22.5);
		backLeft.setTargetPosition(22.5);
		frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		frontRight.setPower(-0.5);
		backLeft.setPower(-0.5);
		

		
		
		
*/
}

}

	public void tele(String message, int milliseconds) {
		telemetry.addLine(message);
		telemetry.update();
		sleep(milliseconds);
	}
} // End of class

