package org.firstinspires.ftc.teamcode.opmode;

import static java.lang.Thread.sleep;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.AutomatedOuttake;
import org.firstinspires.ftc.teamcode.Subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.JankIndexer;
import org.firstinspires.ftc.teamcode.Subsystems.Kicker;
import org.firstinspires.ftc.teamcode.Subsystems.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends OpMode {

    Follower follower;
    AutomatedOuttake outtake;
    Intake intake;
    Indexer spindexer;
    Kicker kicker;
    ElapsedTime timer = new ElapsedTime();
    DistanceSensor sensor, sensor2;

    //intake
    private boolean prevLB = false;
    private boolean intakeToggle = false;

    //outtake
    private boolean prevRB = false;
    private boolean outtakeToggle = false;

    //indexer
    private boolean prevA = false;
    private boolean indexerToggle = false;

    public Supplier<PathChain> pathChain;


    @Override
    public void init(){
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0,0,0));
        pathChain = () -> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(45, 98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                .setBrakingStart(1)
                .setBrakingStrength(1)
                .build();
        follower.update();
        outtake = new AutomatedOuttake(hardwareMap);
        intake = new Intake(hardwareMap);
        spindexer = new Indexer(hardwareMap);
        kicker = new Kicker(hardwareMap);
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
        sensor2 = hardwareMap.get(DistanceSensor.class, "sensor2");
    }

    @Override
    public void start(){
        follower.startTeleopDrive();
        follower.update();
        spindexer.setPos1();
        kicker.kicker.setPosition(kicker.down);
    }

    @Override
    public void loop(){
        //drive
        follower.setTeleOpDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, true);
        follower.update();

        //intake
        intake.run(gamepad1);
        spindexer.runIntake(sensor, sensor2);

        //outtake
        outtake.runAutomated(kicker, spindexer, gamepad1);

        //indexer
//        boolean a = gamepad1.a;
//        if (a && !prevA) indexerToggle = !indexerToggle;
//        prevA = a;
//        spindexer.spindexer.setPosition(indexerToggle ? spindexer.indx2 : spindexer.indx3);

        //kicker
//        if(gamepad1.x){
//            kicker.kicker.setPosition(kicker.up);
////            timer.reset();
////            if(timer.seconds()>=0.500){
////                kicker.kicker.setPosition(kicker.down);
////                telemetry.addData("kicker dodwn", "");
////            }
////            if(timer.seconds()>=1.0000){
////                spindexer.spindexer.setPosition(spindexer.indx2);
////                telemetry.addData("indexer moving: ", "");
////            }
//        } else{
//            kicker.kicker.setPosition(kicker.down);
//        }

        telemetry.addData("kicker position: ", kicker.kicker.getPosition());
//        telemetry.addData("time: ", kicker.timer.milliseconds());
        telemetry.addData("outtake velo: ", outtake.outtake.getVelocity());
//        telemetry.addData("timerL ", timer.seconds());
//        telemetry.addData("intake toggle: ", intake.isOn);
//        telemetry.addData("outtake toggle: ", outtake.isOn);
    }

    public double getDistance(){
        double deltaX = follower.getPose().getX();
        double deltaY = 144-follower.getPose().getY();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }
}
