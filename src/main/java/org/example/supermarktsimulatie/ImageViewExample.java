package org.example.supermarktsimulatie;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ImageViewExample extends Application {

    @Override
    public void start(Stage stage) {
        // Afbeelding laden vanuit resources (classpath)
        Image image = new Image(getClass().getResourceAsStream("/afbeeldingen/superman.png"));

        // ImageView maken en configureren
        ImageView imageView = new ImageView(image);
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(575);
        imageView.setPreserveRatio(true);

        // Scene maken
        Group root = new Group(imageView);
        Scene scene = new Scene(root, 595, 370);

        // Stage instellen
        stage.setTitle("Displaying Image");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
