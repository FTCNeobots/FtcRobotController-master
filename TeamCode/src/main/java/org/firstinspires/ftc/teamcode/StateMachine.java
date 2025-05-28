package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
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
    private double swingSpeed = 1;
    private int runState = 0;
    private int loopState = 0;
    private int targetPos = 0;
    private int liftPosUp = 7300;
    private double rotationRatio = 6.4225;
    private int startBraking = (int)Math.round(0.5 * rotationRatio * countsPerRevolution);


    private int middlePosArm = -300;
    private int bottomPosArm = -1050;
    private long msForClaw = 400;


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
        //dit hoort niet te hoeven, maar zo werkt het?
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        swingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        swingMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


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
                    Swing(1, swingSpeed);
                    runState = 3;
                    break;
                case 3:
                    sleep(50);
                    Claw(true);
                    runState = 4;
                    break;
                case 4:
                    BrakeToStop(10, true);
                    break;
                //turn towards the basket
                case 10:
                    StartTurning(-90, turnSpeed);
                    runState = 11;
                    break;
                case 11:
                    BrakeToStop(20, true);
                    break;
                //drive towards basket
                case 20:
                    StartDrive(20, driveSpeed);
                    runState = 21;
                    break;
                case 21:
                    BrakeToStop(30, true);
                    break;
                //align with basket
                case 30:
                    StartTurning(-45, turnSpeed);
                    runState = 31;
                    break;
                case 31:
                    BrakeToStop(40, true);
                    break;
                //deliver in basket
                case 40:
                    //wait for the lift to go up
                    sleep(100);
                    StartDrive(45, 0.5);
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
                //sleep to prevent lift from tearing itself apart
                case 43:
                    sleep(500);
                    Lift(false, 1);
                    runState = 44;
                    break;
                case 44:
                    BrakeToStop(50, true);
                    break;
                //loop
                case 50:
                    switch(loopState){
                        //align with sample
                        case 0:
                            StartTurning(0, turnSpeed);
                            loopState = 1;
                            break;
                        case 1:
                            BrakeToStop(2, false);
                            break;
                        case 2:
                            StartDrive(0, driveSpeed);
                            loopState = 3;
                            break;
                        case 3:
                            BrakeToStop(4, false);
                            break;
                        case 4:
                            StartTurning(-45, turnSpeed);
                            loopState = 5;
                            break;
                        case 5:
                            BrakeToStop(10, false);
                            break;
                        //drive to the sample
                        case 10:
                            StartDrive(0, driveSpeed);
                            loopState = 11;
                            break;
                        case 11:
                            BrakeToStop(20,false);
                            break;
                        //pick up the sample
                        case 20:
                            swingSpeed = 0.5;
                            Swing(2, swingSpeed);
                            swingSpeed = 1;
                            loopState = 21;
                            break;
                        case 21:
                            //wait for the swing to finish
                            sleep(2000);
                            Claw(false);
                            loopState = 30;
                            break;
                        //put the arm up and move towards the basket
                        case 30:
                            sleep(msForClaw);
                            Swing(0, 0.25);
                            loopState = 31;
                            break;
                        case 31:
                            StartTurning(90, turnSpeed);
                            loopState = 32;
                            break;
                        case 32:
                            BrakeToStop(33, false);
                            break;
                        case 33:
                            StartDrive(5, driveSpeed);
                            loopState = 34;
                            break;
                        case 34:
                            Claw(true);
                            loopState = 35;
                            break;
                        case 35 :
                            BrakeToStop(40, false);
                            break;

                        // place the sample and move towards basket
                        case 40:
                            //wait for the arm to stop jumping
                            sleep(400);
                            StartTurning(-45, turnSpeed);
                            loopState = 41;
                            break;
                        case 41:
                            Claw(false);
                            loopState = 42;
                            break;
                        case 42:
                            BrakeToStop(43, false);
                            break;
                        case 43:



                    }



            }

            

        }




    }
    private void BrakeToStop(int nextStateValue_, boolean trueForRunState_){
        int delta_ = abs(leftFrontDrive.getTargetPosition() - leftFrontDrive.getCurrentPosition());

        while(delta_ > startBraking){
            delta_ = abs(leftFrontDrive.getTargetPosition() - leftFrontDrive.getCurrentPosition());

        }
        leftFrontDrive.setPower(brakeSpeed);
        rightFrontDrive.setPower(brakeSpeed);
        leftBackDrive.setPower(brakeSpeed);
        rightBackDrive.setPower(brakeSpeed);
        if(!leftFrontDrive.isBusy()){
                leftFrontDrive.setPower(0);
                leftBackDrive.setPower(0);
                rightFrontDrive.setPower(0);
                rightBackDrive.setPower(0);
                if(trueForRunState_){
                    runState = nextStateValue_;
                }else{
                    loopState = nextStateValue_;
                }

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
    //make this brake somehow
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

    private void Claw(boolean trueForOpen){
        if (trueForOpen) {
            clawServo.setPosition(0.37);
        }else{
            clawServo.setPosition(0.06);
        }

    }



}
