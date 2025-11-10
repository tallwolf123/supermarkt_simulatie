package org.example.supermarktsimulatie.services;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.example.supermarktsimulatie.builders.AvatarBuilder;
import org.example.supermarktsimulatie.models.Customer;
import org.example.supermarktsimulatie.models.Employee;
import org.example.supermarktsimulatie.models.Waypoint;

import java.util.Map;

public class EntityManager {
    private final Pane root;
    private final Map<String, Waypoint> waypoints;

    public EntityManager(Pane root, Map<String, Waypoint> waypoints) {
        this.root = root;
        this.waypoints = waypoints;
    }

    public void spawnCustomer(Customer c) {
        Circle avatar = new AvatarBuilder()
                .withSize(10)
                .withColor(c.getColor())
                .atPosition(c.getCurrentPosition())
                .withBorder(true)
                .build();

        root.getChildren().add(avatar);
        c.setAvatar(avatar); // via Person superclass
    }

    public void spawnEmployee(Employee e) {
        Circle avatar = new AvatarBuilder()
                .withSize(8)
                .withColor(e.getColor())
                .atPosition(e.getCurrentPosition())
                .withBorder(true)
                .build();

        root.getChildren().add(avatar);
        e.setAvatar(avatar); // via Person superclass
    }
}

