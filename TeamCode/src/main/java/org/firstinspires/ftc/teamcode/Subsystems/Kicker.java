package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Kicker {

    public Servo kicker;
    public double up = 0.55;
    public double down = 0.3;
    public ElapsedTime timer;

    public Kicker(HardwareMap hardwareMap){
        kicker = hardwareMap.get(Servo.class, "kicker");
        timer = new ElapsedTime();
    }

    public void run(){
        if(kicker.getPosition()==up){
            kicker.setPosition(down);
        } else{
            kicker.setPosition(up);
        }
    }

    public void idk(){
        timer.reset();
        kicker.setPosition(up);
        if(timer.milliseconds()>500){
            kicker.setPosition(down);
        }
    }
}
