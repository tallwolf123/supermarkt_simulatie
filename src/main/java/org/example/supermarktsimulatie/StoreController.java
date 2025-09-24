package org.example.supermarktsimulatie;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class StoreController {

    @FXML
    private ImageView imageView;

    @FXML
    private Pane root;

    @FXML
    public void initialize() {
        // Achtergrondafbeelding en cursor
        root.setCursor(Cursor.CROSSHAIR);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.setPreserveRatio(true);

        // Label om de muiscoördinaten te tonen
        javafx.scene.control.Label cursorLabel = new javafx.scene.control.Label();
        cursorLabel.setStyle("-fx-background-color: white; -fx-border-color: black;");
        cursorLabel.setLayoutX(10);
        cursorLabel.setLayoutY(10);
        root.getChildren().add(cursorLabel);

        // Mouse moved event
        root.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            cursorLabel.setText(String.format("X: %.0f, Y: %.0f", x, y));
        });

        // Shelves
        Shelf drinksShelf = new Shelf("Drinks");
        drinksShelf.addProduct(new Product("Cola", 10));
        drinksShelf.addProduct(new Product("Water", 15));

        Shelf snacksShelf = new Shelf("Snacks");
        snacksShelf.addProduct(new Product("Chips", 20));
        snacksShelf.addProduct(new Product("Cookies", 8));

        // Waypoints
        Waypoint entrance = new Waypoint("Entrance", 518, 194, root);
        Waypoint exit = new Waypoint("Exit", 392, 194, root);
        Waypoint drinksWP = new Waypoint("DrinksWP", 518, 268, drinksShelf, root);
        Waypoint snacksWP = new Waypoint("SnacksWP", 450, 268, snacksShelf, root);

        entrance.connectTo(drinksWP, root);
        drinksWP.connectTo(snacksWP, root);
        snacksWP.connectTo(exit, root);

        // Customer
        Customer alice = new Customer("Alice", entrance);
        Circle aliceCircle = new Circle(10, Color.ORANGE);
        aliceCircle.setTranslateX(entrance.getX());
        aliceCircle.setTranslateY(entrance.getY());
        root.getChildren().add(aliceCircle);

        moveCustomerAlong(alice, aliceCircle, new Waypoint[]{drinksWP, snacksWP, exit}, () -> {
            alice.takeProductFromShelf("Cookies");
            alice.showBasket();
            drinksShelf.showStock();
            snacksShelf.showStock();
        });
    }

    private void moveCustomerAlong(Customer customer, Circle circle, Waypoint[] path, Runnable onFinished) {
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
