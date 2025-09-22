package org.example.supermarktsimulatie;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Waypoint {
    private final String id;
    private final double x;
    private final double y;
    private final Shelf shelf; // optional, null if waypoint is just a path node
    private final Circle circle; // visual representation

    public Waypoint(String id, double x, double y, Shelf shelf) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.shelf = shelf;

        this.circle = new Circle(8, Color.RED);
        this.circle.setTranslateX(x);
        this.circle.setTranslateY(y);
    }

    public Waypoint(String id, double x, double y) {
        this(id, x, y, null);
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public boolean hasShelf() {
        return shelf != null;
    }

    public Circle getCircle() {
        return circle;
    }


    @Override
    public String toString() {
        return id + " (" + x + ", " + y + ")" + (shelf != null ? " [Shelf: " + shelf.getName() + "]" : "");
    }
}
