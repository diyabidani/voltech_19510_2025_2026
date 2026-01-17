package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.JankIndexer;

@TeleOp(name = "spindexer move test")
public class SpindexerTest extends OpMode {

    private Intake intake;
//    private Indexer spindexer;
    private Indexer spindexer;
    private DistanceSensor sensor, sensor2;
//servo in port 4 is not working

    @Override
    public void init() {
        spindexer = new Indexer(hardwareMap);
        intake = new Intake(hardwareMap);
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
        sensor2 = hardwareMap.get(DistanceSensor.class, "sensor2");
        spindexer.setPos1();
    }

    @Override
    public void loop() {
        spindexer.runIntake(sensor, sensor2);
        intake.run(gamepad1);
        telemetry.addData("inexer pos: ", spindexer.currentIndxPos);
        telemetry.addData("indexer pos actual: ", spindexer.spindexer.getPosition());
        telemetry.addData("sensor1: ", sensor.getDistance(DistanceUnit.MM));
        telemetry.addData("sensor2: ", sensor2.getDistance(DistanceUnit.MM));
        telemetry.addData("slot 0: ", spindexer.slots[0]);
        telemetry.addData("slot 1: ", spindexer.slots[1]);
        telemetry.addData("slot 2: ", spindexer.slots[2]);
    }
}
