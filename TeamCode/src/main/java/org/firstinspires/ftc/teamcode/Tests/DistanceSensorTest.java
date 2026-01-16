package org.firstinspires.ftc.teamcode.Tests;

import static java.lang.Thread.onSpinWait;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.JankIndexer;

@TeleOp
public class DistanceSensorTest extends OpMode {

    public DistanceSensor sensor;
    public Intake intake;
    public ElapsedTime timer;
    public boolean detected = false;
    public JankIndexer spindexer;

    @Override
    public void init() {
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
        intake = new Intake(hardwareMap);
        spindexer = new JankIndexer(hardwareMap);
        timer = new ElapsedTime();
        spindexer.spindexer.setPosition(spindexer.indx2);
    }

    @Override
    public void loop() {
        intake.run(gamepad1);
        timer.reset();
        if(sensor.getDistance(DistanceUnit.MM)<46.5){
            detected = true;
        }
        telemetry.addData("distance: ", sensor.getDistance(DistanceUnit.MM));
        telemetry.addData("ball Detected", detected);
    }
}
