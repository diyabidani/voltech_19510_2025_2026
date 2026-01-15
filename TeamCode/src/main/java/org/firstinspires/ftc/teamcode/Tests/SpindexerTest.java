package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.JankIndexer;

@TeleOp(name = "spindexer move test")
public class SpindexerTest extends OpMode {

    private Intake intake;
//    private Indexer spindexer;
    private JankIndexer spindexer;
    private ColorSensor sensor;
//servo in port 4 is not working

    @Override
    public void init() {
        spindexer = new JankIndexer(hardwareMap);
        intake = new Intake(hardwareMap);
        sensor = hardwareMap.get(ColorSensor.class, "sensor");
        spindexer.setPos(spindexer.indx1);
    }

    @Override
    public void loop() {
        spindexer.run(sensor);
        intake.run(gamepad1);
        telemetry.addData("timer: ", spindexer.timer.milliseconds());
        telemetry.addData("inexer pos: ", spindexer.spindexer.getPosition());
        telemetry.addData("color apha: ", sensor.alpha());
    }
}
