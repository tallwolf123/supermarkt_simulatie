package org.example.supermarktsimulatie;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class CustomerAnimationController {
    private final Pane root;

    public CustomerAnimationController(Pane root) {
        this.root = root;
    }

    public Circle createCustomerCircle(Waypoint start) {
        Circle circle = new Circle(10, Color.ORANGE);
        circle.setTranslateX(start.getX());
        circle.setTranslateY(start.getY());
        root.getChildren().add(circle);
        return circle;
    }

    public void moveCustomerAlong(Customer customer, Circle circle, Waypoint[] path, Runnable onFinished) {
        moveStep(customer, circle, path, 0, onFinished);
    }

    private void moveStep(Customer customer, Circle circle, Waypoint[] path, int index, Runnable onFinished) {
        if (index >= path.length) {
            if (onFinished != null) onFinished.run();
            return;
        }
        Waypoint next = path[index];
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.5), circle);
        tt.setToX(next.getX());
        tt.setToY(next.getY());
        tt.setOnFinished(e -> {
            customer.moveTo(next);
            if (next.hasShelf()) customer.takeProductFromShelf("Cola");
            moveStep(customer, circle, path, index + 1, onFinished);
        });
        tt.play();
    }
}
