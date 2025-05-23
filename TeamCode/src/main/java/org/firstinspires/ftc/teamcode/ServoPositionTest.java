package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoPositionTest")
@Disabled
public class ServoPositionTest extends LinearOpMode {

    private Servo clawServo;
    public double position = 0;
    @Override

    public void runOpMode() throws InterruptedException {
        clawServo = hardwareMap.get(Servo.class, "claw");

        waitForStart();

        while(opModeIsActive()){
            //open
            if(gamepad2.dpad_up){
                clawServo.setPosition(0.1);
            }
            //close
            if(gamepad2.dpad_down){
                clawServo.setPosition(0.45);
            }


        }

    }
}
