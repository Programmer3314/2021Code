/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

/**
 * Add your docs here.
 */
public class ControlPanel {
  private String desiredColor;
  private String colorString;
  public TalonSRX talon31;
  public boolean reset, inFourSpins;
  public int[][] colorTable = { { 0, 1, 2, -1 }, { -1, 0, 1, 2 }, { 2, -1, 0, 1 }, { 1, 2, -1, 0 } };
  public SetColor currentState, nextState;
  public int destinationColor, FMSDestinationColor, currentColor;
  public int scale, colorCounter, tickMax;
  String gameData;
  private String FMSColor, wantedColor;
  private int FMSColorThrottle;

  public enum SetColor {
    IDLE, START, SPINTOCOLOR, SPININCOLOR, DONE
  }

  // Color Sensor
  // private final I2C.Port i2cPort = I2C.Port.kOnboard;
  // private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private final Color kBlueTarget = ColorMatch.makeColor(0.217, 0.470, 0.312);
  private final Color kGreenTarget = ColorMatch.makeColor(0.237, 0.51, 0.251);
  private final Color kRedTarget = ColorMatch.makeColor(0.315, 0.457, 0.226);
  private final Color kYellowTarget = ColorMatch.makeColor(0.289, 0.518, 0.192);
  private final ColorMatch m_colorMatcher = new ColorMatch();

  public ControlPanel(int talonID) {
    talon31 = new TalonSRX(talonID);

    talon31.configFactoryDefault();
    reset = true;
    inFourSpins = false;

    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);

    currentState = SetColor.IDLE;
    colorCounter = 0;
    desiredColor = "";
    FMSColor = "";
    wantedColor = "";
    FMSColorThrottle = 0;

  }

  public void update() {

    // TODO: review change
    // Only get DriverState instance once in AllRobots
    // And only check for FMSColor if we have never gotten it
    // and only about once a second.
    // gameData = DriverStation.getInstance().getGameSpecificMessage();

    FMSColorThrottle++;
    if ((FMSColorThrottle % 50) == 0) {
      if (FMSColor == "") {
        gameData = Robot.driverStation.getGameSpecificMessage();

        if (gameData.length() > 0) {
          switch (gameData.charAt(0)) {
            case 'B':
              FMSColor = "Blue";
              wantedColor = "Red";
              FMSDestinationColor = 0;
              break;
            case 'G':
              FMSColor = "Green";
              wantedColor = "Yellow";
              FMSDestinationColor = 3;
              break;
            case 'R':
              FMSColor = "Red";
              wantedColor = "Blue";
              FMSDestinationColor = 2;
              break;
            case 'Y':
              FMSColor = "Yellow";
              wantedColor = "Green";
              FMSDestinationColor = 1;
              break;
          }
        }
      }
    }

    // Disable color sensor until lag is isolated or reproducable
    // if (false) {
    // // Color Sensor
    // Color detectedColor = m_colorSensor.getColor();
    // ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

    // if (match.color == kBlueTarget) {
    // colorString = "Blue";
    // currentColor = 2;
    // } else if (match.color == kRedTarget) {
    // colorString = "Red";
    // currentColor = 0;
    // } else if (match.color == kGreenTarget) {
    // colorString = "Green";
    // currentColor = 1;
    // } else if (match.color == kYellowTarget) {
    // colorString = "Yellow";
    // currentColor = 3;
    // } else {
    // colorString = "Unknown";
    // currentColor = -1;
    // }

    // SmartDashboard.putNumber("Red", detectedColor.red);
    // SmartDashboard.putNumber("Green", detectedColor.green);
    // SmartDashboard.putNumber("Blue", detectedColor.blue);
    // SmartDashboard.putNumber("Confidence", match.confidence);
    // SmartDashboard.putString("Detected Color", colorString);
    // }

    nextState = currentState;
    switch (currentState) {
      case IDLE:
        desiredColor = "null";
        destinationColor = -1;
        break;

      case START:
        // if (HumanInput.operatorController.getRawButton(1)) {
        // desiredColor = "Yellow";
        // destinationColor = 3;
        // } else if (HumanInput.operatorController.getRawButton(2)) {
        // desiredColor = "Blue";
        // destinationColor = 2;
        // } else if (HumanInput.operatorController.getRawButton(3)) {
        // desiredColor = "Red";
        // destinationColor = 0;
        // } else if (HumanInput.operatorController.getRawButton(4)) {
        // desiredColor = "Green";
        // destinationColor = 1;
        // } else {
        // desiredColor = "null";
        // destinationColor = -1;
        // }

        // if (!colorString.equalsIgnoreCase(desiredColor) &&
        // !desiredColor.equalsIgnoreCase("null")) {
        // talon31.set(ControlMode.PercentOutput, 0.3);
        // } else {
        // talon31.set(ControlMode.PercentOutput, 0.0);
        // }
        talon31.set(ControlMode.PercentOutput, 0.0);
        if (currentColor >= 0 && destinationColor >= 0) {
          scale = colorTable[destinationColor][currentColor];
          if (scale == 0) {
            nextState = SetColor.DONE;
          } else {
            nextState = SetColor.SPINTOCOLOR;
            if (scale > 0) {
              tickMax = 2800;
            } else {
              tickMax = 3500;
            }
          }
        }
        break;

      case SPINTOCOLOR:
        talon31.set(ControlMode.PercentOutput, (0.30) * Math.signum(scale));
        if (!(colorString.equalsIgnoreCase(desiredColor))) {
          colorCounter = 0;
        } else {
          colorCounter++;
        }
        if (colorCounter >= 5) {
          talon31.setSelectedSensorPosition(0);
          nextState = SetColor.SPININCOLOR;
        }

        break;

      case SPININCOLOR:
        talon31.set(ControlMode.PercentOutput, (0.15) * Math.signum(scale));
        if ((Math.abs(talon31.getSelectedSensorPosition()) <= tickMax)) {

        } else {
          nextState = SetColor.DONE;
        }

        break;

      case DONE:
        talon31.set(ControlMode.PercentOutput, 0.0);
        destinationColor = -1;
        talon31.setSelectedSensorPosition(0);
        nextState = SetColor.IDLE;
        break;
    }

    // Rotation four times
    SmartDashboard.putNumber("CP Motor Speed", 0);
    // if (HumanInput.operatorController.getRawButtonReleased(6)) {
    // inFourSpins = true;
    // talon31.setSelectedSensorPosition(0);
    // }
    if (inFourSpins) {
      if (talon31.getSelectedSensorPosition() <= 412000 * .8) {
        talon31.set(ControlMode.PercentOutput, 1.0);
        SmartDashboard.putNumber("CP Motor Speed", 1.0);
      } else {
        talon31.set(ControlMode.PercentOutput, 0.0);
        inFourSpins = false;
        SmartDashboard.putNumber("CP Motor Speed", -1);
      }
    } else if (HumanInput.CPManipulatorUp) {
      talon31.set(ControlMode.PercentOutput, HumanInput.spinCP * 0.5);
    } else {
      currentState = SetColor.DONE;
      talon31.set(ControlMode.PercentOutput, 0.0);
      inFourSpins = false;
    }

    if (HumanInput.CPManipulatorDown) {
      talon31.set(ControlMode.PercentOutput, 0);
    }

    SmartDashboard.putNumber("Encoder Value", talon31.getSelectedSensorPosition());
    SmartDashboard.putString("Control Panel State", currentState.toString());
    SmartDashboard.putString("Desired Color", desiredColor);
    SmartDashboard.putNumber("Scale", scale);
    SmartDashboard.putNumber("Color Counter", colorCounter);
    SmartDashboard.putString("FMS Color", FMSColor);
    SmartDashboard.putString("Wanted Color", wantedColor);
    currentState = nextState;
  }

  public void spinFourTimes() {
    inFourSpins = true;
    talon31.setSelectedSensorPosition(0);
  }

  public void spinToGreen() {
    desiredColor = "Yellow";
    destinationColor = 3;
    currentState = SetColor.START;
  }

  public void spinToRed() {
    desiredColor = "Blue";
    destinationColor = 2;
    currentState = SetColor.START;
  }

  public void spinToBlue() {
    desiredColor = "Red";
    destinationColor = 0;
    currentState = SetColor.START;
  }

  public void spinToYellow() {
    desiredColor = "Green";
    destinationColor = 1;
    currentState = SetColor.START;
  }

  public void spinToFMSColor() {
    desiredColor = wantedColor;
    destinationColor = FMSDestinationColor;
    currentState = SetColor.START;
  }
}
