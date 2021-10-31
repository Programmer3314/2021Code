package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Climber.ClimbStates;

public class Robot extends MyRobot {
  NetworkTable ballTargetTable;
  NetworkTable portalTapeTargetTable;
  ControlPanelAlignment trenchAlignment;
  AutoStateMachines auto1;
  boolean isForward = true;
  int driverCamNum = 0;
  int operatorCamNum = 0;
  double queuingBeltSpeed;
  boolean useGyro;
  double angleOffset;
  double gyroTolerance, gyroAngleDesired;
  DriveController.MoveParameters mP;
  boolean toggleLightRing = false;
  Climber climber = new Climber();
  int LEDCounter;
  double displacementX;
  double displacementY;
  public static double vertAngle;

  @Override
  public void RechargeRobotInit() {
    Logger.Enabled = true;

    Solenoids.ejectIntake(false);

    ballTargetTable = ntInst.getTable("Ball Target");
    portalTapeTargetTable = ntInst.getTable("Retroreflective Tape Target");

    // Ensure that switched camera entry exists
    ntInst.getEntry("chooseCam").setNumber(0);
    ntInst.getEntry("PumpkinSwitch").setNumber(0);

    driveController = new DriveController(drivetrain, ballTargetTable, portalTapeTargetTable);

    if (hasShooter) {
      shooter = new Shooter(CANMcshooterLeft, CANMcshooterRight, CANMcBallQueuing, CANMcHood, CANMcIndexer,
          CANMcIntake);
    }

    shooter.setTargetShooterRPMTolerance(Constants.shooterRPMTolerance);

    if (isTalonFXTest) {
      fxTest = new TalonFXTest();
    }

    // TODO: revert later
    // Commented due to lag issue
    if (hasControlPanel) {
      controlPanel = new ControlPanel(CANMcctrlPanel);
    }
    trenchAlignment = new ControlPanelAlignment();

    // targetShooterRPM = SmartDashboard.getNumber("Shooter RPM Desired", 0);
    // shooterRPMTolerance = SmartDashboard.getNumber("Shooter RPM Tolerance
    // Desired", 0);
    // SmartDashboard.putNumber("Shooter RPM Desired", targetShooterRPM);
    SmartDashboard.putNumber("Shooter RPM Tolerance Desired", shooter.getTargetShooterRPMTolerance());

    queuingBeltSpeed = Constants.queuingBeltSpeed; // SmartDashboard.getNumber("Queuing Belt Speed", 0.5);
    SmartDashboard.putNumber("Queuing Belt Speed", queuingBeltSpeed);

    Solenoids.startCompressor();

  }

  @Override
  public void RechargeAutonomousInit() {
    shooter.autoCounter = 3;
    HumanInput.update();

    // set auto
    auto1 = null;
    if (HumanInput.autoNumber == 2) {
      auto1 = new ThreeBallAutoNoAim(shooter);
    }
    if (HumanInput.autoNumber == 3) {
      auto1 = new ThreeBallAuto(shooter);
    }

    if (HumanInput.autoNumber == 4) {
      auto1 = new ThreeBallAutoBack(shooter);
    }

    if (HumanInput.autoNumber == 5) {
      auto1 = new FiveBallAutoTrench();
    }

    if (HumanInput.autoNumber == 6) {
      auto1 = new SixBallAuto();
    }

    if (HumanInput.autoNumber == 7) {
      auto1 = new BounceAuto();
    }

    if (HumanInput.autoNumber == 8) {
      auto1 = new BarrelRunAuto();
    }

    if (HumanInput.autoNumber == 9) {
      auto1 = new GalacticSearchAuto();
    }
    // if(HumanInput.autoNumber == 9){
    // auto1 = new SlalomAuto();
    // }

    mP = driveController.new MoveParameters();
    navx.reset();

    SmartDashboard.putNumber("Auto Number", HumanInput.autoNumber);

    SensorInput.LogHeader();
    shooter.LogHeader();
    mP.LogHeader();
    driveController.LogHeader();
    if (auto1 != null) {
      auto1.LogHeader();
    }
    Solenoids.LogHeader();
    LogHeader();

    Solenoids.targettingLightRing(true);
    Solenoids.ejectIntake(false);

    Solenoids.intakeOut.set(false);
    Solenoids.intakeIn.set(true);
    Solenoids.disengageRatchet.set(false);
    Solenoids.engageRatchet.set(true);
    Solenoids.disengagePTO.set(true);
    Solenoids.engagePTO.set(false);
    Solenoids.CPManipulatorDown.set(true);
    Solenoids.CPManipulatorUp.set(false);

    shooter.resetState();

    if (auto1 != null) {
      auto1.reset();
    }

    trenchAlignment.resetState();

    shooter.setHoodSetpoint(0.0);

    if (auto1 != null) {
      auto1.activate();
    }

    isForward = true;
    driverCamNum = 1;

  }

  @Override
  public void RechargeAutonomousPeriodic() {
    vertAngle = -portalTapeTargetTable.getEntry("Vertical Angle").getDouble(-100);
    vertAngle += 8;

    SensorInput.update();
    SensorInput.LogData();
    shooter.LogData();
    mP.LogData();
    driveController.LogData();
    if (auto1 != null) {
      auto1.LogData();
    }
    Solenoids.LogData();
    LogData();

    if (auto1 != null) {
      auto1.update(mP);
    }

    SmartDashboard.putNumber("Vertical Angle", vertAngle);

    shooter.update(mP);
    driveController.update(mP);
  }

  @Override
  public void RechargeTeleopInit() {
    shooter.autoCounter = 5;
    navx.reset();
    navx.resetDisplacement();

    displacementX = 0;
    displacementY = 0;

    mP = driveController.new MoveParameters();
    // SmartDashboard.putNumber("Target Offset", Constants.targettingOffset);

    // Log Data
    HumanInput.LogHeader();
    SensorInput.LogHeader();
    shooter.LogHeader();
    mP.LogHeader();
    driveController.LogHeader();
    Solenoids.LogHeader();
    LogHeader();

    Solenoids.targettingLightRing(true);

    Solenoids.intakeOut.set(false);
    Solenoids.intakeIn.set(true);
    Solenoids.disengageRatchet.set(false);
    Solenoids.engageRatchet.set(true);
    Solenoids.disengagePTO.set(true);
    Solenoids.engagePTO.set(false);
    Solenoids.CPManipulatorDown.set(true);
    Solenoids.CPManipulatorUp.set(false);

    Solenoids.backLED.set(false);
    Solenoids.frontLED.set(false);

    LEDCounter = 0;

    // Already done in Robot Init...
    // if (hasControlPanel) {
    // controlPanel = new ControlPanel(CANMcctrlPanel);
    // }

    mP.currentState = DriveController.DriveState.MANUAL;

    if (HumanInput.autoNumber == 3) {
      auto1 = new ThreeBallAuto(shooter);
    }

    if (HumanInput.autoNumber == 4) {
      auto1 = new ThreeBallAutoBack(shooter);
    }

    trenchAlignment.resetState();
    shooter.resetState();

    if (auto1 != null) {
      auto1.reset();
    }

    Solenoids.disengagePTO();
    Solenoids.startCompressor();

    shooter.setHoodSetpoint(0.0);
    shooter.homedHood = false;

    isForward = true;
    driverCamNum = 1;
    operatorCamNum = 0;
  }

  @Override
  public void RechargeTeleopPeriodic() {

    // LEDCounter++;

    // if(LEDCounter >= 4750){
    // Solenoids.backLED(true);
    // Solenoids.frontLED(true);
    // }
    vertAngle = -portalTapeTargetTable.getEntry("Vertical Angle").getDouble(-100);
    vertAngle += 8;

    HumanInput.update();
    Solenoids.update();
    SensorInput.update();

    // Log Data
    HumanInput.LogData();
    SensorInput.LogData();
    shooter.LogData();
    mP.LogData();
    driveController.LogData();
    Solenoids.LogData();
    LogData();

    if (HumanInput.closeShot) {
      shooter.setTargetShooterRPMTolerance(50);// 10);
      shooter.setHoodSetpoint(0);// 0
      // shooter.setHoodSetpoint(-800);//0
      shooter.setTargetShooterRPM(3000);// 2150
      shooter.prepareShooter();
      // targetShooterRPM = 2100
    }

    if (HumanInput.lineShot) {
      shooter.setTargetShooterRPMTolerance(50);// 10);//5);
      shooter.setHoodSetpoint(-1360);// -1355);//-1350);//-1310);//-1350);//-1400);
      shooter.setTargetShooterRPM(3314);// 3500);
      shooter.prepareShooter();
      // targetShooterRPM = 3600;

    }

    if (HumanInput.trenchShot) {
      shooter.setTargetShooterRPMTolerance(50);// 10);//5);
      shooter.setHoodSetpoint(-1565);// -1575);//1500//-1350); //-1475);
      shooter.setTargetShooterRPM(3700); // 3800 //5200);
      shooter.prepareShooter();
      // targetShooterRPM = 3600;
    }

    if (HumanInput.farShot) {
      shooter.setTargetShooterRPMTolerance(50);// 5);//10);
      shooter.setHoodSetpoint(-1620);// 1550//-1350); //-1475);
      shooter.setTargetShooterRPM(3900);// 3800);//3700); // 3800 //5200);
      shooter.prepareShooter();
      // shooter.setHoodSetpoint(-1350);//-1450);
      // shooter.setTargetShooterRPM(3700);//4400);
      // shooter.prepareShooter();
      // targetShooterRPM = 4400;
    }

    SmartDashboard.putNumber("Target Shooter RPM:", shooter.getTargetShooterRPM());

    if (HumanInput.hoodUp) {
      shooter.setHoodSetpoint(shooter.getHoodSetpoint() + 25);// hood.set(ControlMode.PercentOutput, -0.1);
    } else if (HumanInput.hoodDown) {
      shooter.setHoodSetpoint(shooter.getHoodSetpoint() - 25);// hood.set(ControlMode.PercentOutput, 0.1);
    } else if (HumanInput.hoodUpReleased || HumanInput.hoodDownReleased) {
      // hood.set(ControlMode.PercentOutput, 0);
      // hoodSetpoint = hoodEncoder;
    }

    if (HumanInput.ballChaseButton) {
      mP.currentState = DriveController.DriveState.BALLCHASE;
    } else if (HumanInput.shooterAllInTarget) {
      // shooterRPMTolerance = SmartDashboard.getNumber("Shooter RPM Tolerance
      // Desired", 0);
      // queuingBeltSpeed = SmartDashboard.getNumber("Queuing Belt Speed", 0.5);
      useGyro = false;
      gyroTolerance = SmartDashboard.getNumber("Gyro Tolerance", 3);
      angleOffset = portalTapeTargetTable.getEntry("X Angle").getDouble(0);
      portalTapeTargetTable.getEntry("gyro").setDouble(Robot.rawGyro);
      angleOffset += Robot.rawGyro;
      gyroAngleDesired = angleOffset;

      shooter.autoCounter = 5;
      shooter.shootAll(/* targetShooterRPM, shooterRPMTolerance, */queuingBeltSpeed, useGyro, gyroAngleDesired,
          gyroTolerance);
    } else if (HumanInput.trenchRunAlignment) {
      mP.currentState = DriveController.DriveState.TRENCHRUNALIGNMENT;
    } else if (HumanInput.powerPortAlignment) {
      mP.currentState = DriveController.DriveState.POWERPORTALIGNMENT;
      // mP.currentState = DriveController.DriveState.SHOOTERPOWERPORTALIGNMENT;
    } else if (HumanInput.climbAlignmentButton) {
      mP.currentState = DriveController.DriveState.CLIMBALIGNMENT;
    } else if (HumanInput.gyroLock) {
      mP.currentState = DriveController.DriveState.GYROLOCK;
    } else if (HumanInput.controlPanelAlignment) {
      trenchAlignment.activate();
      // Commented due to lag issue
      // } else if (HumanInput.activateAuto) {
      // SmartDashboard.putString("In active Auto", "Yes");

      // if(auto1 != null){
      // auto1.activate();
      // }

      // if(auto1 != null){
      // auto1.update(mP);
      // }

      // } else if (HumanInput.shutDownAuto) {
      // SmartDashboard.putString("In Active Auto", "No");
      // mP.currentState = DriveController.DriveState.NONE;

      // if(auto1 != null){
      // auto1.reset();
      // }
    } else if (HumanInput.abortIntake) {
      shooter.abortIntake();
    } else if (HumanInput.lightRing) {
      toggleLightRing = !toggleLightRing;
      Solenoids.targettingLightRing(toggleLightRing);
    } else {
      mP.currentState = DriveController.DriveState.MANUAL;
    }

    mP.forward = -HumanInput.forward;// HumanInput.forward;
    mP.turn = HumanInput.turn;

    mP.driverCameraToggle = HumanInput.driverCameraChange;

    if (mP.driverCameraToggle) {
      isForward = !isForward;

      if (isForward) {
        driverCamNum = 1;
        // mP.forward *= 1;
      } else {
        driverCamNum = 0;
      }

      Robot.ntInst.getEntry("chooseCam").setNumber(driverCamNum);
      // Robot.ntInst.getEntry("PumpkinSwitch").setNumber(1);
    }

    // if (!isForward) {
    // mP.forward *= -1;
    // }

    if (HumanInput.operatorCameraChange) {

      if (operatorCamNum == 0) {
        operatorCamNum = 1;
        // mP.forward *= 1;
      } else {
        operatorCamNum = 0;
      }

      Robot.ntInst.getEntry("PumpkinSwitch").setNumber(operatorCamNum);
      // Robot.ntInst.getEntry("PumpkinSwitch").setNumber(1);
    }

    trenchAlignment.update(mP);

    // Commented due to lag issue
    // if(auto1 != null){
    // auto1.update(mP);
    // }

    if (HumanInput.activateGroundIntake && !shooter.getShooterStatus()) {
      shooter.groundIntakeAll();
    }

    if (HumanInput.activateIntake && !shooter.getShooterStatus()) {
      shooter.intakeAll();
    }

    if (hasShooter) {
      shooter.update(mP);
    }
    if (HumanInput.spinIntake) {
      shooter.intake.set(ControlMode.PercentOutput, 1.0);
    }

    if (HumanInput.reverseIntake) {
      shooter.reverseIntake();
    } else if (HumanInput.reverseIntakeReleased) {
      shooter.reverseIntakeRelease();
    }

    if (HumanInput.reverseAll) {
      shooter.reverseAll();
    } else if (HumanInput.reverseAllReleased) {
      shooter.reverseAllRelease();
    }

    if (HumanInput.reset) {
      shooter.reset();
      if (auto1 != null) {
        auto1.reset();
      }
    }

    if (HumanInput.gyroReset) {
      navx.reset();
    }

    if (HumanInput.CPManipulatorUp) {
      if (HumanInput.fourSpins) {
        controlPanel.spinFourTimes();
      }

      // TODO: revert later - maybe
      // Commented due to lag issue
      // if(HumanInput.spinToYellow){
      // controlPanel.spinToYellow();
      // }

      // if(HumanInput.spinToGreen){
      // controlPanel.spinToGreen();
      // }

      // if(HumanInput.spinToBlue){
      // controlPanel.spinToBlue();
      // }

      // if(HumanInput.spinToRed){
      // controlPanel.spinToRed();
      // }

      // if(HumanInput.spinToFMSColor){
      // controlPanel.spinToFMSColor();
      // }
    }

    if (hasControlPanel) {
      controlPanel.update();
    }

    if (HumanInput.operatorBack && HumanInput.operatorStart && climber.climbStates == ClimbStates.IDLE) {
      climber.activate();
    }

    if (HumanInput.abortClimb) {
      climber.abortClimb();
    }

    if (HumanInput.abortShooter) {
      shooter.abortShooter();
    }

    if (HumanInput.resetEncoderVal) {
      driveController.resetEncoderVal();
    }

    climber.update(mP);
    driveController.update(mP);

    SmartDashboard.putString("Ball Intake Config", "Inactive");
    SmartDashboard.putString("Shooter Config", "Inactive");
    SmartDashboard.putString("Control Panel Config", "Inactive");
    SmartDashboard.putString("Climber Config", "Inactive");
    SmartDashboard.putNumber("Vertical Angle", vertAngle);

    if (!(HumanInput.leftSwitch) && !(HumanInput.rightSwitch)) { // ball + intake
      SmartDashboard.putString("Ball Intake Config", "Active");
    } else if (!(HumanInput.leftSwitch) && HumanInput.rightSwitch) { // shooter
      SmartDashboard.putString("Shooter Config", "Active");
    } else if (HumanInput.leftSwitch && !(HumanInput.rightSwitch)) { // control panel
      SmartDashboard.putString("Control Panel Config", "Active");
    } else if (HumanInput.leftSwitch && HumanInput.rightSwitch) { // climber + autos
      SmartDashboard.putString("Climber Config", "Active");
    }

    SmartDashboard.putNumber("X Coordinate: ", navx.getDisplacementX());
    SmartDashboard.putNumber("Y Coordinate: ", navx.getDisplacementY());
    SmartDashboard.putNumber("Z Coordinate: ", navx.getDisplacementZ());

    SmartDashboard.putNumber("Velocity X: ", navx.getVelocityX());
    SmartDashboard.putNumber("Velocity Y: ", navx.getVelocityY());
    SmartDashboard.putNumber("Velocity Z: ", navx.getVelocityZ());

    displacementX += (navx.getVelocityX());// * 0.02);
    displacementY += (navx.getVelocityY());// * 0.02);

    SmartDashboard.putNumber("X Displacement: ", displacementX);
    SmartDashboard.putNumber("Y Displacement: ", displacementY);
    // SmartDashboard.putNumber("Z Displacement: ");

    SmartDashboard.putNumber("Acceleration X: ", navx.getWorldLinearAccelX());
    SmartDashboard.putNumber("Acceleration Y: ", navx.getWorldLinearAccelY());
    SmartDashboard.putNumber("Acceleration Z: ", navx.getWorldLinearAccelZ());

    SmartDashboard.putNumber("Teleop Encoder Val", driveController.encoderPos);

    // SmartDashboard.putNumber("Left Motor Current", drivetrain.talon1.get);

  }

  @Override
  public void RechargeTestInit() {
    // SmartDashboard.putNumber("Target Offset", Constants.targettingOffset);
    // controlPanel.talon31.setSelectedSensorPosition(0);
    Solenoids.startCompressor();
    Solenoids.init();
  }

  @Override
  public void RechargeTestPeriodic() {
    HumanInput.update();
    SensorInput.update();
    Solenoids.update();
    Solenoids.startCompressor();
    // fxTest.Update();
    SmartDashboard.putString("Ball Intake Config", "Inactive");
    SmartDashboard.putString("Shooter Config", "Inactive");
    SmartDashboard.putString("Control Panel Config", "Inactive");
    SmartDashboard.putString("Climber Config", "Inactive");

    if (!(HumanInput.leftSwitch) && !(HumanInput.rightSwitch)) { // ball + intake
      SmartDashboard.putString("Ball Intake Config", "Active");
    } else if (!(HumanInput.leftSwitch) && HumanInput.rightSwitch) { // shooter
      SmartDashboard.putString("Shooter Config", "Active");
    } else if (HumanInput.leftSwitch && !(HumanInput.rightSwitch)) { // control panel
      SmartDashboard.putString("Control Panel Config", "Active");
    } else if (HumanInput.leftSwitch && HumanInput.rightSwitch) { // climber + autos
      SmartDashboard.putString("Climber Config", "Active");
    }

  }

  public void LogHeader() {
    Logger.Header("VerticalAngle,");
  }

  public void LogData() {
    Logger.doubles(vertAngle);
  }

  @Override
  public void RechargeDisabledInit() {
  }

  @Override
  public void RechargeDisabledPeriodic() {
    SmartDashboard.putBoolean("Joysticks OK", HumanInput.CheckJoysticks());
  }


}