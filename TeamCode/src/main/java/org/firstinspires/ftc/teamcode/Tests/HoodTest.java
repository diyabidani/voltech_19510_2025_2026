package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Hood;

@TeleOp
public class HoodTest extends OpMode {

    public Hood hood;

    @Override
    public void init(){
        hood = new Hood(hardwareMap);
        hood.hood.setPosition(hood.down);
    }

    @Override
    public void loop(){
        hood.run(gamepad1);
    }
}
