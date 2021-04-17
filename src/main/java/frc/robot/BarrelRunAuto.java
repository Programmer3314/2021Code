/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public class BarrelRunAuto extends WaypointAuto{

    @Override
    public void setWaypoints() {

        Waypoint.scale = 1.0;

        waypoints.add(new WaypointDistance(0.4, 0, 0.50)); //FORWARD
        waypoints.add(new WaypointDistance(0.7, 0, 0.50)); //FORWARD
        waypoints.add(new WaypointDistance(0.8, 0, 5.5)); //FORWARD
        waypoints.add(new WaypointGyro(0.3 * 2.1, 0.14 * 2.0, 342)); //FORWARD CIRCLE TURN RIGHT
        waypoints.add(new WaypointDistance(0.8, 0, 5.7)); //FORWARD
        waypoints.add(new WaypointDistance(0.6, 0, 0.5)); //FORWARD SLOW

        waypoints.add(new WaypointGyro(0.3 * 1.8, -0.13 * 1.8, -291 + 360)); //-288//FORWARD CIRCLE TURN LEFT
        waypoints.add(new WaypointDistance(0.8, 0, 6.5)); //FORWARD
        waypoints.add(new WaypointDistance(0.5, 0, 0.5)); //1.5//FORWARD (SLOW DOWN)

        waypoints.add(new WaypointGyro(0.3 * 1.8, -0.135 * 1.8, -508+ 360)); //FORWARD CIRCLE TURN LEFT
        
        waypoints.add(new WaypointDistance(1.0, 0, 16.7)); //FORWARD 
        waypoints.add(new WaypointDistance(0, 0, 0.0)); //STOP

        
       //Time: 14.9
    //    waypoints.add(new WaypointDistance(0.3, 0, 0.50)); //FORWARD
    //     waypoints.add(new WaypointDistance(0.5, 0, 0.50)); //FORWARD
    //     waypoints.add(new WaypointDistance(0.7, 0, 7.2)); //FORWARD
    //     waypoints.add(new WaypointGyro(0.3 * 1.6, 0.14 * 1.6, 356)); //FORWARD CIRCLE TURN RIGHT
    //     waypoints.add(new WaypointDistance(0.7, 0, 6.2)); //FORWARD

    //     waypoints.add(new WaypointGyro(0.3 * 1.4, -0.13 * 1.4, -295 + 360)); //-288//FORWARD CIRCLE TURN LEFT
    //     waypoints.add(new WaypointDistance(0.7, 0, 4.6)); //FORWARD
    //     waypoints.add(new WaypointDistance(0.5, 0, 0.4)); //1.5//FORWARD (SLOW DOWN)

    //     waypoints.add(new WaypointGyro(0.3 * 1.6, -0.135 * 1.6, -510 + 360)); //FORWARD CIRCLE TURN LEFT
        
    //     waypoints.add(new WaypointDistance(1.0, 0, 16.5)); //FORWARD 
    //     waypoints.add(new WaypointDistance(0, 0, 0.0)); //STOP



    }

}