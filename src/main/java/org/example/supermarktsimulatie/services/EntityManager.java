package org.example.supermarktsimulatie.services;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.example.supermarktsimulatie.builders.AvatarBuilder;
import org.example.supermarktsimulatie.models.Customer;
import org.example.supermarktsimulatie.models.Employee;
import org.example.supermarktsimulatie.models.Waypoint;

import java.util.Map;
import java.util.Objects;

public class EntityManager {
    private final Pane root;
    private final Map<String, Waypoint> waypoints;

    public EntityManager(Pane root, Map<String, Waypoint> waypoints) {
        this.root = Objects.requireNonNull(root, "root pane is null");
        this.waypoints = Objects.requireNonNull(waypoints, "waypoints is null");
    }

    public Waypoint wp(String id) {
        Waypoint w = waypoints.get(id);
        if (w == null) throw new IllegalArgumentException("Onbekende waypoint: " + id);
        return w;
    }

    public void spawnCustomer(Customer c) {
        Objects.requireNonNull(c, "customer is null");
        Circle avatar = new AvatarBuilder()
                .withSize(10)
                .withColor(c.getColor())
                .atPosition(c.getCurrentPosition())
                .withBorder(true)
                .build();
        root.getChildren().add(avatar);
        c.setAvatar(avatar);
    }

    public void spawnEmployee(Employee e) {
        Objects.requireNonNull(e, "employee is null");
        Circle avatar = new AvatarBuilder()
                .withSize(8)
                .withColor(e.getColor())
                .atPosition(e.getCurrentPosition())
                .withBorder(true)
                .build();
        root.getChildren().add(avatar);
        e.setAvatar(avatar);
    }
}
