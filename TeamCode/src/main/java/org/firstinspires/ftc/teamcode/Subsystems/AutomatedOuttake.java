package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class AutomatedOuttake {

    public DcMotorEx outtake;
    public ElapsedTime timer = new ElapsedTime();
    public double time = 200000;
    public boolean isOn = false;
    public int i;
    public double speed = -1800;

    public AutomatedOuttake(HardwareMap hardwareMap){
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");
        i = 0;
    }

    public void runAutomated(Kicker kicker, Indexer spindexer, Gamepad gm){
        if(gm.y){
            isOn = true;
        }
        if(isOn){
            if(i < 3){
                outtake.setVelocity(speed);
                if(timer.milliseconds()>time+900){
                    spindexer.runOuttake();
                    i += 1;
                    time = 200000;
                } else if(timer.milliseconds()>time+700){
                    kicker.kicker.setPosition(kicker.down);
                } else if(timer.milliseconds()>time+400){
                    kicker.kicker.setPosition(kicker.up);
                } else if(time == 200000){
                    if((outtake.getVelocity()<speed+200)&&(outtake.getVelocity()>speed-200)){
                        time = timer.milliseconds();
                    }
                }
            } else{
                isOn = false;
                outtake.setVelocity(0);
                i = 0;
            }
        }
    }

    public void runAuton(Kicker kicker, Indexer spindexer){
        if(i < 3){
            outtake.setVelocity(speed);
            if(timer.milliseconds()>time+900){
                spindexer.runOuttake();
                i += 1;
                time = 200000;
            } else if(timer.milliseconds()>time+700){
                kicker.kicker.setPosition(kicker.down);
            } else if(timer.milliseconds()>time+400){
                kicker.kicker.setPosition(kicker.up);
            } else if(time == 200000){
                if((outtake.getVelocity()<-1800)&&(outtake.getVelocity()>-2200)){
                    time = timer.milliseconds();
                }
            }
        } else{
            outtake.setVelocity(0);
            i = 0;
        }
    }

    public void singleArtifact(Kicker kicker, Indexer spindexer){
        kicker.kicker.setPosition(kicker.up);
        time = timer.milliseconds();
        if(timer.milliseconds()>time+2000){
            kicker.kicker.setPosition(kicker.down);
            time = timer.milliseconds();
        }
        if(timer.milliseconds()>time+2000){
            spindexer.runOuttake();
            time = 200000;
        }
    }

}
