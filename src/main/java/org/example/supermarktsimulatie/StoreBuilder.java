package org.example.supermarktsimulatie;

import javafx.scene.layout.Pane;
import java.util.Map;

public class StoreBuilder {
    private final Pane root;
    public StoreBuilder(Pane root) { this.root = root; }

    public Map<String, Waypoint> buildDefaultStore() {
        Shelf drinks = new Shelf("Drinks");
        drinks.addProduct(new Product("Cola", 10));
        drinks.addProduct(new Product("Water", 15));

        Shelf snacks = new Shelf("Snacks");
        snacks.addProduct(new Product("Chips", 20));
        snacks.addProduct(new Product("Cookies", 8));

        return new WaypointBuilder(root)
                .add("Entrance", 518, 194)
                .add("Drinks", 518, 268, drinks, Waypoint.WaypointType.SHELF)
                .add("Snacks", 450, 268, snacks, Waypoint.WaypointType.SHELF)
                .add("Checkout1", 470, 350, null, Waypoint.WaypointType.CHECKOUT)
                .add("Checkout2", 500, 350, null, Waypoint.WaypointType.CHECKOUT)
                .add("Checkout3", 530, 350, null, Waypoint.WaypointType.CHECKOUT)
                .add("Exit", 392, 194)
                .connect("Entrance", "Drinks")
                .connect("Drinks", "Snacks")
                .connect("Snacks", "Checkout1")
                .connect("Snacks", "Checkout2")
                .connect("Snacks", "Checkout3")
                .connect("Checkout1", "Exit")
                .connect("Checkout2", "Exit")
                .connect("Checkout3", "Exit")
                .build();
    }
}
