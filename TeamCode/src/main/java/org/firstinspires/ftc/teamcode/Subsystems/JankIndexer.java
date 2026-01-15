package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class JankIndexer {
    public ElapsedTime timer;
    public Servo spindexer;

    public double indx1 = 0.20; //slot 1 intake, slot 2 outtake
    public double indx2 = 0.37; //slot 2 intake, slot 3 outtake
    public double indx3 = 0.50; //slot 3 intake, slot 1 outtake

    public int slot1 = 0;
    public int slot2 = 0;
    public int slot3 = 0;

    public JankIndexer(HardwareMap hardwareMap){
        spindexer = hardwareMap.get(Servo.class, "spindexer");
        timer = new ElapsedTime();
    }

    public void run(ColorSensor sensor){
        if((sensor.alpha()>50&&sensor.green()>sensor.blue()&&sensor.red()<40)||(sensor.alpha()>50&&sensor.blue()>sensor.green())){
            timer.startTime();
//            if(timer.milliseconds()>500){
                if(spindexer.getPosition()==indx1){
                    spindexer.setPosition(indx2);
                    slot1 = 1;
                }
                if(spindexer.getPosition()==indx2){
                    spindexer.setPosition(indx3);
                    slot2 = 1;
                }
                if(spindexer.getPosition()==indx3){
                    spindexer.setPosition(indx1);
                    slot3 = 1;
                }
            }
//        }
    }

    public void setPos(double pos){
        spindexer.setPosition(pos);
    }
}
