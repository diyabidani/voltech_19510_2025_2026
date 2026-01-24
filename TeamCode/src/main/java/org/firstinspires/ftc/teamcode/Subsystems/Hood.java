package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hood {

    public Servo hood;
    public double up = 0.2;
    public double down = 0.45;
    public boolean prevA;
    public boolean hoodToggle;

    public Hood(HardwareMap hardwareMap){
        hood = hardwareMap.get(Servo.class, "servo");
    }

    public void run(Gamepad gm){
        boolean a = gm.a;
        if (a && !prevA) hoodToggle = !hoodToggle;
        prevA = a;
        hood.setPosition(hoodToggle ? up : down);
    }

}
