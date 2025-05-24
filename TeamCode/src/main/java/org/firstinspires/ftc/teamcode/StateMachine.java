package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.ConfigSubSonic.ConfigAuto;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name = "StateMachine")
public class StateMachine extends LinearOpMode {
    private DcMotor leftFrontDrive;
    private DcMotor rightFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightBackDrive;

    private DcMotor swingMotor;
    private DcMotor liftMotor;
    private Servo clawServo;

    double countsPerRevolution = 28 * 15;
    double cmPerRevolution = 31;
    double ticksPerCm = countsPerRevolution / cmPerRevolution;

    private double turnSpeed = 0.5;
    private double driveSpeed = 0.5;
    private double brakeSpeed = 0.25;
    private int runState = 0;
    private int loopState = 0;
    private int targetPos = 0;
    private int liftPosUp = 7300;
    private double rotationRatio = 6.25;
    private int startBraking = 210;


    @Override
    public void runOpMode() throws InterruptedException {
        ConfigAuto(leftBackDrive, leftFrontDrive, rightBackDrive, rightFrontDrive, liftMotor, swingMotor, clawServo);


        waitForStart();
        while(opModeIsActive()){
            switch (runState){

                //driving away from wall, ready to turn + move lift up
                case 0:
                    StartDrive(30, driveSpeed);
                    runState = 1;
                    break;
                case 1:
                    Lift(true, 1);
                    runState = 2;
                    break;
                case 2:
                    BrakeToStop(10);
                    break;
                //turn towards the basket
                case 10:
                    StartTurning(90, turnSpeed);
                    runState = 11;
                    break;
                case 11:
                    BrakeToStop(20);
                    break;
                //drive towards basket
                case 20:
                    StartDrive(200, driveSpeed);
                    runState = 21;
                    break;
                case 21:
                    BrakeToStop(30);
                    break;
                //align with basket
                case 30:
                    StartTurning(45, turnSpeed);
                    runState = 31;
                    break;
                case 31:
                    BrakeToStop(40);
                    break;
                //deliver in basket
                case 40:
                    StartDrive(45, 1);
                    runState = 41;
                    break;
                case 41:
                    if(!leftFrontDrive.isBusy()) {
                        StopMoving();
                        runState = 42;
                    }
                    break;
                case 42:
                    StartDrive(-45, driveSpeed);
                    runState = 43;
                    break;
                case 43:
                    BrakeToStop(50);
                    break;
                //temporary lift back down
                case 50:
                    Lift(false, 1);
                    runState = 60;
                    break;
                //loop
                case 60:
                    switch(loopState){
                        case 0:





                    }



            }

            

        }




    }
    private void BrakeToStop(int nextStateValue_){
        int delta_ = abs(leftFrontDrive.getTargetPosition() - leftFrontDrive.getCurrentPosition());


        if(delta_ <= startBraking){
            leftFrontDrive.setPower(brakeSpeed);
            rightFrontDrive.setPower(brakeSpeed);
            leftBackDrive.setPower(brakeSpeed);
            rightBackDrive.setPower(brakeSpeed);
        }
        if(!leftFrontDrive.isBusy()){
            leftFrontDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightFrontDrive.setPower(0);
            rightBackDrive.setPower(0);

            runState = nextStateValue_;
        }

    }





    private void StartDrive(double cm_, double speed_){
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        targetPos = (int) (cm_ * ticksPerCm);

        leftFrontDrive.setTargetPosition(targetPos);
        leftBackDrive.setTargetPosition(targetPos);
        rightFrontDrive.setTargetPosition(targetPos);
        rightBackDrive.setTargetPosition(targetPos);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed_);
        leftBackDrive.setPower(speed_);
        rightFrontDrive.setPower(speed_);
        rightBackDrive.setPower(speed_);

    }
    private void StartTurning(double degrees_, double speed_) {
        int newTarget;

        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        newTarget = leftFrontDrive.getCurrentPosition() + (int) (degrees_ / 360 * countsPerRevolution * rotationRatio);
        leftFrontDrive.setTargetPosition(-newTarget);
            leftBackDrive.setTargetPosition(-newTarget);
            rightFrontDrive.setTargetPosition(newTarget);
            rightBackDrive.setTargetPosition(newTarget);

            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftFrontDrive.setPower(speed_);
            rightFrontDrive.setPower(speed_);
            leftBackDrive.setPower(speed_);
            rightBackDrive.setPower(speed_);


    }

    private void StopMoving(){
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    private void Lift(boolean trueForUp, double speed) {

        if (trueForUp) {
            liftMotor.setTargetPosition(liftPosUp);
        } else {
            liftMotor.setTargetPosition(0);
        }
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(speed);
    }



}
