package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.teamcode.Subsystems.AutomatedOuttake;
import org.firstinspires.ftc.teamcode.Subsystems.Hood;
import org.firstinspires.ftc.teamcode.Subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Kicker;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class BlueTop extends OpMode {

    private Follower follower;
    private AutomatedOuttake outtake;
    private Kicker kicker;
    private Indexer spindexer;
    private Hood hood;
    private int pathState = 0;
    private Intake intake;
    private DistanceSensor sensor, sensor2;

    private final Pose startPose = new Pose(20,125,Math.toRadians(-40));
    private final Pose shootingPose = new Pose(-8,154,Math.toRadians(-40));
    private PathChain preloaded;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        outtake = new AutomatedOuttake(hardwareMap);
        kicker = new Kicker(hardwareMap);
        spindexer = new Indexer(hardwareMap);
        hood = new Hood(hardwareMap);
        intake = new Intake(hardwareMap);
        sensor = hardwareMap.get(DistanceSensor.class, "sensor");
        sensor2 = hardwareMap.get(DistanceSensor.class, "sensor2");
        spindexer.setPos1();
        hood.hood.setPosition(hood.up);
        kicker.kicker.setPosition(kicker.down);
        buildPaths();
        follower.setStartingPose(startPose);
        outtake.speed = -2000;
        outtake.i = 0;
        telemetry.addData("pose: ", follower.getPose());
    }

    @Override
    public void loop() {
        follower.update();
        runAuton();
        telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("outtake velo: ", outtake.outtake.getVelocity());
        telemetry.addData("pathstate: ", pathState);
        telemetry.addData("timer: ", outtake.timer.milliseconds());
        telemetry.addData("time: ", outtake.time);
    }

    public void buildPaths() {
        preloaded = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootingPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootingPose.getHeading())
                .build();
    }

    public void runAuton(){
        switch(pathState) {
            case 0:
                follower.followPath(preloaded);
                pathState = 1;
                break;
            case 1:
                outtake.runAuton(kicker, spindexer);
                if (outtake.i == 3) {
                    pathState = 2;
                    outtake.i = 0;
                    outtake.outtake.setVelocity(0);
                }
                break;
        }
    }
}
