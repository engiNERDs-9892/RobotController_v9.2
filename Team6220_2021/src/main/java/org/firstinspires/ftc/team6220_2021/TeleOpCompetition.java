package org.firstinspires.ftc.team6220_2021;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp Competition", group = "Competition")
public abstract class TeleOpCompetition extends MasterTeleOp {

    @Override
    public void runOpMode() {
        Initialize();
        resetArmAndServo();
        waitForStart();

        while (opModeIsActive()) {
            driver1.update();
            driver2.update();

            driveRobot();
            driveSlow();
            driveLeftCarousel();
            driveRightCarousel();
            driveGrabber();
            driveArm();
            driveArmManual();
            driveBelt();
            resetArmAndServo();
        }
    }
}