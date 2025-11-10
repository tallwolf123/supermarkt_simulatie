package org.example.supermarktsimulatie.services;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.example.supermarktsimulatie.models.Person;
import org.example.supermarktsimulatie.models.Waypoint;

import java.util.List;

public class MovementService {
    private final Pane root;
    private final RoutePlanner routePlanner;

    public MovementService(Pane root, RoutePlanner routePlanner) {
        this.root = root;
        this.routePlanner = routePlanner;
    }

    public void moveAlong(Person person, List<Waypoint> path, Runnable onComplete) {
        if (path.size() <= 1) { onComplete.run(); return; }

        Waypoint next = path.get(1);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(person.getSpeed()), person.getAvatar());
        tt.setToX(next.getX());
        tt.setToY(next.getY());
        tt.setOnFinished(e -> {
            person.moveTo(next);
            moveAlong(person, path.subList(1, path.size()), onComplete);
        });
        tt.play();
    }
}

