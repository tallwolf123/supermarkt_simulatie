package org.example.supermarktsimulatie.models;

import javafx.scene.paint.Color;

public class Employee extends Person {

    public Employee(String name, Waypoint startPosition) {
        super(name, startPosition);
        this.color = Color.DARKORANGE;
    }

    public Employee(String name, Waypoint startPosition, Color color) {
        super(name, startPosition, color);
    }

    @Override
    public void act() {
        // evt. restock-gedrag toevoegen
        state = State.WALKING;
    }

    public boolean hasProduct(String productName) {
        if (productName == null) return false;
        return basket.stream().anyMatch(p -> productName.equalsIgnoreCase(p.getName()));
    }

    public boolean hasProduct(Product p) {
        return p != null && hasProduct(p.getName());
    }
}
