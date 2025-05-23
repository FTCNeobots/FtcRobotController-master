package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "OpModeBumper")
@Disabled
public class OpModeBumper extends LinearOpMode {

    private DcMotor leftFrontDrive;
    private DcMotor rightFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightBackDrive;

    @Override
    public void runOpMode() throws InterruptedException {
        leftBackDrive = hardwareMap.dcMotor.get("LBD");
        leftFrontDrive = hardwareMap.dcMotor.get("LFD");
        rightBackDrive = hardwareMap.dcMotor.get("RBD");
        rightFrontDrive = hardwareMap.dcMotor.get("RFD");

        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()){
            Direction(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_bumper, gamepad1.left_bumper);
        }


    }
     /// Zet bewegingen om in motorvermogens
     /// @param _TurnRight tussen 0 en 1 rechts
      /// @param _TurnLeft tussen 0 en 1 links
     /// @param _X tussen -1 (links) en 1
     /// @param _Y tussen -1 (achter) en 1
    private void Direction(double _X, double _Y, boolean _TurnRight, boolean _TurnLeft){
        double _Turn;
        if(_TurnRight){
            _Turn = 1;
        } else if (_TurnLeft) {
            _Turn = -1;
        }else{
            _Turn = 0;
        }

        leftFrontDrive.setPower(_Y - _Turn - _X);
        leftBackDrive.setPower(_Y - _Turn + _X);
        rightBackDrive.setPower(_Y + _Turn - _X);
        rightFrontDrive.setPower(_Y + _Turn + _X);
    }
}
