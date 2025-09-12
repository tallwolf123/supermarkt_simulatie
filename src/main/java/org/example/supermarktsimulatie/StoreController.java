package org.example.supermarktsimulatie;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class StoreController {

    @FXML
    private ImageView imageView;

    @FXML
    private StackPane root;

    @FXML
    public void initialize() {
        root.setCursor(Cursor.CROSSHAIR);

        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.setPreserveRatio(true);

        root.widthProperty().addListener((obs, oldVal, newVal) -> updateViewport());
        root.heightProperty().addListener((obs, oldVal, newVal) -> updateViewport());
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
