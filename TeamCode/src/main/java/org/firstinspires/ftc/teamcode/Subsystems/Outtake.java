package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Outtake {

    public DcMotorEx outtake;
    public boolean isOn = false;
    public boolean prevRB;
    public boolean outtakeToggle;

    public Outtake(HardwareMap hardwareMap){
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

//    public void run(double velocity){
//        if(!isOn){
//            isOn = true;
//            outtake.setVelocity(velocity);
//        } else{
//            isOn = false;
//            outtake.setVelocity(0);
//        }
//    }

    public void run(Gamepad gm){
        boolean rb = gm.right_bumper;
        if (rb && !prevRB) outtakeToggle = !outtakeToggle;
        prevRB = rb;
        outtake.setVelocity(outtakeToggle ? -2800 : 0.0);
    }

    public double velocity(double distance){
        //some calculation w distance to determine velocity -- i will calculate this after i can test the outtake on teh field
        return 1400;
    }
}
