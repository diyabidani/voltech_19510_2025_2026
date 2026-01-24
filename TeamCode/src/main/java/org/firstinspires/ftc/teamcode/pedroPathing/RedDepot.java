package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Red Depot", group = "Autonomous")
@Configurable // Panels
public class RedDepot extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private DcMotor intakeMotor;
    private DcMotor outtakeMotor;
    private Timer actionTimer;
    private Timer opmodeTimer;
    private boolean intakeOn = false;

    private PathChain path1;
    private PathChain path2;
    private PathChain path3;
    private PathChain path4;
    private PathChain path5;
    private PathChain path6;
    private PathChain path7;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(72, 8, Math.toRadians(90)));

        buildPaths();
        pathState = 0;

        intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");
        outtakeMotor = hardwareMap.get(DcMotor.class, "outtake_motor");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        autonomousPathUpdate(); // Update autonomous state machine

        intakeMotor.setPower(intakeOn ? 1.0 : 0.0);

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("Opmode Timer", opmodeTimer.getElapsedTimeSeconds());
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void stop() {
    }

    public void buildPaths() {
        path1 = follower.pathBuilder().addPath(
                new BezierLine(
                        new Pose(123.000, 123.000),
                        new Pose(106.722, 105.237)
                )
        ).setLinearHeadingInterpolation(Math.toRadians(230), Math.toRadians(410))
                .build();

        path2 = follower.pathBuilder().addPath(
                new BezierCurve(
                        new Pose(106.722, 105.237),
                        new Pose(79.098, 80.021),
                        new Pose(125.247, 83.340)
                )
        ).setLinearHeadingInterpolation(Math.toRadians(410), Math.toRadians(0))
                .build();

        path3 = follower.pathBuilder().addPath(
                new BezierCurve(
                        new Pose(125.247, 83.340),
                        new Pose(103.948, 91.191),
                        new Pose(106.814, 105.247)
                )
        ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(410))
                .build();

        path4 = follower.pathBuilder().addPath(
                new BezierCurve(
                        new Pose(106.814, 105.247),
                        new Pose(88.696, 66.381),
                        new Pose(89.335, 54.052),
                        new Pose(125.485, 59.887)
                )
        ).setLinearHeadingInterpolation(Math.toRadians(410), Math.toRadians(0))
                .build();

        path5 = follower.pathBuilder().addPath(
                new BezierCurve(
                        new Pose(125.485, 59.887),
                        new Pose(93.119, 86.840),
                        new Pose(106.691, 104.990)
                )
        ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(410))
                .build();

        path6 = follower.pathBuilder().addPath(
                new BezierCurve(
                        new Pose(106.691, 104.990),
                        new Pose(66.954, 25.820),
                        new Pose(125.608, 35.660)
                )
        ).setLinearHeadingInterpolation(Math.toRadians(410), Math.toRadians(0))
                .build();

        path7 = follower.pathBuilder().addPath(
                new BezierCurve(
                        new Pose(125.608, 35.660),
                        new Pose(84.222, 90.253),
                        new Pose(106.814, 105.175)
                )
        ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(410))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start Path 1
                follower.followPath(path1);
                setPathState(1);
                break;
            case 1: // Path 1 finished, wait 5s with outtake
                if (!follower.isBusy()) {
                    outtakeMotor.setPower(1.0);
                    setPathState(2);
                }
                break;
            case 2: // 5s up, start Path 2 with intake
                if (actionTimer.getElapsedTimeSeconds() >= 5) {
                    outtakeMotor.setPower(0.0);
                    follower.followPath(path2);
                    intakeOn = true;
                    setPathState(3);
                }
                break;
            case 3: // Path 2 finished, start Path 3, turn off intake
                if (!follower.isBusy()) {
                    intakeOn = false;
                    follower.followPath(path3);
                    setPathState(4);
                }
                break;
            case 4: // Path 3 finished, wait 5s with outtake
                if (!follower.isBusy()) {
                    outtakeMotor.setPower(1.0);
                    setPathState(5);
                }
                break;
            case 5: // 5s up, start Path 4 with intake, turn off outtake
                if (actionTimer.getElapsedTimeSeconds() >= 5) {
                    outtakeMotor.setPower(0.0);
                    follower.followPath(path4);
                    intakeOn = true;
                    setPathState(6);
                }
                break;
            case 6: // Path 4 finished, start Path 5
                if (!follower.isBusy()) {
                    follower.followPath(path5);
                    setPathState(7);
                }
                break;
            case 7: // Path 5 finished, wait 5s with outtake
                if (!follower.isBusy()) {
                    outtakeMotor.setPower(1.0);
                    setPathState(8);
                }
                break;
            case 8: // 5s up, start Path 6 with intake and outtake
                if (actionTimer.getElapsedTimeSeconds() >= 5) {
                    outtakeMotor.setPower(1.0); // Keep outtake on as requested
                    follower.followPath(path6);
                    intakeOn = true;
                    setPathState(9);
                }
                break;
            case 9: // Path 6 finished, start Path 7
                if (!follower.isBusy()) {
                    follower.followPath(path7);
                    setPathState(10);
                }
                break;
            case 10: // Path 7 finished, wait 5s with outtake
                if (!follower.isBusy()) {
                    outtakeMotor.setPower(1.0);
                    setPathState(11);
                }
                break;
            case 11: // 5s up, stop motors
                if (actionTimer.getElapsedTimeSeconds() >= 5) {
                    outtakeMotor.setPower(0.0);
                    intakeOn = false;
                    setPathState(12);
                }
                break;
            case 12: // Autonomous is finished
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        actionTimer.resetTimer();
    }
}
