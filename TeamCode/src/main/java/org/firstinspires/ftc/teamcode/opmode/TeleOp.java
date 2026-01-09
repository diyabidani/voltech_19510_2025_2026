package org.firstinspires.ftc.teamcode.OpMode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends OpMode {

    Follower follower;

    @Override
    public void init(){
        follower = Constants.createFollower(hardwareMap);
    }

    @Override
    public void loop(){

    }

    public double getDistance(){
        double deltaX = follower.getPose().getX();
        double deltaY = 144-follower.getPose().getY();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }
}
