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
    private Servo rotateServo;

    double countsPerRevolution = 28 * 15;
    double cmPerRevolution = 31;
    double ticksPerCm = countsPerRevolution / cmPerRevolution;

    private double turnSpeed = 0.5;
    private double driveSpeed = 0.5;
    private double brakeSpeed = 0.35;
    private double swingSpeed = 1;
    private int runState = 0;
    private int loopState = 0;
    private int targetPos = 0;
    private int liftPosUp = 3950;
    private double rotationRatio = 6.44;
    private int startBraking = (int)Math.round(0.3 * rotationRatio * countsPerRevolution);


    private int middlePosArm = -300;
    private int bottomPosArm = -1100;
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
        rotateServo = hardwareMap.get(Servo.class, "rotate");

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
                    rotateServo.setPosition(0.5);
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
                    sleep(50);
                    StartTurning(-90, turnSpeed);
                    runState = 11;
                    break;
                case 11:
                    BrakeToStop(20, true);
                    break;
                //drive towards basket
                case 20:
                    sleep(50);
                    StartDrive(25, driveSpeed);
                    runState = 21;
                    break;
                case 21:
                    BrakeToStop(30, true);
                    break;
                //align with basket
                case 30:
                    sleep(50);
                    StartTurning(-40, turnSpeed);
                    runState = 31;
                    break;
                case 31:
                    BrakeToStop(40, true);
                    break;
                //deliver in basket
                case 40:
                    //wait for the lift to go up
                    sleep(100);
                    StartDrive(40, 0.4);
                    runState = 41;
                    break;
                case 41:
                    if(!leftFrontDrive.isBusy()) {
                        StopMoving();
                        runState = 42;
                    }
                    break;
                case 42:
                    sleep(50);
                    StartDrive(-25, driveSpeed);
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
                    while(loopState < 60) {
                        switch (loopState) {
                            //turn to be in line with sample
                            case 0:
                                sleep(50);
                                StartTurning(-45, turnSpeed);
                                loopState = 1;
                                break;
                            case 1:
                                Swing(1, swingSpeed);
                                Claw(true);
                                rotateServo.setPosition(0.5);
                                loopState = 2;
                                break;
                            case 2:
                                BrakeToStop(10, false);
                                break;
                            //drive towards sample and pick it up
                            case 10:
                                sleep(50);
                                StartDrive(-8, driveSpeed);
                                loopState = 11;
                                break;
                            case 11:
                                BrakeToStop(12, false);
                                break;
                            case 12:
                                Swing(2, swingSpeed);
                                loopState = 13;
                                break;
                            case 13:
                                sleep(1000);
                                Claw(false);
                                loopState = 20;
                                break;
                            //drive back and align with basket
                            case 20:
                                sleep(msForClaw);
                                rotateServo.setPosition(0.67);
                                Swing(0, 0.4);
                                loopState = 21;
                                break;
                            case 21:
                                StartDrive(5, driveSpeed);
                                loopState = 22;
                                break;
                            case 22:
                                BrakeToStop(23, false);
                                break;
                            case 23:
                                StartTurning(45, turnSpeed);
                                loopState = 24;
                                break;
                            case 24:
                                BrakeToStop(30, false);
                                break;
                            //deposit the second sample in the lift
                            case 30:
                                sleep(50);
                                Claw(true);
                                sleep(msForClaw);
                                Claw(false);
                                loopState = 31;
                                break;
                            case 31:
                                sleep(msForClaw);
                                Swing(1, swingSpeed);
                                rotateServo.setPosition(0.5);
                                loopState = 40;
                                break;
                            case 40:
                                sleep(300 );
                                Lift(true, 1);
                                loopState = 41;
                                break;
                            //deliver the second sample
                            case 41:
                                //wait for the lift to go up
                                sleep(2500);
                                StartDrive(30, driveSpeed);
                                Claw(true);
                                loopState = 42;
                                break;
                            case 42:
                                if (!leftFrontDrive.isBusy()) {
                                    StopMoving();
                                    loopState = 50;
                                }
                                break;
                            //back away from the basket and move the lift back down
                            case 50:
                                sleep(50);
                                StartDrive(-30, driveSpeed);
                                loopState = 51;
                                break;
                            //sleep to prevent lift from tearing itself apart
                            case 51:
                                sleep(1500);
                                Lift(false, 1);
                                loopState = 52;
                                break;
                            case 52:
                                BrakeToStop(60, false);
                                loopState = 60;
                                break;
                        }
                    }

                    runState = 60;
                    loopState = 0;
                    break;
                case 60:
                    switch(loopState){
                        //turn and move to align with third sample
                        case 0:
                            sleep(50);
                            StartTurning(45, turnSpeed);
                            loopState = 1;
                            break;
                        case 1:
                            Swing(1, swingSpeed);
                            Claw(true);
                            loopState = 2;
                            break;
                        case 2:
                            BrakeToStop(3, false);
                            break;
                        case 3:
                            sleep(50);
                            StartDrive(25, driveSpeed);
                            loopState = 4;
                            break;
                        case 4:
                            BrakeToStop(5, false);
                            break;
                        case 5:
                            sleep(50);
                            StartTurning(-90, turnSpeed);
                            loopState = 6;
                            break;
                        case 6:
                            BrakeToStop(10, false);
                            break;
                        //move forward and pick up the third sample
                        case 10:
                            sleep(50);
                            StartDrive(-10, driveSpeed);
                            loopState = 11;
                            break;
                        case 11:
                            BrakeToStop(12, false);
                            break;
                        case 12:
                            Swing(2, swingSpeed);
                            loopState = 13;
                            break;
                        case 13:
                            sleep(500);
                            Claw(false);
                            loopState = 20;
                            break;
                        //move all the way back to align with basket
                        case 20:
                            sleep(msForClaw);
                            rotateServo.setPosition(0.67);
                            Swing(0, driveSpeed);
                            loopState = 21;
                            break;
                        case 21:
                            StartTurning(90, turnSpeed);
                            loopState = 22;
                            break;
                        case 22:
                            BrakeToStop(23, false);
                            break;
                        case 23:
                            sleep(50);
                            StartDrive(-15, driveSpeed);
                            loopState =24;
                            break;
                        case 24:
                            BrakeToStop(25, false);
                            break;
                        case 25:
                            sleep(50);
                            StartTurning(-45, turnSpeed);
                            loopState = 26;
                            break;
                        case 26:
                            BrakeToStop(30, false);
                            break;
                        //put the third sample in the lift
                        case 30:
                            sleep(0);
                            Claw(true);
                            sleep(msForClaw);
                            Claw(false);
                            loopState = 31;
                            break;
                        case 31:
                            sleep(msForClaw);
                            Swing(1, swingSpeed);
                            loopState = 40;
                            break;
                        //deliver the third sample
                        case 40:
                            sleep(500);
                            Lift(true, 1);
                            loopState = 41;
                            break;
                        case 41:
                            //wait for the lift to go up
                            sleep(2500);
                            StartDrive(40, 0.5);
                            Claw(true);
                            loopState = 42;
                            break;
                        case 42:
                            if(!leftFrontDrive.isBusy()) {
                                StopMoving();
                                loopState = 50;
                            }
                            break;
                        //move back from the basket and lower the lift
                        case 50:
                            sleep(50);
                            StartDrive(-35, driveSpeed);
                            loopState = 51;
                            break;
                        //sleep to prevent lift from tearing itself apart
                        case 51:
                            sleep(1500);
                            Lift(false, 1);
                            loopState = 52;
                            break;
                        case 52:
                            BrakeToStop(50, true);
                            runState = 60;
                            break;
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
            liftMotor.setTargetPosition(200);
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
            clawServo.setPosition(0.70);
        }else{
            clawServo.setPosition(0.42);
        }

    }



}
