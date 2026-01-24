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


@Autonomous(name = "Blue Depot", group = "Autonomous")
@Configurable // Panels
public class BlueDepot extends OpMode {
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
        follower.setStartingPose(new Pose(22, 126, Math.toRadians(-35)));


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
                            new BezierLine(
                                    new Pose(22.000, 126.000),


                                    new Pose(39.214, 111.703)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(145))


                    .build();


            Path2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(39.214, 111.703),
                                    new Pose(66.514, 78.943),
                                    new Pose(17.402, 83.970)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(145), Math.toRadians(180))


                    .build();


            Path3 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(17.402, 83.970),
                                    new Pose(50.479, 100.752),
                                    new Pose(39.047, 111.888)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(145))


                    .build();


            Path4 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(39.047, 111.888),
                                    new Pose(65.732, 52.958),
                                    new Pose(17.727, 59.807)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(145), Math.toRadians(180))


                    .build();


            Path5 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(17.727, 59.807),
                                    new Pose(57.435, 101.047),
                                    new Pose(38.965, 111.742)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(145))


                    .build();


            Path6 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(38.965, 111.742),
                                    new Pose(80.685, 29.731),
                                    new Pose(17.966, 35.576)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(145), Math.toRadians(180))


                    .build();


            Path7 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(17.966, 35.576),
                                    new Pose(58.915, 98.869),
                                    new Pose(39.023, 112.107)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(145))


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