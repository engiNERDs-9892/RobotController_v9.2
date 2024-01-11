package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name = "Tele", group = "Pushbot")
public class tele extends OpMode {
    Hardware robot = new Hardware();
    double speedLimit;
    double y;
    double x;
    double rx;

    double denominator;
    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;

    int deploymentState = 0;
    double oldTime = 0;

    int pixlePickerColorTime = 5000;

    Gamepad.LedEffect gamepadLaunchSequenceLed = new Gamepad.LedEffect.Builder()
            .addStep(100, 0, 0, 2000)
            .addStep(0, 100, 0, 200)
            .addStep(100, 0, 0, 200)
            .addStep(0, 100, 0, 200)
            .addStep(100, 0, 0, 200)
            .addStep(0, 100, 0, 200)
            .addStep(100, 0, 0, 200)
            .addStep(0, 100, 0, 200)
            .addStep(100, 0, 0, 200)
            .addStep(0, 100, 0, 200)
            .addStep(100, 0, 0, 200)
            .addStep(0, 100, 0, 200)
            .addStep(100, 0, 0, 200)
            .addStep(0, 100, 0, 200)
            .addStep(100, 0, 0, 200)
            .addStep(100, 0, 100, 5000)
            .build();
    Gamepad.RumbleEffect gamepadLaunchSequenceRumble = new Gamepad.RumbleEffect.Builder()
            .addStep(.3, .3, 2000)
            .addStep(.5, 0, 200)
            .addStep(0, .5, 200)
            .addStep(.5, 0, 200)
            .addStep(0, .5, 200)
            .addStep(.5, 0, 200)
            .addStep(0, .5, 200)
            .addStep(.5, 0, 200)
            .addStep(0, .5, 200)
            .addStep(.5, 0, 200)
            .addStep(0, .5, 200)
            .addStep(.5, 0, 200)
            .addStep(0, .5, 200)
            .addStep(.5, 0, 200)
            .addStep(0, .5, 200)
            .build();


    public void init() {

        telemetry.addData("Robot:", "Initializing");
        telemetry.update();

        robot.init(hardwareMap);

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Robot:", "Ready");
        telemetry.update();

    }

    public void start() {

    }

    public void loop() {

        if (gamepad1.right_trigger > .5) {
            speedLimit = 50;
        } else {
            speedLimit = 100;
        }

        double speedLimitValue = speedLimit / 100;

        y = -gamepad1.left_stick_y; // Remember, this is reversed!
        x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        rx = gamepad1.right_stick_x;

        denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        frontLeftPower = (y + x + rx) / denominator;
        frontRightPower = (y - x - rx) / denominator;
        backLeftPower = (y - x + rx) / denominator;
        backRightPower = (y + x - rx) / denominator;

        if ((Math.abs(gamepad1.right_stick_x) > 0.1) || (Math.abs(gamepad1.right_stick_y) > 0.1) || (Math.abs(gamepad1.left_stick_x) > 0.1) || (Math.abs(gamepad1.left_stick_y) > 0.1)) {
            robot.leftDrive.setPower(frontLeftPower * speedLimitValue);
            robot.leftBackDrive.setPower(backLeftPower * speedLimitValue);
            robot.rightDrive.setPower(frontRightPower * speedLimitValue);
            robot.rightBackDrive.setPower(backRightPower * speedLimitValue);
        } else {
            robot.leftDrive.setPower(0);
            robot.leftBackDrive.setPower(0);
            robot.rightDrive.setPower(0);
            robot.rightBackDrive.setPower(0);
        }

        robot.winch.setPower(-gamepad2.right_stick_y);
        robot.lift.setPower(-gamepad2.left_stick_y);

        if (gamepad2.dpad_up) {
        } else if (gamepad2.dpad_down) {
        }

        if (gamepad2.y) {
        } else if (gamepad2.a) {

        }

        if (gamepad2.x && gamepad2.b) {

        } else {

        }

        /** Stripper State **/

        switch (deploymentState) {
            case 0:
                deploymentState++;
                break;
            case 1:
                robot.stripper.setPosition(robot.stripperOpen);
                if (getRuntime() > oldTime + .2) {
                    if (gamepad2.right_bumper) {
                        deploymentState++;
                        oldTime = getRuntime();
                    } else if (gamepad2.left_bumper) {
                        deploymentState = 1;
                        oldTime = getRuntime();
                    }
                }
                break;
            case 2:
                robot.stripper.setPosition(robot.stripperFirstRelease);
                if (getRuntime() > oldTime + .2) {
                    if (gamepad2.right_bumper) {
                        deploymentState++;
                        oldTime = getRuntime();
                    } else if (gamepad2.left_bumper) {
                        deploymentState = 1;
                        oldTime = getRuntime();
                    }
                }
                break;
            case 3:
                robot.stripper.setPosition(robot.stripperSecondRelease);
                if (getRuntime() > oldTime + .2) {
                    if (gamepad2.right_bumper) {
                        deploymentState++;
                        oldTime = getRuntime();
                    } else if (gamepad2.left_bumper) {
                        deploymentState = 1;
                        oldTime = getRuntime();
                    }
                }
                break;
            case 4:
                deploymentState = 0;
                break;
        }


        /** Intake/Transfer **/

        switch (escapmentFingerPosition) {
            case 0:
                break;
            case 1:
                robot.escapementFinger.setPosition(-.1);
        }


        robot.intake.setPower(gamepad2.right_trigger);
        robot.transfer.setPower(gamepad2.right_trigger);
        robot.intake.setPower(-gamepad2.left_trigger);
        robot.transfer.setPower(-gamepad2.left_trigger);

        if (gamepad1.x) {
            gamepad1.setLedColor(255, 0, 255, pixlePickerColorTime);
            gamepad2.setLedColor(255, 0, 255, pixlePickerColorTime);
        }

        if (gamepad1.y) {
            gamepad1.setLedColor(0, 255, 0, pixlePickerColorTime);
            gamepad2.setLedColor(0, 255, 0, pixlePickerColorTime);
        }

        if (gamepad1.b) {
            gamepad1.setLedColor(249, 100, 0, pixlePickerColorTime);
            gamepad2.setLedColor(249, 100, 0, pixlePickerColorTime);
        }

        if (gamepad1.a) {
            gamepad1.setLedColor(255, 255, 255, pixlePickerColorTime);
            gamepad2.setLedColor(255, 255, 255, pixlePickerColorTime);
        }

        if (gamepad2.right_bumper && gamepad1.left_bumper) {
            gamepad1.runLedEffect(gamepadLaunchSequenceLed);
            gamepad2.runLedEffect(gamepadLaunchSequenceLed);
            gamepad1.runRumbleEffect(gamepadLaunchSequenceRumble);
            gamepad2.runRumbleEffect(gamepadLaunchSequenceRumble);
        }
        telemetry.addData("Stripper State", deploymentState);
        telemetry.addData("BB1", robot.firstPixelDetector.isPressed());
        telemetry.addData("BB2", robot.firstPixelDetector.isPressed());
        telemetry.update();

    }

}