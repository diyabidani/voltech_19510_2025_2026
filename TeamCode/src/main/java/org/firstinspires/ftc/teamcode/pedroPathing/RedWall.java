package org.firstinspires.ftc.teamcode.pedroPathing;


import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Red Wall", group = "Autonomous")
@Configurable // Panels
public class RedWall extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class
    private DcMotor intakeMotor;
    private DcMotor outtakeMotor;
    private ElapsedTime timer;
    private boolean intakeOn = false;


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();


        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(87.927, 7.835, Math.toRadians(90)));


        paths = new Paths(follower); // Build paths
        pathState = 0;


        intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");
        outtakeMotor = hardwareMap.get(DcMotor.class, "outtake_motor");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        outtakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        timer = new ElapsedTime();


        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }


    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        pathState = autonomousPathUpdate(); // Update autonomous state machine


        intakeMotor.setPower(intakeOn ? 1.0 : 0.0);


        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }




    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain Path6;
        public PathChain Path7;


        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(87.927, 7.835),
                                    new Pose(92.689, 100.571),
                                    new Pose(105.819, 108.453)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(35))


                    .build();


            Path2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(105.819, 108.453),
                                    new Pose(75.842, 80.159),
                                    new Pose(126.075, 83.489)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(360))


                    .build();


            Path3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(126.075, 83.489),
                                    new Pose(90.700, 95.301),
                                    new Pose(105.683, 108.657)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(35))


                    .build();


            Path4 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(105.683, 108.657),
                                    new Pose(69.699, 53.423),
                                    new Pose(126.457, 59.639)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(360))


                    .build();


            Path5 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(126.457, 59.639),
                                    new Pose(87.821, 95.491),
                                    new Pose(105.721, 108.533)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(35))


                    .build();


            Path6 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(105.721, 108.533),
                                    new Pose(57.496, 28.605),
                                    new Pose(126.345, 35.521)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(360))


                    .build();


            Path7 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(126.345, 35.521),
                                    new Pose(85.331, 94.419),
                                    new Pose(105.563, 108.579)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(35))


                    .build();
        }
    }




    public int autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start Path 1
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path1);
                    pathState++;
                }
                break;
            case 1: // Path 1 finished, wait 5s with outtake
                if (!follower.isBusy()) {
                    outtakeMotor.setPower(1.0);
                    timer.reset();
                    pathState++;
                }
                break;
            case 2: // 5s up, start Path 2 with intake
                if (timer.seconds() >= 5) {
                    outtakeMotor.setPower(0.0);
                    follower.followPath(paths.Path2);
                    intakeOn = true;
                    pathState++;
                }
                break;
            case 3: // Path 2 finished, start Path 3, turn off intake
                if (!follower.isBusy()) {
                    intakeOn = false;
                    follower.followPath(paths.Path3);
                    pathState++;
                }
                break;
            case 4: // Path 3 finished, wait 5s with outtake
                if (!follower.isBusy()) {
                    outtakeMotor.setPower(1.0);
                    timer.reset();
                    pathState++;
                }
                break;
            case 5: // 5s up, start Path 4 with intake, turn off outtake
                if (timer.seconds() >= 5) {
                    outtakeMotor.setPower(0.0);
                    follower.followPath(paths.Path4);
                    intakeOn = true;
                    pathState++;
                }
                break;
            case 6: // Path 4 finished, start Path 5
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path5);
                    pathState++;
                }
                break;
            case 7: // Path 5 finished, wait 5s with outtake
                if (!follower.isBusy()) {
                    outtakeMotor.setPower(1.0);
                    timer.reset();
                    pathState++;
                }
                break;
            case 8: // 5s up, start Path 6 with intake and outtake
                if (timer.seconds() >= 5) {
                    outtakeMotor.setPower(1.0); // Keep outtake on as requested
                    follower.followPath(paths.Path6);
                    intakeOn = true;
                    pathState++;
                }
                break;
            case 9: // Path 6 finished, start Path 7
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path7);
                    pathState++;
                }
                break;
            case 10: // Path 7 finished, wait 5s with outtake
                if (!follower.isBusy()) {
                    outtakeMotor.setPower(1.0);
                    timer.reset();
                    pathState++;
                }
                break;
            case 11: // 5s up, stop motors
                if (timer.seconds() >= 5) {
                    outtakeMotor.setPower(0.0);
                    intakeOn = false;
                    pathState++;
                }
                break;
            case 12: // Autonomous is finished
                break;
        }
        return pathState;
    }
}
