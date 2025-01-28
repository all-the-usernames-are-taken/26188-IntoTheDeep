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

            telemetry.addLine ("Match setup!");
            telemetry.addLine ("Set alliance using B or X");
            telemetry.addLine ("Set net side using Y or A");
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

            telemetry.addLine("");
            telemetry.addLine("DO NOT PRESS START!");
            telemetry.update ();
        }

        //Signal that we're ready
        telemetry.addLine ("Auto ready!");
        telemetry.update();

        if (netSide) turnDirection = -1;

        waitForStart();

        // Checks if the drive train is moving at all. This allows us to run 2 things at once!**
        // ** hypothetically.
        boolean anyMotorRunning = frontLeft.isBusy() || frontRight.isBusy() ||
                                    backLeft.isBusy() || backRight.isBusy();

        // Auto start - Concept
        // STRAFE AND DRIVE TEST!
        driveForward(12);
        driveForward(-12);
        strafeLeft(8);
        strafeLeft(-8);
        driveForwardDiagonalLeft(4);
        driveForwardDiagonalLeft(-4);
        // TURN TEST!
        turnRight(360);
        turnRight(-360);
        // Moving arm and drivetrain at the same time
        driveForward(4);
    }


} // End of class
