package org.example.supermarktsimulatie.builders;

import javafx.scene.layout.Pane;
import org.example.supermarktsimulatie.models.Shelf;
import org.example.supermarktsimulatie.models.Waypoint;

import java.util.HashMap;
import java.util.Map;

public class WaypointBuilder {
    private final Pane root;
    private final Map<String, Waypoint> waypoints = new HashMap<>();

    public WaypointBuilder(Pane root) { this.root = root; }

    public WaypointBuilder add(String id, double x, double y) {
        waypoints.put(id, new Waypoint(id, x, y, root));
        return this;
    }

    public WaypointBuilder add(String id, double x, double y, Shelf shelf, Waypoint.WaypointType type) {
        waypoints.put(id, new Waypoint(id, x, y, shelf, type, root));
        return this;
    }

    public WaypointBuilder connect(String from, String to) {
        Waypoint a = waypoints.get(from);
        Waypoint b = waypoints.get(to);
        if (a == null || b == null)
            throw new IllegalStateException("Onbekende waypoint in connect: " + from + " -> " + to);
        a.connectTo(b, root); // bidirectioneel via Waypoint.connectTo
        return this;
    }

    public Map<String, Waypoint> build() { return waypoints; }
}
