package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "TestTegenZelfmoord")
public class TestTegenZelfmoord extends LinearOpMode {
    private DcMotor leftFrontDrive;
    private DcMotor rightFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightBackDrive;

    private DcMotor swingMotor;
    private DcMotor liftMotor;
    private Servo clawServo;

    private double maxSpeed = 0.5;
    private double botHeading;
    private double turnSpeed = 1;

    private double swingSpeed = 0.75;
    private double liftSpeed = 1;


    @Override
    public void runOpMode() throws InterruptedException {
        leftBackDrive = hardwareMap.dcMotor.get("LBD");
        leftFrontDrive = hardwareMap.dcMotor.get("LFD");
        rightBackDrive = hardwareMap.dcMotor.get("RBD");
        rightFrontDrive = hardwareMap.dcMotor.get("RFD");

        liftMotor = hardwareMap.dcMotor.get("lift");
        swingMotor = hardwareMap.dcMotor.get("swing");
        clawServo = hardwareMap.get(Servo.class, "claw");

        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while(opModeIsActive()){

            if(gamepad1.left_bumper){
                leftBackDrive.setPower(1);
            }
            if(gamepad1.left_trigger > 0){
                leftFrontDrive.setPower(1);
            }
            if(gamepad1.right_bumper){
                rightBackDrive.setPower(1);
            }
            if(gamepad1.right_trigger > 0){
                rightFrontDrive.setPower(1);
            }
            stopMoving();
        }
    }
    private void stopMoving(){

        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
    }
}
