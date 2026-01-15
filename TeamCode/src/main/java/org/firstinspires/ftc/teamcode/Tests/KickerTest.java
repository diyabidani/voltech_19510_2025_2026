package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Kicker;

@TeleOp(name = "kickerTest")
public class KickerTest extends OpMode {

    public Kicker kicker;

    @Override
    public void init() {
        kicker = new Kicker(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad1.x){
            kicker.run();
        }
    }
}
