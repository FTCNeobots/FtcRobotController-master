package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "Long")
public class Long extends LinearOpMode {

    private DcMotor leftFrontDrive;
    private DcMotor rightFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightBackDrive;
    //according to REV 28 per revolution, *15 because of gearbox
    double countsPerRevolution = 28 * 15;
    double cmPerRevolution = 37;
    double ticksPerCm = countsPerRevolution/cmPerRevolution;
    double rotationRatio = 6.2;
    double power = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {

        leftBackDrive = hardwareMap.dcMotor.get("LBD");
        leftFrontDrive = hardwareMap.dcMotor.get("LFD");
        rightBackDrive = hardwareMap.dcMotor.get("RBD");
        rightFrontDrive = hardwareMap.dcMotor.get("RFD");

        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        Straight(0.5, -60);
        Strafe(0.3, -340);
        Straight(0.3, 80);


        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(5000);
    }

    private void Straight(double speed, double cm){
        int newTarget;

        if(opModeIsActive()){
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            newTarget = leftFrontDrive.getCurrentPosition() + (int)(cm * ticksPerCm);
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
                telemetry.addData("Running to",  " %7d", newTarget);
                telemetry.addData("Currently at",  " at %7d",
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

            newTarget = leftFrontDrive.getCurrentPosition() + (int) (degrees/360 * countsPerRevolution * rotationRatio);
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
    private void Strafe(double speed, double cm){
        int newTarget;

        if(opModeIsActive()){
            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            newTarget = leftFrontDrive.getCurrentPosition() + (int)(cm * ticksPerCm);
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
                telemetry.addData("Running to",  " %7d", newTarget);
                telemetry.addData("Currently at",  " at %7d",
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


}
