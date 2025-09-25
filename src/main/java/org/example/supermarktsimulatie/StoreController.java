package org.example.supermarktsimulatie;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class StoreController {

    @FXML
    private ImageView imageView;

    @FXML
    private Pane root;

    @FXML
    private Label cursorLabel;

    private final InventoryManager inventoryManager = new InventoryManager();

    @FXML
    public void initialize() {
        root.setCursor(Cursor.CROSSHAIR);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.setPreserveRatio(true);

        root.setOnMouseMoved(event -> cursorLabel.setText(
                String.format("X: %.0f, Y: %.0f", event.getX(), event.getY())
        ));

        // Oneindige storage
        Storage storage = new Storage("Storage");
        storage.addProduct(new Product("Cola", 1));
        storage.addProduct(new Product("Water", 1));
        storage.addProduct(new Product("Chips", 1));
        storage.addProduct(new Product("Cookies", 1));

        // Normale shelves
        Shelf drinksShelf = new Shelf("Drinks");
        Shelf snacksShelf = new Shelf("Snacks");

        inventoryManager.restockShelf(storage, drinksShelf, "Cola", 10);
        inventoryManager.restockShelf(storage, drinksShelf, "Water", 15);
        inventoryManager.restockShelf(storage, snacksShelf, "Chips", 20);
        inventoryManager.restockShelf(storage, snacksShelf, "Cookies", 8);

        // Waypoints
        StoreMapController mapController = new StoreMapController(root);
        Waypoint[] path = mapController.setupMap(drinksShelf, snacksShelf);

        // Customer
        Customer alice = new Customer("Alice", path[0], inventoryManager);
        CustomerAnimationController animationController = new CustomerAnimationController(root);
        Circle aliceCircle = animationController.createCustomerCircle(path[0]);

        animationController.moveCustomerAlong(alice, aliceCircle, path, () -> {
            alice.takeProductFromShelf("Cookies");
            alice.showBasket();
            drinksShelf.showStock();
            snacksShelf.showStock();
        });
    }
}
