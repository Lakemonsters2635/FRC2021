package frc.robot.models;

import org.frcteam2910.common.control.ITrajectoryConstraint;
import org.frcteam2910.common.control.Path;
import org.frcteam2910.common.control.PathArcSegment;
import org.frcteam2910.common.control.PathLineSegment;
import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.math.Vector2;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.AutonomousTrajectoryCommand;
import frc.robot.commands.AutonomousTrajectoryLimitSwitchCommand;
import frc.robot.commands.ElevatorCommand;
import frc.robot.commands.IntakeActuateCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.IntakeDetectToElevatorIndexCommand;
import frc.robot.commands.RobotRotateCommand;
import frc.robot.commands.RotateControlPanelCommand;
import frc.robot.commands.ShooterActuateCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.VisionRotationDriveCommand;

public class AutonomousSequences {

        public static CommandGroup RotateTest(){
                CommandGroup output = new CommandGroup();

                // RobotRotateCommand rotateCommand = new RobotRotateCommand(90);
                // output.addSequential(rotateCommand);
                // Path driveToTrenchPath = new Path(Rotation2.ZERO);
                // driveToTrenchPath.addSegment(
                //         new PathLineSegment(
                //                 new Vector2(0.0, 0.0),
                //                 new Vector2(0.0, 0.0)
                //         ),
                //         Rotation2.fromDegrees(-90)
                //         // negative angle = clockwise when viewed from Frank's safe command station
                //         // positive angle = counterclockwise when viewed from Frank's safe command station       
                // );

                
                PathArcSegment fooFi = PathArcSegment.fromPoints(new Vector2(0.0, 0.0), new Vector2(70.0, -20.0), new Vector2(0, -60.0));
                Path arcFooFi = new Path(Rotation2.ZERO);
                arcFooFi.addSegment(fooFi);

                // Trajectory driveToTrenchTrajectory = new Trajectory(driveToTrenchPath, Robot.drivetrainSubsystem.CONSTRAINTS);
                // AutonomousTrajectoryCommand driveToTrenchCommand = new AutonomousTrajectoryCommand(driveToTrenchTrajectory);

                Trajectory fooFiTrajectory = new Trajectory(arcFooFi, Robot.drivetrainSubsystem.CONSTRAINTS);
                AutonomousTrajectoryCommand fooFiCommand = new AutonomousTrajectoryCommand(fooFiTrajectory);

                
                output.addSequential(fooFiCommand);
        
                return output;
        }

	public static CommandGroup ShootThenCollectRight(){
                CommandGroup output = new CommandGroup();
                ShooterActuateCommand shooterAcuateCommand = new ShooterActuateCommand(true, 1);
                ElevatorCommand elevatorCommand = new ElevatorCommand(false, 3);
                ShooterCommand shooterCommand = new ShooterCommand(false, 4, RobotMap.SHOOTER_INTITIATION_LINE_UPPER_MOTOR_SPEED );
                Path driveToTrenchPath = new Path(Rotation2.ZERO);
                driveToTrenchPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-44.63, -67.905) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveToTrenchTrajectory = new Trajectory(driveToTrenchPath, Robot.drivetrainSubsystem.CONSTRAINTS);

                AutonomousTrajectoryCommand driveToTrenchCommand = new AutonomousTrajectoryCommand(driveToTrenchTrajectory);

                IntakeActuateCommand lowerIntake = new IntakeActuateCommand(false,1);
                IntakeActuateCommand raiseIntake = new IntakeActuateCommand(true,1);

                
                output.addSequential(shooterAcuateCommand);
                output.addParallel(elevatorCommand);
                output.addSequential(shooterCommand);
                output.addParallel(lowerIntake);
                output.addSequential(driveToTrenchCommand);
                
                
                //We've reached the trench. Now collect power cell. 
                Path driveThroughTrenchPath = new Path(Rotation2.ZERO);
                driveThroughTrenchPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-106, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveThroughTrenchTrajectory = new Trajectory(driveThroughTrenchPath, Robot.drivetrainSubsystem.INTAKE_CONSTRAINTS);

                AutonomousTrajectoryCommand driveThroughTrenchCommand = new AutonomousTrajectoryCommand(driveThroughTrenchTrajectory);

           
        
                output.addParallel(driveThroughTrenchCommand);
                output.addSequential(new IntakeDetectToElevatorIndexCommand());
                output.addSequential(new IntakeDetectToElevatorIndexCommand());
                output.addSequential(new IntakeDetectToElevatorIndexCommand());
                output.addSequential(raiseIntake);
                return output;
        }

	public static CommandGroup ShootThenCollectLeft(){
                CommandGroup output = new CommandGroup();
                ShooterActuateCommand shooterAcuateCommand = new ShooterActuateCommand(true, 1);
                ElevatorCommand elevatorCommand = new ElevatorCommand(false, 3);
                ShooterCommand shooterCommand = new ShooterCommand(false, 4, RobotMap.SHOOTER_INTITIATION_LINE_UPPER_MOTOR_SPEED );
                Path driveToLeftTrenchPath = new Path(Rotation2.ZERO);
                driveToLeftTrenchPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-75, 191.8) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveToLeftTrenchTrajectory = new Trajectory(driveToLeftTrenchPath, Robot.drivetrainSubsystem.CONSTRAINTS);

                AutonomousTrajectoryCommand driveToTrenchCommand = new AutonomousTrajectoryCommand(driveToLeftTrenchTrajectory);

                IntakeActuateCommand lowerIntake = new IntakeActuateCommand(false,1);
                IntakeActuateCommand raiseIntake = new IntakeActuateCommand(true,1);

                
                output.addSequential(shooterAcuateCommand);
                output.addParallel(elevatorCommand);
                output.addSequential(shooterCommand);
                output.addParallel(lowerIntake);
                output.addSequential(driveToTrenchCommand);
                
                Path driveToBallPath = new Path(Rotation2.ZERO);
                driveToBallPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-20, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveToBallTrajectory = new Trajectory(driveToBallPath, Robot.drivetrainSubsystem.INTAKE_CONSTRAINTS);

                AutonomousTrajectoryCommand driveToBallCommand = new AutonomousTrajectoryCommand(driveToBallTrajectory);

                
                output.addParallel(driveToBallCommand);
                output.addSequential(new IntakeDetectToElevatorIndexCommand());
                
                Path driveToNextBallPath = new Path(Rotation2.ZERO);
                driveToNextBallPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(20, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );
                driveToNextBallPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(0.0, 18.3) //FHE:TODO Confirm positive/negative
                        )
                );
                driveToNextBallPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-20, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveToNextBallTrajectory = new Trajectory(driveToNextBallPath, Robot.drivetrainSubsystem.INTAKE_CONSTRAINTS);

                AutonomousTrajectoryCommand driveToNextBallCommand = new AutonomousTrajectoryCommand(driveToNextBallTrajectory);

                output.addParallel(driveToNextBallCommand);
                output.addSequential(new IntakeDetectToElevatorIndexCommand());

                output.addSequential(raiseIntake);
                return output;
        }


        public static CommandGroup ShootThenCollectRight_ThenShootAgain(){
                CommandGroup output =  ShootThenCollectRight();
                Path driveBackToShoot = new Path(Rotation2.ZERO);


                driveBackToShoot.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(106, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveBackToShootTrajectory = new Trajectory(driveBackToShoot, Robot.drivetrainSubsystem.CONSTRAINTS);

                AutonomousTrajectoryCommand driveBackToShootCommand= new AutonomousTrajectoryCommand(driveBackToShootTrajectory);
                output.addSequential(driveBackToShootCommand,3);
                VisionRotationDriveCommand rotateCommand = new VisionRotationDriveCommand(2);
             

                ElevatorCommand elevatorCommand = new ElevatorCommand(false, 3);
                ShooterCommand shooterCommand = new ShooterCommand(false, 4, RobotMap.SHOOTER_INTITIATION_LINE_UPPER_MOTOR_SPEED );
                output.addSequential(rotateCommand);
                output.addParallel(elevatorCommand);
                output.addSequential(shooterCommand);;
                return output;
        }

        public static CommandGroup ShootFromRight_Of_Optimal_Then_Collect(){
                CommandGroup output =  new CommandGroup();
                IntakeActuateCommand raiseIntake = new IntakeActuateCommand(true,2);
                VisionRotationDriveCommand visionRotateCommand = new VisionRotationDriveCommand(2);
                RobotRotateCommand rotateCommand = new RobotRotateCommand(0);
                ShooterActuateCommand shooterAcuateCommand = new ShooterActuateCommand(true, 1);
                ElevatorCommand elevatorCommand = new ElevatorCommand(false, 3);
                IntakeActuateCommand lowerIntake = new IntakeActuateCommand(false, 2);
                ShooterCommand shooterCommand = new ShooterCommand(false, 4, RobotMap.SHOOTER_INTITIATION_LINE_UPPER_MOTOR_SPEED );

                output.addParallel(raiseIntake);
                output.addSequential(visionRotateCommand);
                
     
                output.addSequential(shooterAcuateCommand);
                output.addParallel(elevatorCommand);
                output.addSequential(shooterCommand);

                //output.addSequential(rotateCommand);
               
                Path driveToTrenchPath = new Path(Rotation2.ZERO);
                driveToTrenchPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-44.63, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveToTrenchTrajectory = new Trajectory(driveToTrenchPath, Robot.drivetrainSubsystem.CONSTRAINTS);

                AutonomousTrajectoryCommand driveToTrenchCommand= new AutonomousTrajectoryCommand(driveToTrenchTrajectory);
                output.addSequential(driveToTrenchCommand);


                output.addParallel(lowerIntake);

                Path driveThroughTrenchPath = new Path(Rotation2.ZERO);
                driveThroughTrenchPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-106, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveThroughTrenchTrajectory = new Trajectory(driveThroughTrenchPath, Robot.drivetrainSubsystem.INTAKE_CONSTRAINTS);

                AutonomousTrajectoryCommand driveThroughTrenchCommand = new AutonomousTrajectoryCommand(driveThroughTrenchTrajectory);

           
        
                output.addParallel(driveThroughTrenchCommand);
                output.addSequential(new IntakeDetectToElevatorIndexCommand());
                output.addSequential(new IntakeDetectToElevatorIndexCommand());
                output.addSequential(new IntakeDetectToElevatorIndexCommand());
                output.addSequential(new IntakeActuateCommand(true,2));

                return output;

        }

        public static CommandGroup shootThenBackAwayFromInitiationLine(){
                CommandGroup output =  new CommandGroup();
                IntakeActuateCommand raiseIntake = new IntakeActuateCommand(true,2);
                VisionRotationDriveCommand visionRotateCommand = new VisionRotationDriveCommand(2);
                RobotRotateCommand rotateCommand = new RobotRotateCommand(0);
                ShooterActuateCommand shooterAcuateCommand = new ShooterActuateCommand(true, 1);
                ElevatorCommand elevatorCommand = new ElevatorCommand(false, 3);
                ShooterCommand shooterCommand = new ShooterCommand(false, 4, RobotMap.SHOOTER_INTITIATION_LINE_UPPER_MOTOR_SPEED );

                output.addParallel(raiseIntake);
                output.addSequential(visionRotateCommand);
                
     
                output.addSequential(shooterAcuateCommand);
                output.addParallel(elevatorCommand);
                output.addSequential(shooterCommand);

                //output.addSequential(rotateCommand);
               
                Path driveAwayPath = new Path(Rotation2.ZERO);
                driveAwayPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-20, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveAwayTrajectory = new Trajectory(driveAwayPath, Robot.drivetrainSubsystem.CONSTRAINTS);

                AutonomousTrajectoryCommand driveAwayCommand= new AutonomousTrajectoryCommand(driveAwayTrajectory);
                output.addSequential(driveAwayCommand);

                return output;
        }


	public static CommandGroup backAwayFromInitiationLine(){
                CommandGroup output = new CommandGroup();
                Path backAwayPath = new Path(Rotation2.ZERO);
                backAwayPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-48, 0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory backawayTrajectory = new Trajectory(backAwayPath, Robot.drivetrainSubsystem.CONSTRAINTS);
                AutonomousTrajectoryCommand backAwayCommand = new AutonomousTrajectoryCommand(backawayTrajectory);
                output.addSequential(backAwayCommand, 2);

                return output;

        }


        public static CommandGroup PositionForCPMAndRotateFourTimes(){
                CommandGroup output = new CommandGroup();
                Path driveOffWallPath = new Path(Rotation2.ZERO);
                driveOffWallPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(-27.5, 0.0) //FHE:TODO Confirm positive/negative
                        )
                );


                Trajectory driveOffWallTrajectory = new Trajectory(driveOffWallPath, Robot.drivetrainSubsystem.CONSTRAINTS);

                AutonomousTrajectoryCommand driveOffWallCommand = new AutonomousTrajectoryCommand(driveOffWallTrajectory);

                output.addSequential(driveOffWallCommand);

                Path driveTowardsCPPath = new Path(Rotation2.ZERO);
                driveTowardsCPPath.addSegment(
                        new PathLineSegment(
                                new Vector2(0.0, 0.0),
                                new Vector2(0, -200) //FHE:TODO Confirm positive/negative
                        )
                );

                
                Trajectory driveTowardsCPTrajectory = new Trajectory(driveTowardsCPPath, Robot.drivetrainSubsystem.CONSTRAINTS);//TODO add slow constrains

                AutonomousTrajectoryLimitSwitchCommand driveTowardsCPCommand = new AutonomousTrajectoryLimitSwitchCommand(driveTowardsCPTrajectory);
                output.addSequential(driveTowardsCPCommand);

                RotateControlPanelCommand rotateControlPanelCommand = new RotateControlPanelCommand(Robot.colorSpinnerSubsystem, 4);
                output.addSequential(rotateControlPanelCommand);
                return output;
        }
    //Lifts intake
    //Drives forward 5 inches
    //Spins intake
    //Lowers intake and calls elevator state machine
    //Drives backward 5 inches.
    public static CommandGroup CollectPowerCell(){
        Path fiveInchesPath = new Path(Rotation2.ZERO);
        fiveInchesPath.addSegment(
                new PathLineSegment(
                        new Vector2(0.0, 0.0),
                        new Vector2(5, 0)
                )
        );


        Trajectory fiveInchesForward = new Trajectory(fiveInchesPath, Robot.drivetrainSubsystem.CONSTRAINTS);


        CommandGroup output = new CommandGroup();
        
        AutonomousTrajectoryCommand trajectoryCommand = new AutonomousTrajectoryCommand(fiveInchesForward);
        IntakeCommand intakeCommand = new IntakeCommand(false);
        output.addParallel(intakeCommand);
        output.addParallel(trajectoryCommand);
	return output;
    }


    public static String getMethodName()
	{
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		return methodName;
	}
}