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
        if(sensor.green()>sensor.red()&& sensor.green()>sensor.blue()){
            telemetry.addData("GREEN", "");
        } else if(sensor.blue()>sensor.green()&&sensor.blue()>sensor.red()){
            telemetry.addData("PURPLE", "");
        }
        if(sensor.alpha()>50){
            telemetry.addData("BALL DETECTED", "");
        }else{
            telemetry.addData("NO BALL DETECTED", "");
        }
        telemetry.addData("green: ", sensor.green());
        telemetry.addData("red: ", sensor.red());
        telemetry.addData("blue: ", sensor.blue());
        telemetry.addData("alpha: ", sensor.alpha());
    }
}
