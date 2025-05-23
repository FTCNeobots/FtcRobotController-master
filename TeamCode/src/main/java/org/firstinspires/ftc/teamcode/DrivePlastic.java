package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp(name = "DrivePlastic")
public class DrivePlastic extends LinearOpMode {

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
    private double liftSpeed = 1    ;

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

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        waitForStart();
        while (opModeIsActive()){
            NormalDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            SpeedControl();
            ArmControl();

            // This button choice was made so that it is hard to hit on accident,
            if (gamepad1.back) {
                imu.resetYaw();
                telemetry.addData("Yaw ", "reset!");
                telemetry.update();

            }
            botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        }


    }

    private void NormalDrive(double _Xget, double _Yget, double _Turnget){

        double _X = _Xget * Math.cos(botHeading) - _Yget * Math.sin(botHeading);
        double _Y = _Xget * Math.sin(botHeading) + _Yget * Math.cos(botHeading);
        ///die negatief hoort niet te hoeven, maar helpt wel
        double _Turn = -_Turnget * turnSpeed;
        _X = _X *1.1;


        double _LFSpeed = MathLogic.Clamp(_Y - _X + _Turn, -1, 1)*maxSpeed;
        double _LBSpeed = MathLogic.Clamp(_Y + _X + _Turn, -1, 1)*maxSpeed;
        double _RBSpeed = MathLogic.Clamp(_Y - _X - _Turn, -1, 1)*maxSpeed;
        double _RFSpeed = MathLogic.Clamp(_Y + _X - _Turn, -1, 1)*maxSpeed;

        telemetry.addData("Left front ", _LFSpeed);
        telemetry.addData("Left back ", _LBSpeed);
        telemetry.addData("Right back ", _RBSpeed);
        telemetry.addData("Right front ", _RFSpeed);
        telemetry.update();

        leftFrontDrive.setPower(_LFSpeed);
        leftBackDrive.setPower(_LBSpeed);
        rightBackDrive.setPower(_RBSpeed);
        rightFrontDrive.setPower(_RFSpeed);

    }
    private void SpeedControl(){
        if(gamepad1.left_bumper){
            maxSpeed = 0.25;
            turnSpeed = 4;

            telemetry.addData("Max speed ", maxSpeed);
            telemetry.update();
        }else if(gamepad1.right_bumper){
            maxSpeed = 0.5;
            turnSpeed = 2;

            telemetry.addData("Max speed ", maxSpeed);
            telemetry.update();
        }

    }

    //bakje spinnen met motor is triggers, arm swing is bumpers, arm uitschuiven is a en b, bakje kantelen met servo is dpad up en down
    public void ArmControl(){
        if(gamepad2.a){
            swingMotor.setPower(-swingSpeed);
        }if(gamepad2.b){
            swingMotor.setPower(swingSpeed);
        }

        if(gamepad2.left_bumper){
            liftMotor.setPower(-liftSpeed);
        }if(gamepad2.right_bumper){
            liftMotor.setPower(liftSpeed);
        }

        if(gamepad2.left_trigger > 0){
            liftMotor.setPower(-0.5 * liftSpeed);
        }if(gamepad2.right_trigger > 0){
            liftMotor.setPower(0.5 * liftSpeed);
        }

        if(gamepad2.dpad_up){
            clawServo.setPosition(0.1);
        }if (gamepad2.dpad_down){
            clawServo.setPosition(0.46);
        }
        Gamepad2StopMoving();

    }
    public void Gamepad2StopMoving(){
        liftMotor.setPower(0);
        swingMotor.setPower(0);
    }

}
