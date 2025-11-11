package org.example.supermarktsimulatie.services;

import javafx.animation.AnimationTimer;
import org.example.supermarktsimulatie.models.Person;
import org.example.supermarktsimulatie.models.Waypoint;

import java.util.*;
import java.util.function.Consumer;

/** Vloeiende beweging in pixels/seconde; roept onArrive bij elk waypoint en onComplete op het eind. */
public class MovementService {

    private static final double EPS = 0.5; // aankomstdrempel in pixels
    private final Map<Person, Mover> movers = new HashMap<>();

    public void moveAlong(Person person,
                          List<Waypoint> path,
                          Consumer<Waypoint> onArrive,
                          Runnable onComplete) {
        if (person == null || path == null || path.size() <= 1) {
            if (onComplete != null) onComplete.run();
            return;
        }
        stop(person);
        Mover mover = new Mover(person, path, onArrive, onComplete);
        movers.put(person, mover);
        mover.start();
    }

    public void stop(Person person) {
        Mover m = movers.remove(person);
        if (m != null) m.stop();
    }

    private static final class Mover extends AnimationTimer {
        private final Person person;
        private final List<Waypoint> path;
        private final Consumer<Waypoint> onArrive;
        private final Runnable onComplete;
        private int index = 0;          // bewegen van path[index] -> path[index+1]
        private long lastNs = 0;

        Mover(Person person, List<Waypoint> path, Consumer<Waypoint> onArrive, Runnable onComplete) {
            this.person = person;
            this.path = path;
            this.onArrive = onArrive;
            this.onComplete = onComplete;
            Waypoint start = path.get(0);
            person.getAvatar().setLayoutX(start.getX());
            person.getAvatar().setLayoutY(start.getY());
            person.getAvatar().setTranslateX(0);
            person.getAvatar().setTranslateY(0);
            person.moveTo(start);
            person.setState(Person.State.WALKING);
        }

        @Override
        public void handle(long now) {
            if (lastNs == 0) { lastNs = now; return; }
            double dt = (now - lastNs) / 1_000_000_000.0;
            lastNs = now;

            if (index >= path.size() - 1) { finish(); return; }

            Waypoint to = path.get(index + 1);
            double x = person.getAvatar().getLayoutX();
            double y = person.getAvatar().getLayoutY();
            double dx = to.getX() - x;
            double dy = to.getY() - y;
            double dist = Math.hypot(dx, dy);

            if (dist <= EPS) {
                // gearriveerd
                person.getAvatar().setLayoutX(to.getX());
                person.getAvatar().setLayoutY(to.getY());
                person.moveTo(to);
                if (onArrive != null) onArrive.accept(to);
                index++;
                if (index >= path.size() - 1) { finish(); }
                return;
            }

            double pxPerSec = Math.max(10.0, person.getSpeed());
            double step = pxPerSec * dt;
            if (step >= dist) {
                person.getAvatar().setLayoutX(to.getX());
                person.getAvatar().setLayoutY(to.getY());
            } else {
                double ux = dx / dist, uy = dy / dist;
                person.getAvatar().setLayoutX(x + ux * step);
                person.getAvatar().setLayoutY(y + uy * step);
            }
        }

        private void finish() {
            stop();
            person.setState(Person.State.DONE);
            if (onComplete != null) onComplete.run();
        }
    }
}
