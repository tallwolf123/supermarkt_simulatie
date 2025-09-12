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

        Image image = new Image(getClass().getResourceAsStream("/afbeeldingen/farmershall_1stfloor.png"));

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        StackPane root = new StackPane(imageView);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        Scene scene = new Scene(root, screenWidth, screenHeight);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> scaleImage(imageView, scene));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> scaleImage(imageView, scene));

        // schermgrootte
        stage.setTitle("ImageView Fill Window");
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);

        stage.setScene(scene);
        stage.show();


        scaleImage(imageView, scene);
    }

    private void scaleImage(ImageView imageView, Scene scene) {
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        double imageWidth = imageView.getImage().getWidth();
        double imageHeight = imageView.getImage().getHeight();


        double scale = Math.max(sceneWidth / imageWidth, sceneHeight / imageHeight);

        imageView.setFitWidth(imageWidth * scale);
        imageView.setFitHeight(imageHeight * scale);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
