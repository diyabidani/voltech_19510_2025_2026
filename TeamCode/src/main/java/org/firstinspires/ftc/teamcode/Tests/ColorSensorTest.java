package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorSensorTest extends OpMode {

    public ColorSensor sensor;

    @Override
    public void init() {
        sensor = hardwareMap.get(ColorSensor.class, "sensor");
    }

    @Override
    public void loop() {
        telemetry.addData("green: ", sensor.green());
        telemetry.addData("red: ", sensor.red());
        telemetry.addData("blue: ", sensor.blue());
        telemetry.addData("alpha: ", sensor.alpha());
    }
}
