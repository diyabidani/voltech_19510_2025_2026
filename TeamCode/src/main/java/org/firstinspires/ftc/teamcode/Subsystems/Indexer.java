package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Indexer {

    public Servo spindexer;
//    private ColorSensor sensor;
    public ElapsedTime timer;

    public double[] positions = new double[]{0.15,0.30,0.45};
//    public double indx1 = 0.15; //slot 1 intake, slot 2 outtake
//    public double indx2 = 0.43; //slot 2 intake, slot 3 outtake
//    public double indx3 = 0.69; //slot 3 intake, slot 1 outtake

    public double[] slots = new double[]{0,0,0};
//    public double[] slot1 = new double[]{0,0};
//    public double[] slot2 = new double[]{0,0};
//    public double[] slot3 = new double[]{0,0};

    public int currentIndxPos = 0;
    //index 1: 0 means no ball, index 2: 0 means green and 1 means purple
    public double time;

    public Indexer(HardwareMap hardwareMap){
        spindexer = hardwareMap.get(Servo.class, "spindexer");
//        kicker = hardwareMap.get(Servo.class, "kicker");
        timer = new ElapsedTime();
        time = 200000;
    }

    public void runIntake(DistanceSensor sensor, DistanceSensor sensor2){
        if((sensor.getDistance(DistanceUnit.MM)<37)||(sensor2.getDistance(DistanceUnit.MM)<37)){
            time = timer.milliseconds();
        }
        if(timer.milliseconds()>time+10){
            time = 200000;
            slots[currentIndxPos] = 1;
            //turn spindexer
            if(currentIndxPos!=2){
                spindexer.setPosition(positions[currentIndxPos+1]);
                currentIndxPos += 1;
            } else{
                spindexer.setPosition(positions[0]);
                currentIndxPos = 0;
            }
        }
    }

    public void runOuttake(){
        slots[currentIndxPos] = 0;
        if(currentIndxPos==0){
            spindexer.setPosition(positions[2]);
            currentIndxPos = 2;
        } else{
            spindexer.setPosition(positions[currentIndxPos-1]);
            currentIndxPos -= 1;
        }
    }

//    public void run(ColorSensor sensor){
//        if(sensor.alpha()>50){
//            timer.startTime();
//            //log that there is a ball in this slot
//            slots[currentIndxPos] = 1;
//            if(timer.milliseconds()==1000){
//                if(currentIndxPos<2){
//                    spindexer.setPosition(positions[currentIndxPos+1]);
//                    currentIndxPos += 1;
//                    //move to the next slot
//                } else{
//                    spindexer.setPosition(0);
//                    currentIndxPos = 0;
//                    //move to slot 0
//                }
//            }
////            if(sensor.green()>sensor.red()&& sensor.green()>sensor.blue()){
////                //green in slot 1
////                slot1[1] = 0;
////            } else if(sensor.blue()>sensor.green()&&sensor.blue()>sensor.red()){
////                //purple in slot 1
////                slot2[1] = 1;
////            }
//        }else{
//            //no ball in slot 1
//            slots[currentIndxPos] = 0;
//        }
//    }

    public void setPos1(){
        spindexer.setPosition(positions[0]);
    }
    public void setPos2(){
        spindexer.setPosition(positions[1]);
    }
    public void setPos3(){
        spindexer.setPosition(positions[2]);
    }

    //edit these values
//    public enum spindexerPositions{
//        FIRST(0),
//        SECOND(0.3), //was 0.3 but vartype was bugging
//        THIRD(0.7);
//
//        public double value;
//
//        spindexerPositions(double pos){
//            this.value = pos;
//        }
//    }
//
//    //edit these values asw
//    public enum kickerPositions{
//        DOWN(0),
//        UP(0.3); //was 0.3 but vartype was bugging
//
//        public double value;

//        kickerPositions(double pos){
//            this.value = pos;
//        }
//    }

}
