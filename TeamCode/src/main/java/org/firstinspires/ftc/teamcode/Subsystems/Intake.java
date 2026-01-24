package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    public DcMotorEx intake;
    public boolean prevIsOn = true;

    public boolean prevLB;
    public boolean intakeToggle;
    public boolean isOn = false;

    public Intake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotorEx.class, "intake");
    }

    public void run(Gamepad gm){
        boolean lb = gm.left_bumper;
        if (lb && !prevLB) intakeToggle = !intakeToggle;
        prevLB = lb;
        if (gm.left_trigger > 0) {
            intake.setPower(-0.6);
        } else {
            intake.setPower(intakeToggle ? 0.6 : 0.0);
        }
    }
}
