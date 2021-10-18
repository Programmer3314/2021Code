
package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// push test

public class DrivetrainNEO implements IDriveTrain {
    public CANSparkMax spark1, spark2, spark3, spark4, spark5, spark6;
    private CANPIDController leftPidController, rightPidController;
    private CANEncoder leftEncoder, rightEncoder;


    public DrivetrainNEO( int leftDriveFront, int leftDriveMiddle, int leftDriveBack, int rightDriveFront, int rightDriveMiddle, int rightDriveBack){
        spark1 = new CANSparkMax(leftDriveFront, CANSparkMaxLowLevel.MotorType.kBrushless);
        spark2 = new CANSparkMax(leftDriveMiddle, CANSparkMaxLowLevel.MotorType.kBrushless);
        spark3 = new CANSparkMax(leftDriveBack, CANSparkMaxLowLevel.MotorType.kBrushless);
        spark4 = new CANSparkMax(rightDriveFront, CANSparkMaxLowLevel.MotorType.kBrushless);
        spark5 = new CANSparkMax(rightDriveMiddle, CANSparkMaxLowLevel.MotorType.kBrushless);
        spark6 = new CANSparkMax(rightDriveBack, CANSparkMaxLowLevel.MotorType.kBrushless);

        spark1.restoreFactoryDefaults();
        spark2.restoreFactoryDefaults();
        spark3.restoreFactoryDefaults();
        spark4.restoreFactoryDefaults();
        spark5.restoreFactoryDefaults();
        spark6.restoreFactoryDefaults();

        spark4.setInverted(true);

        spark2.follow(spark1);
        spark3.follow(spark1);
        spark5.follow(spark4);
        spark6.follow(spark4);

        spark1.setSmartCurrentLimit(Constants.sparkDriveTrainStallLimit, Constants.sparkDriveTrainFreeLimit);
        spark2.setSmartCurrentLimit(Constants.sparkDriveTrainStallLimit, Constants.sparkDriveTrainFreeLimit);
        spark3.setSmartCurrentLimit(Constants.sparkDriveTrainStallLimit, Constants.sparkDriveTrainFreeLimit);
        spark4.setSmartCurrentLimit(Constants.sparkDriveTrainStallLimit, Constants.sparkDriveTrainFreeLimit);
        spark5.setSmartCurrentLimit(Constants.sparkDriveTrainStallLimit, Constants.sparkDriveTrainFreeLimit);
        spark6.setSmartCurrentLimit(Constants.sparkDriveTrainStallLimit, Constants.sparkDriveTrainFreeLimit);

        leftPidController = spark1.getPIDController();
        rightPidController = spark4.getPIDController();

        leftPidController.setP(Constants.drivetrainKP);
        leftPidController.setI(Constants.drivetrainKI);
        leftPidController.setD(Constants.drivetrainKD);
        leftPidController.setIZone(Constants.drivetrainKIz);
        leftPidController.setFF(Constants.drivetrainKFF);
        leftPidController.setOutputRange(Constants.drivetrainKMinOutput, Constants.drivetrainKMaxOutput);

        rightPidController.setP(Constants.drivetrainKP);
        rightPidController.setI(Constants.drivetrainKI);
        rightPidController.setD(Constants.drivetrainKD);
        rightPidController.setIZone(Constants.drivetrainKIz);
        rightPidController.setFF(Constants.drivetrainKFF);
        rightPidController.setOutputRange(Constants.drivetrainKMinOutput, Constants.drivetrainKMaxOutput);

        leftEncoder = spark1.getEncoder();
        rightEncoder = spark4.getEncoder();

        leftEncoder.setVelocityConversionFactor(1);
        rightEncoder.setVelocityConversionFactor(1);
    }

    @Override
    public void update(double leftSetPoint, double rightSetPoint){
        leftPidController.setReference(leftSetPoint * Constants.maxRPM, ControlType.kVelocity);
        rightPidController.setReference(rightSetPoint * Constants.maxRPM, ControlType.kVelocity);

        Robot.ntInst.getEntry("RPM Left").setDouble(leftEncoder.getVelocity());
        Robot.ntInst.getEntry("Set Point Left").setDouble(leftSetPoint);
        SmartDashboard.putNumber("Current Flow ", spark1.getOutputCurrent());
    }

    @Override
    public double getEncoderVal(){
        return leftEncoder.getPosition();
    }

    @Override
    public void resetEncoderVal() {
        // TODO Auto-generated method stub

    }

    
}