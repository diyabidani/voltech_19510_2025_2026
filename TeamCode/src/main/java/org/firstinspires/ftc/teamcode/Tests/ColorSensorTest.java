package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "color sensor test")
public class ColorSensorTest extends OpMode {

    public ColorSensor sensor;
    public int artifactColor;

    @Override
    public void init() {
        sensor = hardwareMap.get(ColorSensor.class, "sensor");
    }

    @Override
    public void loop() {
        if((sensor.alpha()>50&&sensor.green()>sensor.blue()&&sensor.red()<45)){
            telemetry.addData("GREEN", "");
        } else if((sensor.alpha()>50&&sensor.blue()>sensor.green())){
            telemetry.addData("PURPLE", "");
        } else{
            telemetry.addData("no ball detected: ", "");
        }
        telemetry.addData("green: ", sensor.green());
        telemetry.addData("red: ", sensor.red());
        telemetry.addData("blue: ", sensor.blue());
        telemetry.addData("alpha: ", sensor.alpha());
    }
}
