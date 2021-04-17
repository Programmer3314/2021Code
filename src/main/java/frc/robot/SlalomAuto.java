/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public class SlalomAuto extends WaypointAuto{

    @Override
    public void setWaypoints() {
        waypoints.add(new WaypointDistance(0.4, 0, 2)); //FORWARD
        waypoints.add(new WaypointGyro(0.3 * 1.5, -0.1 * 1.5, -52)); //TURN LEFT 
        waypoints.add(new WaypointDistance(0.6, 0, 1.0)); //FORWARD through cones
        //waypoints.add(new WaypointGyro(0.3 * 1.6, 0.0545 * 1.6, 50)); //WIDE ARC TURN RIGHT
        //// waypoints.add(new WaypointDistance(0.4, 0, 6.5)); //FORWARD
         waypoints.add(new WaypointGyro(0.3 * 1.5, 0.1 * 1.5, -5)); //TURN RIGHT 
        waypoints.add(new WaypointDistance(0.6, 0, 5.4)); //FORWARD LONG
        waypoints.add(new WaypointGyro(0.3 * 1.5, 0.1 * 1.5, 55));//TURN
        waypoints.add(new WaypointDistance(0.4, 0, 2.0)); //FORWARD THOUGH CONES
        waypoints.add(new WaypointGyro(0.3 * 1.5, -0.14 * 1.5, -230)); //FULL CIRCLE TURN LEFT
        waypoints.add(new WaypointDistance(0.4, 0, 2)); //FORWARD Through cones
        waypoints.add(new WaypointGyro(0.3 * 1.5, 0.1 * 1.5, -185)); //TURN RIGHT
        waypoints.add(new WaypointDistance(0.7, 0, 5.6)); //FORWARD LONG
        waypoints.add(new WaypointGyro(0.3 * 1.5, 0.1 * 1.5, -135)); // TURN RIGHT
        waypoints.add(new WaypointDistance(0.6, 0, 2.5)); //FORWARD Through cones
        waypoints.add(new WaypointGyro(0.3 * 1.5, -0.1 * 1.5, -170 + 8)); //TURN LEFT
        waypoints.add(new WaypointDistance(0, 0, 0)); //STOP


        // //scale = 1.1
        // waypoints.add(new WaypointDistance(0.4, 0, 2)); //FORWARD
        // waypoints.add(new WaypointGyro(0.3, -0.1, -50)); //TURN LEFT
        // waypoints.add(new WaypointDistance(0.4, 0, 1.25)); //FORWARD
        // waypoints.add(new WaypointGyro(0.3, 0.1, -8)); //TURN RIGHT
        // waypoints.add(new WaypointDistance(0.4, 0, 6.5)); //FORWARD
        // waypoints.add(new WaypointGyro(0.3, 0.1, 50)); //TURN RIGHT
        // waypoints.add(new WaypointDistance(0.4, 0, 2)); //FORWARD
        // waypoints.add(new WaypointGyro(0.3, -0.13, -225)); //FULL CIRCLE TURN LEFT
        // waypoints.add(new WaypointDistance(0.4, 0, 2)); //FORWARD
        // waypoints.add(new WaypointGyro(0.3, 0.1, -180)); //TURN RIGHT
        // waypoints.add(new WaypointDistance(0.4, 0, 8.5)); //FORWARD
        // waypoints.add(new WaypointGyro(0.3, 0.1, -130)); // TURN RIGHT
        // waypoints.add(new WaypointDistance(0.4, 0, 3)); //FORWARD
        // waypoints.add(new WaypointGyro(0.3, -0.1, -180 + 8)); //TURN LEFT
        // waypoints.add(new WaypointDistance(0, 0, 0)); //STOP
    }

}