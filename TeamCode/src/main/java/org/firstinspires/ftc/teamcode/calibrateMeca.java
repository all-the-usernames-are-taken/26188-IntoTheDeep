package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.onbotjava.OnBotJavaManager.initialize;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous
public class calibrateMeca extends BaseOpMode {

    boolean anyMotorRunning;

    boolean netSide = false;

    boolean redAlliance = false;

    public int turnDirection = +1;

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
            telemetry.addLine ("Set netside using Y or A");
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

        // Auto start
        // Calibration tests!
        driveForward(48);
        tele("driveForward 48 inches",5000);

        strafeLeft(48);
        tele("strafeLeft 48 inches",5000);

        turnRight(360);
        tele("turnRight 360 degrees", 5000);
        // todo: after everything is calibrated, test diagonals
    }

    public void tele(String message, int milliseconds) {
        telemetry.addLine(message);
        telemetry.update();
        sleep(milliseconds);
    }

}
