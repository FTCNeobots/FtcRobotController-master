package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoClawTest")
public class ServoClawTest extends LinearOpMode {

    double pos = 0;
    double pos2 = 0;
    private Servo clawServo;
    private Servo rotateServo;
    @Override
    public void runOpMode() throws InterruptedException {
        clawServo = hardwareMap.get(Servo.class, "claw");
        rotateServo = hardwareMap.get(Servo.class, "rotate");
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                clawServo.setPosition(pos);
            }
            if (gamepad1.left_bumper) {
                pos -= 0.05;
                sleep(50);
            }
            if (gamepad1.right_bumper) {
                pos += 0.05;
                sleep(50);
            }
            if (gamepad1.b) {
                rotateServo.setPosition(pos2);
            }
            if (gamepad1.left_trigger > 0) {
                pos2 -= 0.05;
                sleep(50);
            }
            if (gamepad1.right_trigger > 0) {
                pos2 += 0.05;
                sleep(50);
            }

            telemetry.addData("Current claw position if applied: ", pos);
            telemetry.addData("Current rotate position if applied: ", pos2);
            telemetry.update();
        }

    }
}
