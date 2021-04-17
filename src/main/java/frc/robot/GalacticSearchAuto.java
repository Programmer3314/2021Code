// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.Thread.State;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.DriveController.DriveState;
import frc.robot.DriveController.MoveParameters;

//Start intake
//Waypoint Distance to D5, second ball
//Waypoint gyro to angle towards A6, 3rd ball
//Waypoint Distance to A6
//Waypoint Gyro to angle towards endzone
//Waypoint Distance to Endzone, B11-D11 

//Path A- 153328 ticks
//Path B- 184620 ticks

public class GalacticSearchAuto implements AutoStateMachines{

    public enum States{
        START,
        GRAB_BALL1,
        GRAB_BALL2,
        TURN_TO_BALL_A,
        GRAB3RD_SLOW_A,
        GRAB3RD_FAST_A,
        TURN_TO_END_A,
        RUN_A,
        TURN_TO_BALL_B,
        GRAB3RD_SLOW_B,
        GRAB3RD_FAST_B,
        TURN_TO_END_B,
        RUN_B,
        DONE
    }
    States currentState = States.START;
    int ballCounter = 0;
    boolean ballSensorPrevious;
    double lastEncoderVal;
    boolean pathA;

    @Override
    public void update(MoveParameters mP) {
        switch(currentState){
            case START:
                Robot.shooter.groundIntakeAll();
                mP.forward = -0.2;
                mP.turn = 0;
                mP.currentState = DriveState.BALLCHASE;
                lastEncoderVal = Robot.driveController.encoderPos;

                currentState = States.GRAB_BALL1;
            break;
                
            case GRAB_BALL1:
                mP.forward = -0.5;
                mP.turn = 0;
                mP.currentState = DriveState.BALLCHASE;

                 
                if(SensorInput.queuedTrack1 == true && ballSensorPrevious == false){
                    ballCounter++;
                }

                if(ballCounter == 1){
                    currentState = States.GRAB_BALL2;
                }
            break;
        
            case GRAB_BALL2:
                mP.forward = -0.5;
                mP.turn = 0;
                mP.currentState = DriveState.BALLCHASE;

                 
                if(SensorInput.queuedIntake){
                    SmartDashboard.putNumber("Encoder at Ball 2",(Robot.driveController.encoderPos - lastEncoderVal));
                    pathA = ((Robot.driveController.encoderPos - lastEncoderVal) < 170000);
                    
                    if(pathA){
                        currentState = States.TURN_TO_BALL_A;
                    }else{
                        currentState = States.TURN_TO_BALL_B;
                    }
                }

                // if(SensorInput.queuedTrack1 == true && ballSensorPrevious == false){
                //     ballCounter++;
                // }

                // if(ballCounter == 2){
                //     currentState = States.TURN_TO_BALL;
                // }
            break;

            
            
            case TURN_TO_BALL_A:
                mP.forward = 0;
                mP.turn = 0;
                mP.angle = -90;
                mP.currentState = DriveState.TURN_TO_GYRO;

                if(Robot.cleanGyro <= -65){
                    lastEncoderVal = Robot.driveController.encoderPos;
                    currentState = States.GRAB3RD_FAST_A;
                }
            break;

            case GRAB3RD_FAST_A:
                mP.forward = -0.5;
                mP.turn = 0;
                mP.currentState = DriveState.BALLCHASE;

                if((Robot.driveController.encoderPos - lastEncoderVal) / Constants.encoderTicksToFeet <= -4){
                    currentState = States.GRAB3RD_SLOW_A;
                }
                
            break;
            
            case GRAB3RD_SLOW_A:
                mP.forward = -0.3;
                mP.turn = 0;
                mP.currentState = DriveState.BALLCHASE;

                if(SensorInput.queuedTrack1 == true && ballSensorPrevious == false){
                    ballCounter++;
                }

                if(ballCounter == 2){
                    currentState = States.TURN_TO_END_A;
                }
            break;
            
            case TURN_TO_END_A:
                mP.forward = 0;
                mP.turn = 0;
                mP.angle = -32;
                mP.currentState = DriveState.TURN_TO_GYRO;

                if(Robot.cleanGyro >= -32){
                    lastEncoderVal = Robot.driveController.encoderPos;
                    currentState = States.RUN_A;
                }
            break;
            
            case RUN_A:
                mP.forward = -0.6;
                mP.turn = 0;
                mP.currentState = DriveState.GYROLOCK;

                if((Robot.driveController.encoderPos - lastEncoderVal) / Constants.encoderTicksToFeet <= -13){
                    currentState = States.DONE;
                }
            break;
            
            case TURN_TO_BALL_B:
            mP.forward = 0;
            mP.turn = 0;
            mP.angle = -90;
            mP.currentState = DriveState.TURN_TO_GYRO;

            if(Robot.cleanGyro <= -55){
                lastEncoderVal = Robot.driveController.encoderPos;
                currentState = States.GRAB3RD_FAST_B;
            }
        break;

        case GRAB3RD_FAST_B:
            mP.forward = -0.5;
            mP.turn = 0;
            mP.currentState = DriveState.BALLCHASE;

            if((Robot.driveController.encoderPos - lastEncoderVal) / Constants.encoderTicksToFeet <= -4){
                currentState = States.GRAB3RD_SLOW_B;
            }
            
        break;
        
        case GRAB3RD_SLOW_B:
            mP.forward = -0.3;
            mP.turn = 0;
            mP.currentState = DriveState.BALLCHASE;

            if(SensorInput.queuedTrack1 == true && ballSensorPrevious == false){
                ballCounter++;
            }

            if(ballCounter == 2){
                currentState = States.TURN_TO_END_B;
            }
        break;
        
        case TURN_TO_END_B:
            mP.forward = 0;
            mP.turn = 0;
            mP.angle = -60;
            mP.currentState = DriveState.TURN_TO_GYRO;

            if(Robot.cleanGyro >= -60){
                lastEncoderVal = Robot.driveController.encoderPos;
                currentState = States.RUN_B;
            }
        break;
        
        case RUN_B:
            mP.forward = -0.6;
            mP.turn = 0;
            mP.currentState = DriveState.GYROLOCK;

            if((Robot.driveController.encoderPos - lastEncoderVal) / Constants.encoderTicksToFeet <= -9){
                currentState = States.DONE;
            }
        break;

            case DONE:
                mP.forward = 0;
                mP.turn = 0;
                break;
            
        }
        ballSensorPrevious = SensorInput.queuedTrack1;

        SmartDashboard.putString("Galactic States", currentState.toString());
        SmartDashboard.putBoolean("Ball Sensor", SensorInput.queuedTrack1);
        SmartDashboard.putBoolean("Galactic Path A", pathA);
        SmartDashboard.putNumber("Galactic Ball Counter", ballCounter);
        SmartDashboard.putNumber("Galactic Run Feet", (Robot.driveController.encoderPos - lastEncoderVal) / Constants.encoderTicksToFeet);
    }

    @Override
    public void activate() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void LogHeader() {
        // TODO Auto-generated method stub

    }

    @Override
    public void LogData() {
        // TODO Auto-generated method stub

    }


}