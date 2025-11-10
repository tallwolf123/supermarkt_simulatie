package org.example.supermarktsimulatie.models;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    protected String name;
    protected Waypoint currentPosition;
    protected Circle avatar;
    protected double speed;
    protected State state;
    protected List<Product> basket = new ArrayList<>();

    public double getSpeed() {
        return 1;
    }

    public Node getAvatar() {
        return null;
    }

    public enum State { WALKING, WAITING, CHECKOUT, DONE }

    public Person(String name, Waypoint startPosition) {
        this.name = name;
        this.currentPosition = startPosition;
    }

    public String getName() { return name; }
    public Waypoint getCurrentPosition() { return currentPosition; }
    public void moveTo(Waypoint waypoint) { this.currentPosition = waypoint; }

    public List<Product> getBasketSnapshot() { return List.copyOf(basket); }
    public String getBasketDescription() {
        return basket.isEmpty() ? "(leeg)" :
                basket.stream().map(Product::getName).reduce((a,b)->a+", "+b).orElse("");
    }

    public boolean hasProduct(String productName) {
        return basket.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(productName));
    }


    public State getState() { return state; }
    public void setState(State s) { this.state = s; }

    public abstract void act();
}

