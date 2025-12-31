package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "servo test")
public class ServoTest extends OpMode {

    public Servo servo1, servo2;

    public enum spindexerPositions{
        FIRST(0),
        SECOND(0.3), //was 0.3 but vartype was bugging
        THIRD(0.7);

        public double value;

        spindexerPositions(double pos){
            this.value = pos;
        }
    }

    public enum kickerPositions{
        DOWN(0),
        UP(0.3); //was 0.3 but vartype was bugging

        public double value;

        kickerPositions(double pos){
            this.value = pos;
        }
    }

    @Override
    public void init() {
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");
    }

    @Override
    public void loop() {
        if(gamepad1.y){
            servo1.setPosition(spindexerPositions.FIRST.value);
        }
        if(gamepad1.b){
            servo1.setPosition(spindexerPositions.SECOND.value);
        }
        if(gamepad1.a){
            servo1.setPosition(spindexerPositions.THIRD.value);
        }

        if(gamepad1.dpad_up){
            servo2.setPosition(kickerPositions.UP.value);
        }
        if(gamepad1.dpad_down){
            servo2.setPosition(kickerPositions.DOWN.value);
        }
    }
}
