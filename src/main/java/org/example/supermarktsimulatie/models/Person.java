package org.example.supermarktsimulatie.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    public enum State { IDLE, WALKING, SHOPPING, QUEUING, CHECKING_OUT, DONE }

    protected String name;
    protected Waypoint currentPosition;
    protected Circle avatar;
    protected double speed = 120.0; // pixels per seconde
    protected State state = State.IDLE;
    protected final List<Product> basket = new ArrayList<>();
    protected Color color = Color.DODGERBLUE;

    protected Person(String name, Waypoint startPosition) {
        this.name = name;
        this.currentPosition = startPosition;
    }

    protected Person(String name, Waypoint startPosition, Color color) {
        this(name, startPosition);
        if (color != null) this.color = color;
    }

    public String getName() { return name; }
    public Waypoint getCurrentPosition() { return currentPosition; }
    public void moveTo(Waypoint waypoint) { this.currentPosition = waypoint; }

    public Circle getAvatar() { return avatar; }
    public void setAvatar(Circle avatar) { this.avatar = avatar; }

    /** pixels/seconde */
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = Math.max(10.0, speed); }

    public List<Product> getBasketSnapshot() { return List.copyOf(basket); }
    public String getBasketDescription() {
        return basket.isEmpty() ? "(leeg)" :
                basket.stream().map(Product::getName).reduce((a,b) -> a + ", " + b).orElse("");
    }

    public State getState() { return state; }
    public void setState(State s) { this.state = s; }

    public Color getColor() { return color; }
    public void setColor(Color color) { if (color != null) this.color = color; }

    public abstract void act();
}
