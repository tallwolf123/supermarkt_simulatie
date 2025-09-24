package org.example.supermarktsimulatie;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class Waypoint {
    private final String id;
    private final double x;
    private final double y;
    private final Shelf shelf;
    private final Circle circle;
    private final List<Waypoint> connections = new ArrayList<>();

    public Waypoint(String id, double x, double y, Pane root) {
        this(id, x, y, null, root);
    }

    public Waypoint(String id, double x, double y, Shelf shelf, Pane root) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.shelf = shelf;

        // Circle
        this.circle = new Circle(8, Color.RED);
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
        line.startXProperty().bind(this.circle.layoutXProperty());
        line.startYProperty().bind(this.circle.layoutYProperty());
        line.endXProperty().bind(other.circle.layoutXProperty());
        line.endYProperty().bind(other.circle.layoutYProperty());
        line.setStroke(Color.RED);
        line.setStrokeWidth(3);

        int imageViewIndex = 0;
        root.getChildren().add(imageViewIndex + 1, line);

        this.circle.toFront();
        other.circle.toFront();
    }


    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public Shelf getShelf() { return shelf; }
    public boolean hasShelf() { return shelf != null; }
}
