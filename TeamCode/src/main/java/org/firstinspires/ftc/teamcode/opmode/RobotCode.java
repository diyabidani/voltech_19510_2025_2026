package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "RobotCode")
public class RobotCode extends OpMode {

    // ------------------ DRIVETRAIN ------------------
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    // ------------------ MECHANISMS ------------------
    private DcMotor intakeMotor;
    private DcMotorEx outtakeMotor;

    private Servo kicker;
    private Servo spindexer;

    // ------------------ SENSORS ------------------
    private ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    // ------------------ TOGGLES ------------------
    private boolean intakeToggle = false;
    private boolean outtakeToggle = false;

    private boolean prevRB = false;
    private boolean prevB = false;

    // ------------------ ENUMS ------------------
    public enum SpindexerPositions {
        FIRST(0.0),
        SECOND(0.3),
        THIRD(0.7);

        public final double value;
        SpindexerPositions(double value) { this.value = value; }
    }

    public enum KickerPositions {
        DOWN(0.0),
        UP(0.3);

        public final double value;
        KickerPositions(double value) { this.value = value; }
    }

    // ------------------ INIT ------------------
    @Override
    public void init() {

        // Drivetrain
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");
        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Motors
        intakeMotor  = hardwareMap.get(DcMotor.class, "intake_motor");
        outtakeMotor = hardwareMap.get(DcMotorEx.class, "outtake_motor");

        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Servos
        kicker = hardwareMap.get(Servo.class, "kicker");
        spindexer = hardwareMap.get(Servo.class, "spindexer");

        kicker.setPosition(KickerPositions.DOWN.value);
        spindexer.setPosition(SpindexerPositions.FIRST.value);

        // Color sensor (REV V3)
        colorSensor = hardwareMap.get(ColorSensor.class, "color_sensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "color_sensor");

        telemetry.addLine("Initialized");
        telemetry.update();
    }

    // ------------------ LOOP ------------------
    @Override
    public void loop() {

        // ---------- DRIVE ----------
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

        double max = Math.max(1.0,
                Math.max(Math.abs(fl),
                        Math.max(Math.abs(fr),
                                Math.max(Math.abs(bl), Math.abs(br)))));

        frontLeft.setPower(fl / max);
        frontRight.setPower(fr / max);
        backLeft.setPower(bl / max);
        backRight.setPower(br / max);

        // ---------- INTAKE TOGGLE (RB) ----------
        boolean rb = gamepad1.right_bumper;
        if (rb && !prevRB) intakeToggle = !intakeToggle;
        prevRB = rb;

        if (gamepad1.right_trigger > 0.5) {
            intakeMotor.setPower(-1.0);
        } else {
            intakeMotor.setPower(intakeToggle ? 1.0 : 0.0);
        }

        // ---------- OUTTAKE TOGGLE (B) ----------
        boolean b = gamepad1.b;
        if (b && !prevB) outtakeToggle = !outtakeToggle;
        prevB = b;

        outtakeMotor.setPower(outtakeToggle ? 1.0 : 0.0);

        // ---------- SPINDEXER ----------
        if (gamepad1.y)
            spindexer.setPosition(SpindexerPositions.FIRST.value);
        if (gamepad1.x)
            spindexer.setPosition(SpindexerPositions.SECOND.value);
        if (gamepad1.a)
            spindexer.setPosition(SpindexerPositions.THIRD.value);

        // ---------- KICKER ----------
        if (gamepad1.dpad_up)
            kicker.setPosition(KickerPositions.UP.value);
        if (gamepad1.dpad_down)
            kicker.setPosition(KickerPositions.DOWN.value);

        // ---------- COLOR SENSOR ----------
        String ballColor = detectBallColor();

        telemetry.addLine("---- COLOR SENSOR ----");
        telemetry.addData("Detected Color", ballColor);
        telemetry.addData("Red", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue", colorSensor.blue());
        telemetry.addData("Distance (cm)",
                "%.2f", distanceSensor.getDistance(DistanceUnit.CM));

        telemetry.addLine("----------------------");
        telemetry.addData("Intake", intakeToggle);
        telemetry.addData("Outtake", outtakeToggle);
        telemetry.addData("outtake velocity: ", outtakeMotor.getVelocity());
        telemetry.update();
    }

    // ------------------ COLOR DETECTION ------------------
    private String detectBallColor() {

        int r = colorSensor.red();
        int g = colorSensor.green();
        int b = colorSensor.blue();

        // Always detect GREEN or PURPLE
        if (g > r && g > b) {
            return "GREEN";
        } else {
            return "PURPLE";
        }
    }
}