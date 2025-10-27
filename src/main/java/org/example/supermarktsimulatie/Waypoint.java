package org.example.supermarktsimulatie;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.*;

public class Waypoint {
    public enum WaypointType { NORMAL, SHELF, CHECKOUT, EMPLOYEEONLY }

    private final String id;
    private final double x, y;
    private final Shelf shelf;
    private final WaypointType type;
    private final Circle circle;
    private final List<Waypoint> connections = new ArrayList<>();
    private final Queue<Customer> checkoutQueue = new LinkedList<>();

    public Waypoint(String id, double x, double y, Pane root) {
        this(id, x, y, null, WaypointType.NORMAL, root);
    }

    public Waypoint(String id, double x, double y, Shelf shelf, WaypointType type, Pane root) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.shelf = shelf;
        this.type = type;

        this.circle = new Circle(8, type == WaypointType.CHECKOUT ? Color.GREEN : Color.RED);
        this.circle.setLayoutX(x);
        this.circle.setLayoutY(y);
        root.getChildren().add(this.circle);
    }

    public void connectTo(Waypoint other, Pane root) {
        if (!connections.contains(other)) {
            connections.add(other);
            other.connections.add(this);
        }
        Line line = new Line();
        line.startXProperty().bind(circle.layoutXProperty());
        line.startYProperty().bind(circle.layoutYProperty());
        line.endXProperty().bind(other.circle.layoutXProperty());
        line.endYProperty().bind(other.circle.layoutYProperty());
        line.setStroke(Color.RED);
        line.setStrokeWidth(3);
        root.getChildren().add(1, line);
        circle.toFront();
        other.circle.toFront();
    }

    public List<Waypoint> getConnections() { return connections; }
    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public Shelf getShelf() { return shelf; }
    public boolean hasShelf() { return shelf != null; }
    public WaypointType getType() { return type; }

    // Queue logic
    public void enqueueAtCheckout(Customer c) { if (type==WaypointType.CHECKOUT) checkoutQueue.add(c); }
    public Customer dequeueFromCheckout() { return checkoutQueue.poll(); }
    public int getQueueSize() { return checkoutQueue.size(); }

    /** Snapshot van de huidige wachtrij in FIFO-volgorde. */
    public List<Customer> getQueueSnapshot() { return new ArrayList<>(checkoutQueue); }

    /** Bereken wachtrijpositie op de lijn van 'from' naar deze checkout. */
    public double[] getQueuePositionOnPath(Waypoint from, int index) {
        double dx = this.x - from.getX();
        double dy = this.y - from.getY();
        double len = Math.sqrt(dx*dx + dy*dy);
        if (len == 0) return new double[]{x, y};
        double step = 20; // afstand tussen klanten
        double factor = (len - (index+1)*step) / len;
        if (factor < 0) factor = 0;
        return new double[]{from.getX() + dx*factor, from.getY() + dy*factor};
    }
}
