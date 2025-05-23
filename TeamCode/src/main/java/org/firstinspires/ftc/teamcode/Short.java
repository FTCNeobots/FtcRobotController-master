package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name = "Short")
public class Short extends LinearOpMode {

    private DcMotor leftFrontDrive;
    private DcMotor rightFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightBackDrive;
    private DcMotor swingMotor;
    private DcMotor liftMotor;

    private Servo clawServo;
    //according to REV 28 per revolution, *15 because of gearbox
    double countsPerRevolution = 28 * 15;
    double cmPerRevolution = 35.87;
    double ticksPerCm = countsPerRevolution / cmPerRevolution;
    double rotationRatio = 6.2;
    double power = 0.5;
    int armPosUp = 0;
    int armPosDown = -1000;
    int liftPosUp = 7400;

    @Override
    public void runOpMode() throws InterruptedException {

        leftBackDrive = hardwareMap.dcMotor.get("LBD");
        leftFrontDrive = hardwareMap.dcMotor.get("LFD");
        rightBackDrive = hardwareMap.dcMotor.get("RBD");
        rightFrontDrive = hardwareMap.dcMotor.get("RFD");
        swingMotor = hardwareMap.dcMotor.get("swing");
        liftMotor = hardwareMap.dcMotor.get("lift");

        clawServo = hardwareMap.get(Servo.class, "claw");

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
        ///put code in here


        Claw(true);
        Arm(-1, 0.2);
        sleep(500);
        Claw(false);
        Arm(1, 0.5);
        sleep(1000);
        Claw(true);
        Claw(false);
        Arm(0, 0.2);
        Lift(true, 1);
        sleep(2000);
        Lift(false, 1);
        Arm(1, 0.5);


        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(5000);
    }

    private void Straight(double speed, double cm) {
        int newTarget;

        if (opModeIsActive()) {
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            newTarget = leftFrontDrive.getCurrentPosition() + (int) (cm * ticksPerCm);
            leftFrontDrive.setTargetPosition(newTarget);
            leftBackDrive.setTargetPosition(newTarget);
            rightFrontDrive.setTargetPosition(newTarget);
            rightBackDrive.setTargetPosition(newTarget);

            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftFrontDrive.setPower(speed);
            rightFrontDrive.setPower(speed);
            leftBackDrive.setPower(speed);
            rightBackDrive.setPower(speed);

            while (opModeIsActive() &&
                    leftFrontDrive.isBusy()) {
                telemetry.addData("Running to", " %7d", newTarget);
                telemetry.addData("Currently at", " at %7d",
                        leftFrontDrive.getCurrentPosition());
                telemetry.update();
            }

            leftFrontDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightFrontDrive.setPower(0);
            rightBackDrive.setPower(0);

            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            sleep(250);
        }
    }

    private void Turn(double speed, double degrees) {
        int newTarget;

        if (opModeIsActive()) {
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            newTarget = leftFrontDrive.getCurrentPosition() + (int) (degrees / 360 * countsPerRevolution * rotationRatio);
            leftFrontDrive.setTargetPosition(newTarget);
            leftBackDrive.setTargetPosition(newTarget);
            rightFrontDrive.setTargetPosition(-newTarget);
            rightBackDrive.setTargetPosition(-newTarget);

            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftFrontDrive.setPower(speed);
            rightFrontDrive.setPower(speed);
            leftBackDrive.setPower(speed);
            rightBackDrive.setPower(speed);

            while (opModeIsActive() &&
                    leftFrontDrive.isBusy()) {
                telemetry.addData("Running to", " %7d", newTarget);
                telemetry.addData("Currently at", " at %7d",
                        leftBackDrive.getCurrentPosition());
                telemetry.addData("Running to", " %7d", newTarget);
                telemetry.addData("Currently at", " at %7d",
                        rightFrontDrive.getCurrentPosition());
                telemetry.addData("Running to", " %7d", newTarget);
                telemetry.addData("Currently at", " at %7d",
                        leftFrontDrive.getCurrentPosition());
                telemetry.addData("Running to", " %7d", newTarget);
                telemetry.addData("Currently at", " at %7d",
                        rightBackDrive.getCurrentPosition());
                telemetry.update();
            }

            leftFrontDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightFrontDrive.setPower(0);
            rightBackDrive.setPower(0);

            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            sleep(250);
        }
    }

    private void Strafe(double speed, double cm) {
        int newTarget;

        if (opModeIsActive()) {
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            newTarget = leftFrontDrive.getCurrentPosition() + (int) (cm * ticksPerCm);
            leftFrontDrive.setTargetPosition(newTarget);
            leftBackDrive.setTargetPosition(-newTarget);
            rightFrontDrive.setTargetPosition(-newTarget);
            rightBackDrive.setTargetPosition(newTarget);

            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftFrontDrive.setPower(speed);
            rightFrontDrive.setPower(speed);
            leftBackDrive.setPower(speed);
            rightBackDrive.setPower(speed);

            while (opModeIsActive() &&
                    leftFrontDrive.isBusy()) {
                telemetry.addData("Running to", " %7d", newTarget);
                telemetry.addData("Currently at", " at %7d",
                        leftFrontDrive.getCurrentPosition());
                telemetry.update();
            }

            leftFrontDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightFrontDrive.setPower(0);
            rightBackDrive.setPower(0);

            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            sleep(250);
        }
    }

    private void Arm(int trueForUp, double speed) {
        if (trueForUp == 1) {
            swingMotor.setTargetPosition(armPosUp);
        } else if (trueForUp == -1){
            swingMotor.setTargetPosition(armPosDown);
        }else if (trueForUp == 0){
            swingMotor.setTargetPosition(-300);
        }
        else{
            swingMotor.setTargetPosition(-100);
        }
        swingMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        swingMotor.setPower(speed);

        while (opModeIsActive() &&
                swingMotor.isBusy()) {

        }
        swingMotor.setPower(0);
        sleep(250);

    }

    private void Claw(boolean trueForUp) {

        if (trueForUp) {
            clawServo.setPosition(0.37);
        } else {
            clawServo.setPosition(0.06);
        }
        sleep(500);
    }
    private void Lift(boolean trueForUp, double speed){

        if (trueForUp) {
            liftMotor.setTargetPosition(liftPosUp);
        } else {
            liftMotor.setTargetPosition(0);
        }
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(speed);
        while (opModeIsActive() &&
                liftMotor.isBusy()) {

        }
        liftMotor.setPower(0);
        sleep(250);
    }

}


