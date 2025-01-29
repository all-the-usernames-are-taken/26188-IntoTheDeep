package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.onbotjava.OnBotJavaManager.initialize;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BaseOpMode;
@Autonomous
public class MecaMecaAuto extends BaseOpMode {

    // VARIABLES EXCLUSIVE TO AUTONOMOUS
    boolean anyMotorRunning;

    boolean netSide = false;

    boolean redAlliance = false;

    public int turnDirection = +1;

    public int headstart = 0; // in seconds

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
            if (gamepad1.b) redAlliance=true;
            else if (gamepad1.x) redAlliance=false;
            if (gamepad1.y) netSide=true;
            else if (gamepad1.a) netSide=false;

            if (gamepad1.dpad_up && !wasDPADpressed) {
                headstart += 1;
            }
            else if (gamepad1.dpad_down && !wasDPADpressed && headstart > 0) {
                headstart -= 1;
            }
            if (gamepad1.dpad_left && !wasDPADpressed) {
                optionSelected = (optionSelected + 1) % optionCount;
            }
            else if (gamepad1.dpad_right && !wasDPADpressed) {
                optionSelected = (optionSelected - 1) % optionCount;
            }
            wasDPADpressed = gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right;

            telemetry.addLine ("Match setup!");
            telemetry.addLine ("Set alliance using B or X");
            telemetry.addLine ("Set net side using Y or A");
            telemetry.addLine("Set strat by using DPAD LEFT and RIGHT");
            telemetry.addLine("Set delay by using DPAD UP and DOWN");
            telemetry.addLine ("Press right bumper to exit setup");
            //Signal that we're ready
            telemetry.addLine ("Here goes nothing...");
            telemetry.addLine ("");

            if (redAlliance)
            {
                telemetry.addData("Alliance", "Red");
            }

            else
            {
                telemetry.addData
                        ("Alliance", "Blue");
            }


            if (netSide)
            {
                telemetry.addData
                        ("Side", "Net");
            }
            else
            {
                telemetry.addData
                        ("Side", "Observer pit");
            }
            telemetry.addData("Headstart: ", headstart);
            telemetry.addData("Strat: ", optionNames[optionSelected]);



            telemetry.addLine("");
            telemetry.addLine("DO NOT PRESS START!");
            telemetry.update ();
        }

        //Signal that we're ready
        telemetry.addLine ("Auto ready!");
        telemetry.update();

        if (netSide) turnDirection = -1;

        waitForStart();
        sleep(headstart * 1000);

        // Checks if the drive train is moving at all. This allows us to run 2 things at once!**
        // ** hypothetically.
        boolean anyMotorRunning = frontLeft.isBusy() || frontRight.isBusy() ||
                                    backLeft.isBusy() || backRight.isBusy();

        if (optionSelected == 1) { // Calibration was option 1
            // Auto start
            // Calibration tests!
            tele("start calibration!", 0);
            driveForward(48);
            tele("driveForward 48 inches",5000);

            strafeLeft(48);
            tele("strafeLeft 48 inches",5000);

            turnRight(360);
            tele("turnRight 360 degrees", 5000);
        }
        else if (optionSelected == 0) { // "Default" was option 0

        }

    }
    public void tele(String message, int milliseconds) {
        telemetry.addLine(message);
        telemetry.update();
        sleep(milliseconds);
    }

} // End of class
