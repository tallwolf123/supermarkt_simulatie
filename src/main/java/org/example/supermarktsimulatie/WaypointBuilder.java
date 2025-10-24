package org.example.supermarktsimulatie;

import javafx.scene.layout.Pane;
import java.util.HashMap;
import java.util.Map;

public class WaypointBuilder {
    private final Pane root;
    private final Map<String,Waypoint> waypoints = new HashMap<>();
    public WaypointBuilder(Pane root){ this.root=root; }

    public WaypointBuilder add(String id,double x,double y){ waypoints.put(id,new Waypoint(id,x,y,root)); return this; }
    public WaypointBuilder add(String id,double x,double y,Shelf shelf,Waypoint.WaypointType type){ waypoints.put(id,new Waypoint(id,x,y,shelf,type,root)); return this; }
    public WaypointBuilder connect(String from,String to){ waypoints.get(from).connectTo(waypoints.get(to),root); return this; }
    public Map<String,Waypoint> build(){ return waypoints; }
}
