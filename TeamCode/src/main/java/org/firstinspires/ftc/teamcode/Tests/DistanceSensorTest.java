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

    public DistanceSensor sensor, sensor2;
    public Intake intake;
    public ElapsedTime timer;
    public JankIndexer spindexer;
    public double time;
    public boolean detected = false;

    @Override
    public void init() {
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
        sensor2 = hardwareMap.get(DistanceSensor.class, "sensor2");
        intake = new Intake(hardwareMap);
        spindexer = new JankIndexer(hardwareMap);
        timer = new ElapsedTime();
        spindexer.spindexer.setPosition(spindexer.indx2);
        time = 200000;
    }

    @Override
    public void start(){
        timer.reset();
    }

    @Override
    public void loop() {
        intake.run(gamepad1);
        if((sensor.getDistance(DistanceUnit.MM)<40)||(sensor2.getDistance(DistanceUnit.MM)<40)){
            telemetry.addData("dteccted", "");
            time = timer.milliseconds();
            detected = true;
        }
//        if(timer.milliseconds()>time+5000){
//            time = 200000;
//            telemetry.addData("yay this works", "");
//            spindexer.setPos(spindexer.indx3);
//            //turn spindexer
//        }
        telemetry.addData("distance: ", sensor.getDistance(DistanceUnit.MM));
        telemetry.addData("timer: ", timer.milliseconds());
        telemetry.addData("detec: ", detected);
        telemetry.addData("sn2{ ", sensor2.getDistance(DistanceUnit.MM));
    }
}
