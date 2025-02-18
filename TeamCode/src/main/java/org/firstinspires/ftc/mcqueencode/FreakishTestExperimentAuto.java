package org.firstinspires.ftc.mcqueencode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

//***********************************
@Autonomous
//***********************************

public class FreakishTestExperimentAuto extends BaseOpMode {

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
		tele("Going to position", 0);
		armLift.setTargetPosition(0); // reset elbow/wrist/armLift to 0
		elbow.setTargetPosition(0);
		armLift.setTargetPosition(0);
		claw.setPosition(minClaw); // open claw in case
		tele("extending armExtend",0);
		armExtend.setTargetPosition(1121 - armExtend.getCurrentPosition());


}

}

	public void tele(String message, int milliseconds) {
		telemetry.addLine(message);
		telemetry.update();
		sleep(milliseconds);
	}
} // End of class

