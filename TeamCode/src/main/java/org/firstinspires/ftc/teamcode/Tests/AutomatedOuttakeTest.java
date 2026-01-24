package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.AutomatedOuttake;
import org.firstinspires.ftc.teamcode.Subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Kicker;

@TeleOp(name = "automated outtake")
public class AutomatedOuttakeTest extends OpMode {

    public AutomatedOuttake outtake;
    public Kicker kicker;
    public Indexer spindexer;
    public Intake intake;
    public DistanceSensor sensor, sensor2;

    @Override
    public void init(){
        outtake = new AutomatedOuttake(hardwareMap);
        kicker = new Kicker(hardwareMap);
        spindexer = new Indexer(hardwareMap);
        intake = new Intake(hardwareMap);
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
        sensor2 = hardwareMap.get(DistanceSensor.class, "sensor2");
        spindexer.setPos1();
        kicker.kicker.setPosition(kicker.down);
    }

    @Override
    public void loop(){
        spindexer.runIntake(sensor, sensor2);
        intake.run(gamepad1);
        outtake.runAutomated(kicker, spindexer, gamepad1);
        telemetry.addData("outtake velo: ", outtake.outtake.getVelocity());
        telemetry.addData("kicker pos: ", kicker.kicker.getPosition());
        telemetry.addData("time: ", outtake.time);
        telemetry.addData("timer: ", outtake.timer);
        telemetry.addData("is on: ", outtake.isOn);
    }
}
