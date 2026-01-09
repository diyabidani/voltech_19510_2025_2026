package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Indexer {

    private Servo kicker, spindexer;
    private ColorSensor colorSensor;

    public Indexer(HardwareMap hardwareMap){
        spindexer = hardwareMap.get(Servo.class, "spindexer");
        kicker = hardwareMap.get(Servo.class, "kicker");
    }

    //edit these values
    public enum spindexerPositions{
        FIRST(0),
        SECOND(0.3), //was 0.3 but vartype was bugging
        THIRD(0.7);

        public double value;

        spindexerPositions(double pos){
            this.value = pos;
        }
    }

    //edit these values asw
    public enum kickerPositions{
        DOWN(0),
        UP(0.3); //was 0.3 but vartype was bugging

        public double value;

        kickerPositions(double pos){
            this.value = pos;
        }
    }

}
