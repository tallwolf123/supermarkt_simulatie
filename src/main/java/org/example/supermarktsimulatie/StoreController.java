package org.example.supermarktsimulatie;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.PathTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class StoreController {

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane root;

    @FXML
    public void initialize() {
        // --- Existing ImageView logic ---
        root.setCursor(Cursor.CROSSHAIR);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.setPreserveRatio(false);
        root.widthProperty().addListener((obs, oldVal, newVal) -> updateViewport());
        root.heightProperty().addListener((obs, oldVal, newVal) -> updateViewport());

//        Label coordLabel = new Label();
//        coordLabel.setStyle("-fx-background-color: white; -fx-padding: 4; -fx-border-color: black;");
//        coordLabel.setLayoutX(10);
//        coordLabel.setLayoutY(10);
//        root.getChildren().add(coordLabel);
//
//        root.setOnMouseMoved(event -> {
//            double mouseX = event.getX();
//            double mouseY = event.getY();
//
//            double imageWidth = imageView.getImage().getWidth();
//            double imageHeight = imageView.getImage().getHeight();
//            double paneWidth = root.getWidth();
//            double paneHeight = root.getHeight();
//
//            // Calculate scale to fit the image inside StackPane while preserving ratio
//            double scale = Math.min(paneWidth / imageWidth, paneHeight / imageHeight);
//
//            // Size of displayed image
//            double displayedWidth = imageWidth * scale;
//            double displayedHeight = imageHeight * scale;
//
//            // Letterboxing offsets
//            double offsetX = (paneWidth - displayedWidth) / 2;
//            double offsetY = (paneHeight - displayedHeight) / 2;
//
//            // Mouse relative to top-left of displayed image
//            double relativeX = mouseX - offsetX;
//            double relativeY = mouseY - offsetY;
//
//            // Clamp to image bounds
//            relativeX = Math.max(0, Math.min(displayedWidth, relativeX));
//            relativeY = Math.max(0, Math.min(displayedHeight, relativeY));
//
//            // Convert to original image coordinates
//            double imageX = relativeX / scale;
//            double imageY = relativeY / scale;
//
//            coordLabel.setText(String.format("X: %.1f, Y: %.1f", imageX, imageY));
//
//            // Optional: make label follow cursor
//            coordLabel.setLayoutX(mouseX + 15);
//            coordLabel.setLayoutY(mouseY + 15);
//        });



        // --- Setup shelves ---
        Shelf drinksShelf = new Shelf("Drinks");
        drinksShelf.addProduct(new Product("Cola", 10));
        drinksShelf.addProduct(new Product("Water", 15));

        Shelf snacksShelf = new Shelf("Snacks");
        snacksShelf.addProduct(new Product("Chips", 20));
        snacksShelf.addProduct(new Product("Cookies", 8));

        // --- Setup waypoints ---
        Waypoint entrance = new Waypoint("Entrance", 70, -200);
        Waypoint drinksWP = new Waypoint("DrinksWP", 70, -120, drinksShelf);
        Waypoint snacksWP = new Waypoint("SnacksWP", 20, -120, snacksShelf);

        // Add waypoint circles to root automatically
        root.getChildren().addAll(
                entrance.getCircle(),
                drinksWP.getCircle(),
                snacksWP.getCircle()
        );

        // --- Add labels ---
        Label drinksLabel = new Label(drinksWP.getId());
        drinksLabel.setLayoutX(drinksWP.getX() + 12);
        drinksLabel.setLayoutY(drinksWP.getY() - 5);

        // --- Customer representation ---
        Circle aliceCircle = new Circle(10, Color.ORANGE);
        aliceCircle.setTranslateX(entrance.getX());
        aliceCircle.setTranslateY(entrance.getY());
        root.getChildren().add(aliceCircle);

        // --- Create customer ---
        Customer alice = new Customer("Alice", entrance);

        // --- Animate customer moving to drinks shelf ---
        moveCustomer(alice, aliceCircle, drinksWP, () -> {
            alice.takeProductFromShelf("Cola");

            // After picking Cola, move to snacks shelf
            moveCustomer(alice, aliceCircle, snacksWP, () -> {
                alice.takeProductFromShelf("Cookies");

                // Show results in console
                alice.showBasket();
                drinksShelf.showStock();
                snacksShelf.showStock();
            });
        });
    }

    // Helper method to animate movement and update customer position
    private void moveCustomer(Customer customer, Circle circle, Waypoint waypoint, Runnable onFinished) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1.5), circle);
        tt.setToX(waypoint.getX());
        tt.setToY(waypoint.getY());
        tt.setOnFinished(e -> {
            customer.moveTo(waypoint);
            if (onFinished != null) onFinished.run();
        });
        tt.play();
    }

    private void updateViewport() {
        double vw = root.getWidth();
        double vh = root.getHeight();
        double iw = imageView.getImage().getWidth();
        double ih = imageView.getImage().getHeight();

        double scale = Math.max(vw / iw, vh / ih);

        double sw = vw / scale;
        double sh = vh / scale;

        double sx = (iw - sw) / 2;
        double sy = (ih - sh) / 2;

        imageView.setViewport(new Rectangle2D(sx, sy, sw, sh));
    }
}
