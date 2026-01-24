package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class Distance extends OpMode {

    public DistanceSensor sensor, sensor2;

    @Override
    public void init(){
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
        sensor2 = hardwareMap.get(DistanceSensor.class, "sensor2");
    }

    @Override
    public void loop(){
        telemetry.addData("sensor1: ", sensor.getDistance(DistanceUnit.MM));
        telemetry.addData("sensor2: ", sensor2.getDistance(DistanceUnit.MM));
    }
}
