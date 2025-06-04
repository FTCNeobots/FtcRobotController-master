package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoClawTest")
public class ServoClawTest extends LinearOpMode {

    double pos = 0;
    private Servo clawServo;
    @Override
    public void runOpMode() throws InterruptedException {
        clawServo = hardwareMap.get(Servo.class, "claw");
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                clawServo.setPosition(pos);
            }
            if (gamepad1.left_bumper) {
                pos -= 0.05;
                sleep(500);
            }
            if (gamepad1.right_bumper) {
                pos += 0.05;
                sleep(500);
            }
            telemetry.addData("Current position if applied: ", pos);
            telemetry.update();
        }

    }
}
