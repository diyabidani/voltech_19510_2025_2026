package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.AutomatedOuttake;
import org.firstinspires.ftc.teamcode.Subsystems.Hood;
import org.firstinspires.ftc.teamcode.Subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Kicker;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class Blue extends OpMode {

    private Follower follower;
    private Paths paths;
    private int pathState;
    private Intake intake;
    private AutomatedOuttake outtake;
    private Indexer spindexer;
    private Kicker kicker;
    private DistanceSensor sensor, sensor2;
    private Hood hood;

    public static class Paths {
        public PathChain preloaded;
        public PathChain intakesetup1;
        public PathChain intake1;
        public PathChain shootsetup1;

        public Paths(Follower follower) {
            preloaded = follower.pathBuilder().addPath(new BezierLine(new Pose(20.000, 125.000), new Pose(48.000, 96.000)))
                    .setLinearHeadingInterpolation(Math.toRadians(140), Math.toRadians(140))
                    .build();
            intakesetup1 = follower.pathBuilder().addPath(new BezierLine(new Pose(48.000, 96.000), new Pose(56.000, 84.000)))
                    .setLinearHeadingInterpolation(Math.toRadians(140), Math.toRadians(180))
                    .build();
            intake1 = follower.pathBuilder().addPath(new BezierLine(new Pose(56.000, 84.000), new Pose(14.000, 84.000)))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();
            shootsetup1 = follower.pathBuilder().addPath(new BezierLine(new Pose(14.000, 84.000), new Pose(48.000, 96.000)))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(140))
                    .build();
        }
    }

    public void updateAuton(){
        switch(pathState){
            case 0:
                follower.followPath(paths.preloaded);
                if(!follower.isBusy()){
                    outtake.runAuton(kicker, spindexer);
                }
                pathState = 1;
                break;
//            case 1:
//                follower.followPath(paths.intakesetup1);
//                intake.intake.setPower(0.6);
//                pathState = 2;
//                break;
//            case 2:
//                follower.followPath(paths.intake1);
//                if(!follower.isBusy()){
//                    intake.intake.setPower(0);
//                }
//                pathState = 3;
//                break;
//            case 3:
//                follower.followPath(paths.shootsetup1);
//                if(!follower.isBusy()){
//                    outtake.runAuton(kicker, spindexer);
//                }
//                break;
        }
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        paths = new Paths(follower);
        intake = new Intake(hardwareMap);
        outtake = new AutomatedOuttake(hardwareMap);
        spindexer = new Indexer(hardwareMap);
        kicker = new Kicker(hardwareMap);
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
        sensor2 = hardwareMap.get(DistanceSensor.class, "sensor2");
        hood = new Hood(hardwareMap);
        spindexer.setPos1();
        hood.hood.setPosition(hood.up);
        follower.setStartingPose(new Pose(20,125,140));
    }

    @Override
    public void loop() {
        follower.update();
        updateAuton();
        telemetry.addData("pathstate: ", pathState);
        telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        spindexer.runIntake(sensor, sensor2);
    }
}
