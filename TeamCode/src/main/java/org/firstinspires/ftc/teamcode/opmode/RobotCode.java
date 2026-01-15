package org.firstinspires.ftc.teamcode.opmode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name = "RobotCode", group = "TeleOp")
public class RobotCode extends OpMode {

    // ------------------ DRIVETRAIN ------------------
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    // ------------------ MECHANISMS ------------------
    private DcMotor intakeMotor;
    private DcMotor outtakeMotor;
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
        FIRST(0.0), SECOND(0.3), THIRD(0.7);
        public final double value;
        SpindexerPositions(double value) { this.value = value; }
    }

    public enum KickerPositions {
        DOWN(0.0), UP(0.3);
        public final double value;
        KickerPositions(double value) { this.value = value; }
    }

    // ------------------ VISION ------------------
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTagProcessor;

    // ------------------ INIT ------------------
    @Override
    public void init() {

        // --------- Drivetrain ----------
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");
        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // --------- Mechanisms ----------
        intakeMotor  = hardwareMap.get(DcMotor.class, "intake_motor");
        outtakeMotor = hardwareMap.get(DcMotor.class, "outtake_motor");

        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        kicker = hardwareMap.get(Servo.class, "kicker");
        spindexer = hardwareMap.get(Servo.class, "spindexer");

        kicker.setPosition(KickerPositions.DOWN.value);
        spindexer.setPosition(SpindexerPositions.FIRST.value);

        // --------- Sensors ----------
        colorSensor = hardwareMap.get(ColorSensor.class, "color_sensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "color_sensor");

        // --------- Vision ----------
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .addProcessor(aprilTagProcessor)
                .build();

        telemetry.addLine("Robot + AprilTag Vision Initialized");
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
        telemetry.addData("Distance (cm)", "%.2f", distanceSensor.getDistance(DistanceUnit.CM));

        telemetry.addLine("----------------------");
        telemetry.addData("Intake", intakeToggle);
        telemetry.addData("Outtake", outtakeToggle);

        // ---------- APRILTAG DETECTION ----------
        List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
        telemetry.addLine("---- APRILTAG DETECTION ----");
        telemetry.addData("AprilTags detected", detections.size());

        if (!detections.isEmpty()) {
            AprilTagDetection tag = detections.get(0);
            telemetry.addData("Tag ID", tag.id);

            if (tag.id == 21) {
                telemetry.addLine("1");
                telemetry.addLine("GPP");
            } else if (tag.id == 22) {
                telemetry.addLine("2");
                telemetry.addLine("PGP");
            } else if (tag.id == 33) {
                telemetry.addLine("3");
                telemetry.addLine("PPG");
            }

            if (tag.ftcPose != null) {
                telemetry.addData(
                        "Pose (X,Y,Z)",
                        "%.1f, %.1f, %.1f",
                        tag.ftcPose.x,
                        tag.ftcPose.y,
                        tag.ftcPose.z
                );
            }
        }

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

    // ------------------ STOP ------------------
    @Override
    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
}
