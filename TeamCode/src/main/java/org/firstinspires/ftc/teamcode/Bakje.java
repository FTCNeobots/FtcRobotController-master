package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "Bakje")
@Disabled
public class Bakje extends LinearOpMode {


    private DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {
        motor = hardwareMap.dcMotor.get("test");
        double speed = 0.2;
        waitForStart();

        while (opModeIsActive()){
            if(gamepad1.a){
                motor.setPower(speed);
            }else if(gamepad1.b){
                motor.setPower(-speed);
            }else{
                motor.setPower(0);
            }
            if(gamepad1.left_bumper){
                speed = 0.5;
            }
            if(gamepad1.right_bumper){
                speed =1;
            }

        }

    }
}
