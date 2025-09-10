package org.example.supermarktsimulatie;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ImageViewExample extends Application {

    @Override
    public void start(Stage stage) {
        // Afbeelding laden vanuit resources
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/afbeeldingen/farmershall_1stfloor.png")));
        imageView.setPreserveRatio(true);

        // Root container die de afbeelding centreert
        StackPane root = new StackPane(imageView);

        // Schermgrootte ophalen en Scene maken
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());

        // Dynamisch schalen
        scene.widthProperty().addListener((obs, oldVal, newVal) -> scaleImage(imageView, scene));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> scaleImage(imageView, scene));

        // Stage configureren
        stage.setTitle("ImageView");
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setScene(scene);
        stage.show();

        // Initiale schaal toepassen
        scaleImage(imageView, scene);
    }

    private void scaleImage(ImageView imageView, Scene scene) {
        double scale = Math.max(scene.getWidth() / imageView.getImage().getWidth(),
                scene.getHeight() / imageView.getImage().getHeight());
        imageView.setFitWidth(imageView.getImage().getWidth() * scale);
        imageView.setFitHeight(imageView.getImage().getHeight() * scale);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
