package org.example.supermarktsimulatie.builders;

import javafx.scene.layout.Pane;
import org.example.supermarktsimulatie.models.Product;
import org.example.supermarktsimulatie.models.Shelf;
import org.example.supermarktsimulatie.models.Waypoint;

import java.util.Map;

public class StoreBuilder {
    private final Pane root;
    public StoreBuilder(Pane root) { this.root = root; }

    public Map<String, Waypoint> buildDefaultStore() {
        Shelf drinks = new Shelf("Drinks");
        drinks.addProduct(new Product("Cola", 10));
        drinks.addProduct(new Product("Water", 15));
        drinks.addProduct(new Product("Milk", 20));

        Shelf snacks = new Shelf("Snacks");
        snacks.addProduct(new Product("Chips", 20));
        snacks.addProduct(new Product("Cookies", 8));

        Shelf storage = new Shelf("Storage");
        storage.addProduct(new Product("Stock"));

        return new WaypointBuilder(root)
                .add("Entrance", 518, 194)
                .add("Drinks", 518, 268, drinks, Waypoint.WaypointType.SHELF)
                .add("Snacks", 450, 268, snacks, Waypoint.WaypointType.SHELF)
                .add("Milk", 400, 267, drinks, Waypoint.WaypointType.SHELF)
                .add("Checkout1", 470, 350, null, Waypoint.WaypointType.CHECKOUT)
                .add("Checkout2", 500, 350, null, Waypoint.WaypointType.CHECKOUT)
                .add("Checkout3", 530, 350, null, Waypoint.WaypointType.CHECKOUT)
                .add("Storage", 450, 400, storage, Waypoint.WaypointType.EMPLOYEEONLY)
                .add("Exit", 392, 194)
                .connect("Entrance", "Drinks")
                .connect("Drinks", "Snacks")
                .connect("Snacks", "Checkout1")
                .connect("Snacks", "Checkout2")
                .connect("Snacks", "Checkout3")
                .connect("Snacks", "Milk")
                .connect("Milk", "Storage")
                .connect("Milk", "Exit")
//                .connect("Checkout1", "Exit")
//                .connect("Checkout2", "Exit")
//                .connect("Checkout3", "Exit")
                .build();
    }
}
