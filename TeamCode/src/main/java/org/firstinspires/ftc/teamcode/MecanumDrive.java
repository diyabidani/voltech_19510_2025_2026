package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="MecanumDrive")
public class MecanumDrive extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");

        // Set motor directions
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addLine("Mecanum Drive Ready");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Joystick inputs
        double y  = -gamepad1.left_stick_y; // forward/back
        double x  = gamepad1.left_stick_x;  // strafe
        double rx = gamepad1.right_stick_x; // rotation

        // Apply deadzone
        double deadzone = 0.05;
        y  = Math.abs(y)  > deadzone ? y  : 0;
        x  = Math.abs(x)  > deadzone ? x  : 0;
        rx = Math.abs(rx) > deadzone ? rx : 0;

        // Mecanum calculations
        double frontLeftPower  = y + x + rx;
        double frontRightPower = y - x - rx;
        double backLeftPower   = y - x + rx;
        double backRightPower  = y + x - rx;

        // Normalize powers
        double max = Math.max(1.0, Math.max(Math.abs(frontLeftPower),
                Math.max(Math.abs(frontRightPower),
                        Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)))));

        frontLeft.setPower(frontLeftPower / max);
        frontRight.setPower(frontRightPower / max);
        backLeft.setPower(backLeftPower / max);
        backRight.setPower(backRightPower / max);

        // Telemetry
        telemetry.addData("Drive", "Y=%.2f, X=%.2f, RX=%.2f", y, x, rx);
        telemetry.update();
    }
}