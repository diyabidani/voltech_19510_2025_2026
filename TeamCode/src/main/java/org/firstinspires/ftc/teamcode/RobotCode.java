package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="RobotCode")
public class RobotCode extends OpMode {

    // Drivetrain
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    // Intake + Outtake motors
    private DcMotor intakeMotor;
    private DcMotor outtakeMotor;

    // Axon Mini (continuous rotation servo)
    private CRServo axonMini;

    // Toggle states
    private boolean intakeToggle = false;
    private boolean prevRB = false;

    private boolean outtakeToggle = false;
    private boolean prevB = false;

    private boolean prevLB = false;

    @Override
    public void init() {

        // Drivetrain
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");
        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Intake + Outtake motors
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");
        outtakeMotor = hardwareMap.get(DcMotor.class, "outtake_motor");

        // Axon Mini CR servo
        axonMini = hardwareMap.get(CRServo.class, "axon_mini");
        axonMini.setPower(0.5); // STOP

        // Motor directions
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);   // CCW default
        outtakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);  // CCW

        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        // ------------------ DRIVING ------------------
        double y  = -gamepad1.left_stick_y;
        double x  =  gamepad1.left_stick_x;
        double rx =  gamepad1.right_stick_x;

        double dz = 0.05;
        if (Math.abs(y) < dz) y = 0;
        if (Math.abs(x) < dz) x = 0;
        if (Math.abs(rx) < dz) rx = 0;

        double fl = y + x + rx;
        double fr = y - x - rx;
        double bl = y - x + rx;
        double br = y + x - rx;

        double max = Math.max(1.0, Math.max(Math.abs(fl),
                Math.max(Math.abs(fr), Math.max(Math.abs(bl), Math.abs(br)))));

        frontLeft.setPower(fl / max);
        frontRight.setPower(fr / max);
        backLeft.setPower(bl / max);
        backRight.setPower(br / max);


        // ------------------ INTAKE TOGGLE (RB) ------------------
        boolean rbPressed = gamepad1.right_bumper;

        if (rbPressed && !prevRB) {
            intakeToggle = !intakeToggle;
        }
        prevRB = rbPressed;

        // ------------------ INTAKE DIRECTION LOGIC ------------------
        boolean rtHeld = gamepad1.right_trigger > 0.5;

        if (rtHeld) {
            // CLOCKWISE override
            intakeMotor.setPower(-1.0);
        } else {
            // Normal CCW toggle behavior
            intakeMotor.setPower(intakeToggle ? 1.0 : 0.0);
        }


        // ------------------ OUTTAKE TOGGLE (B) ------------------
        boolean bPressed = gamepad1.b;

        if (bPressed && !prevB) {
            outtakeToggle = !outtakeToggle;
        }
        prevB = bPressed;

        outtakeMotor.setPower(outtakeToggle ? 1.0 : 0.0);


        // ------------------ AXON MINI KICK (LB) ------------------
        boolean lbPressed = gamepad1.left_bumper;

        if (lbPressed && !prevLB) {
            // KICK UP (fast rotation)
            axonMini.setPower(1.0);   // adjust direction if needed
            sleep(150);               // adjust to get ~120 degrees

            // KICK DOWN (fast rotation back)
            axonMini.setPower(0.0);   // opposite direction
            sleep(150);

            // STOP
            axonMini.setPower(0.5);
        }
        prevLB = lbPressed;


        // ------------------ TELEMETRY ------------------
        telemetry.addData("Intake Toggle", intakeToggle);
        telemetry.addData("Intake Dir", rtHeld ? "CLOCKWISE" : (intakeToggle ? "COUNTER-CLOCKWISE" : "OFF"));
        telemetry.addData("Outtake Toggle", outtakeToggle);
        telemetry.addData("Axon Mini", lbPressed ? "KICK" : "READY");
        telemetry.update();
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}