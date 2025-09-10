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
        // Afbeelding laden vanuit resources
        Image image = new Image(getClass().getResourceAsStream("/afbeeldingen/farmershall_1stfloor.png"));

        // ImageView maken
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(false); // niet behouden, zodat het venster volledig gevuld wordt

        // Scene maken
        Group root = new Group(imageView);
        Scene scene = new Scene(root, 800, 600); // startgrootte van het venster

        // ImageView binden aan de Scene, zodat het meegroeit
        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());

        // Stage instellen
        stage.setTitle("Dynamic ImageView Example");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
