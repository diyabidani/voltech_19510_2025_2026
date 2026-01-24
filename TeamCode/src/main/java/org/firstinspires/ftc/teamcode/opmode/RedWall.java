package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Subsystems.AutomatedOuttake;
import org.firstinspires.ftc.teamcode.Subsystems.Hood;
import org.firstinspires.ftc.teamcode.Subsystems.Indexer;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Kicker;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Red Wall Auto")
public class RedWall extends OpMode {

    private Follower follower;
    private AutomatedOuttake outtake;
    private Kicker kicker;
    private Indexer spindexer;
    private Hood hood;
    private Intake intake;

    private PathChain Path1;
    private int pathState = 0;

    // MUST match first pose in BezierCurve
    private final Pose startPose = new Pose(87.927, 7.835, Math.toRadians(90));

    @Override
    public void init() {

        follower = Constants.createFollower(hardwareMap);
        outtake = new AutomatedOuttake(hardwareMap);
        kicker = new Kicker(hardwareMap);
        spindexer = new Indexer(hardwareMap);
        hood = new Hood(hardwareMap);
        intake = new Intake(hardwareMap);

        buildPaths();
        follower.setStartingPose(startPose);

        spindexer.setPos1();
        hood.hood.setPosition(hood.up);
        kicker.kicker.setPosition(kicker.down);

        outtake.speed = -2000;
        outtake.i = 0;
    }

    @Override
    public void loop() {
        follower.update();
        runAuton();

        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", follower.getPose().getHeading());
        telemetry.addData("State", pathState);
    }

    public void buildPaths() {
        Path1 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(87.927, 7.835),
                        new Pose(92.689, 100.571),
                        new Pose(105.819, 108.453)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(35))
                .build();
    }

    public void runAuton() {
        switch (pathState) {

            case 0: // Drive the curved path
                follower.followPath(Path1);
                pathState = 1;
                break;

            case 1: // Shoot after robot stops
                if (!follower.isBusy()) {
                    outtake.runAuton(kicker, spindexer);

                    if (outtake.i == 3) {
                        outtake.outtake.setVelocity(0);
                        outtake.i = 0;
                        pathState = 2;
                    }
                }
                break;

            case 2: // Finished
                break;
        }
    }
}