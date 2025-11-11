package org.example.supermarktsimulatie.models;

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class Waypoint {
    private final String id;
    private final double x, y;
    private final List<Waypoint> connections = new ArrayList<>();
    private final Shelf shelf;                 // kan null zijn
    private final WaypointType type;

    public enum WaypointType { DEFAULT, SHELF, CHECKOUT, EMPLOYEEONLY }

    // Gewoon knooppunt
    public Waypoint(String id, double x, double y, Pane root) {
        this(id, x, y, null, WaypointType.DEFAULT, root);
    }

    // Knooppunt met shelf/type
    public Waypoint(String id, double x, double y, Shelf shelf, WaypointType type, Pane root) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.shelf = shelf;
        this.type = (type == null) ? WaypointType.DEFAULT : type;
    }

    /** Tweerichtingsverbinding leggen */
    public void connectTo(Waypoint other, Pane root) {
        if (other == null || other == this) return;
        if (!connections.contains(other)) connections.add(other);
        if (!other.connections.contains(this)) other.connections.add(this);
    }

    // Getters
    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public List<Waypoint> getConnections() { return connections; }
    public Shelf getShelf() { return shelf; }
    public boolean hasShelf() { return shelf != null; }
    public WaypointType getType() { return type; }
}
