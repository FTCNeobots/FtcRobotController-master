package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.ConfigSubSonic.ConfigAuto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "SwingEncoderPosTest")
public class SwingEncoderPosTest extends LinearOpMode {
    private DcMotor leftFrontDrive;
    private DcMotor rightFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightBackDrive;

    private DcMotor swingMotor;
    private DcMotor liftMotor;
    private Servo clawServo;
    private int middlePosArm;
    private int bottomPosArm;
    private int testSpeed;
    @Override
    public void runOpMode() throws InterruptedException {
        ConfigAuto(leftBackDrive, leftFrontDrive, rightBackDrive, rightFrontDrive, liftMotor, swingMotor, clawServo);



        middlePosArm = 10;
        bottomPosArm = 20;
        testSpeed = 1;

        waitForStart();
        while(opModeIsActive()){

            if(gamepad1.a){
                Swing(0, testSpeed);
            }
            if(gamepad1.b){
                Swing(1, testSpeed);
            }
            if(gamepad1.y){
                Swing(2, testSpeed);
            }
        }









    }
    private void Swing(int upMiddleDown, double speed) {

        if (upMiddleDown == 0) {
            swingMotor.setTargetPosition(0);
        } else if(upMiddleDown == 1){
            swingMotor.setTargetPosition(middlePosArm);
        }else {
            swingMotor.setTargetPosition(bottomPosArm);
        }
        swingMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        swingMotor.setPower(speed);
    }
}
