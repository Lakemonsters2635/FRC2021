package org.frcteam2910.common.control;


import org.frcteam2910.common.math.RigidTransform2;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.util.HolonomicDriveSignal;
import org.frcteam2910.common.util.HolonomicFeedforward;

public class HolonomicMotionProfiledTrajectoryFollower extends TrajectoryFollower<HolonomicDriveSignal> {
    private PidController forwardController;
    private PidController strafeController;
    private PidController rotationController;

    private HolonomicFeedforward feedforward;

    private Trajectory.Segment lastSegment = null;

    private boolean finished = false;

    public HolonomicMotionProfiledTrajectoryFollower(PidConstants translationConstants, PidConstants rotationConstants,
                                                     HolonomicFeedforward feedforward) {
        this.forwardController = new PidController(translationConstants);
        this.strafeController = new PidController(translationConstants);
        this.rotationController = new PidController(rotationConstants);
        this.rotationController.setContinuous(true);
        this.rotationController.setInputRange(0.0, 2.0 * Math.PI);

        this.feedforward = feedforward;
    }
    RigidTransform2 pose;
    @Override
    protected HolonomicDriveSignal calculateDriveSignal(RigidTransform2 currentPose, Vector2 velocity,
                                               double rotationalVelocity, Trajectory trajectory, double time,
                                               double dt) {

                                               

        //System.out.println("calculateDriveSignal: time: " + time + "\t pose: " + currentPose.toString());
        //System.out.println("velocity; " + velocity.toString() + "\trotaionalVelocity; " + rotationalVelocity);
        if (time > trajectory.getDuration()) {
            finished = true;
            return new HolonomicDriveSignal(Vector2.ZERO, 0.0,false);
        }

        pose = currentPose;

        lastSegment = trajectory.calculateSegment(time);

        Vector2 segmentVelocity = Vector2.fromAngle(lastSegment.heading).scale(lastSegment.velocity);
        Vector2 segmentAcceleration = Vector2.fromAngle(lastSegment.heading).scale(lastSegment.acceleration);

        Vector2 feedforwardVector = feedforward.calculateFeedforward(segmentVelocity, segmentAcceleration);

        forwardController.setSetpoint(lastSegment.translation.x);
        strafeController.setSetpoint(lastSegment.translation.y);
        rotationController.setSetpoint(lastSegment.rotation.toRadians());

        // System.out.println("Forward Setpoint: " + forwardController.getSetpoint());
        //System.out.println("Strafe Setpoint: " + strafeController.getSetpoint());
        //System.out.println("curentpose Strafe: " + currentPose.translation.y);
        //System.out.println("Rotation Setpoint: " + Math.toDegrees(rotationController.getSetpoint()));
// System.out.println("Y: " + (float) currentPose.translation.y + "\t X: " + (float) currentPose.translation.x);
//System.out.println("(" + currentPose.translation.y + ", " + currentPose.translation.x + ")");

        return new HolonomicDriveSignal(
                new Vector2(
                        forwardController.calculate(currentPose.translation.x, dt) + feedforwardVector.x,
                        (strafeController.calculate(currentPose.translation.y, dt) + feedforwardVector.y) 
                ),
                rotationController.calculate(currentPose.rotation.toRadians(), dt),
                true
        );
    }

    public Trajectory.Segment getLastSegment() {
        return lastSegment;
    }

    public RigidTransform2 getCurrentPose() {
        return pose;
    }
    
    @Override
    protected boolean isFinished() {
        return finished;
    }

    @Override
    protected void reset() {
        forwardController.reset();
        strafeController.reset();
        rotationController.reset();

        finished = false;
    }
}
