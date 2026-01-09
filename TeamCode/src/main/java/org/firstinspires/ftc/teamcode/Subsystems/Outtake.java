package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Outtake {

    private DcMotorEx outtake;
    public boolean isOn = false;

    public Outtake(HardwareMap hardwareMap){
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");
    }

    public void run(double velocity){
        if(!isOn){
            outtake.setVelocity(velocity);
            isOn = true;
        } else{
            outtake.setVelocity(0);
            isOn = false;
        }
    }

    public double velocity(double distance){
        //some calculation w distance to determine velocity -- i will calculate this after i can test the outtake on teh field
        return 1400;
    }
}
