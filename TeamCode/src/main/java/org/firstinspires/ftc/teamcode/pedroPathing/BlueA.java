package org.firstinspires.ftc.teamcode.pedroPathing;

import com.google.blocks.ftcrobotcontroller.util.ToolboxUtil;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.teamcode.Subsystems.AutomatedOuttake;
import org.firstinspires.ftc.teamcode.Subsystems.Hood;
import org.firstinspires.ftc.teamcode.Subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Kicker;
import org.opencv.core.Mat;

@Autonomous
public class BlueA extends OpMode {

    private Follower follower;
    private AutomatedOuttake outtake;
    private Kicker kicker;
    private Indexer spindexer;
    private Hood hood;
    private int pathState = 0;
    private Intake intake;
    private DistanceSensor sensor, sensor2;

    private final Pose startPose = new Pose(20,125,Math.toRadians(-40));
    private final Pose shootingPose = new Pose(10,150,Math.toRadians(-40));
    private final Pose shootingPose2 = new Pose(10,150,Math.toRadians(-40));
    private final Pose artifacts1 = new Pose(0,166, Math.toRadians(0));
    private final Pose intaking1 = new Pose(25, 166, Math.toRadians(0));
    private final Pose artifacts2 = new Pose();
    private final Pose artifacts3 = new Pose();

    private PathChain preloaded, intakesetup1, intake1, shooting1;

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
        spindexer.runIntake(sensor, sensor2);
        telemetry.addData("x: ", follower.getPose().getX());
        telemetry.addData("y: ", follower.getPose().getY());
        telemetry.addData("heading: ", follower.getPose().getHeading());
        telemetry.addData("outtake velo: ", outtake.outtake.getVelocity());
        telemetry.addData("pathstate: ", pathState);
        telemetry.addData("timer: ", outtake.timer.milliseconds());
        telemetry.addData("time: ", outtake.time);
    }

    public void buildPaths(){
        preloaded = follower.pathBuilder()
                .addPath(new BezierLine(startPose,shootingPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootingPose.getHeading())
                .build();
        intakesetup1 = follower.pathBuilder()
                .addPath(new BezierLine(shootingPose,artifacts1))
                .setLinearHeadingInterpolation(shootingPose.getHeading(), artifacts1.getHeading())
                .build();
        intake1 = follower.pathBuilder()
                .addPath(new BezierLine(artifacts1, intaking1))
                .setLinearHeadingInterpolation(artifacts1.getHeading(),intaking1.getHeading())
                .build();
        shooting1 = follower.pathBuilder()
                .addPath(new BezierLine(intaking1, shootingPose2))
                .setLinearHeadingInterpolation(intaking1.getHeading(), shootingPose2.getHeading())
                .build();
    }

    public void runAuton(){
        switch(pathState){
            case 0:
                follower.followPath(preloaded);
                pathState = 1;
                break;
            case 1:
                outtake.runAuton(kicker, spindexer);
                if(outtake.i==3){
                    pathState = 2;
                    outtake.i = 0;
                    outtake.outtake.setVelocity(0);
                }
                break;
            case 2:
                follower.followPath(intakesetup1);
                intake.intake.setPower(0.6);
                pathState = 3;
                break;
            case 3:
                follower.followPath(intake1);
                pathState = 4;
                break;
            case 4:
                intake.intake.setPower(0);
                follower.followPath(shooting1);
                pathState = 5;
                break;
        }
    }
}
