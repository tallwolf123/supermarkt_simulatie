package org.example.supermarktsimulatie;

import javafx.scene.layout.Pane;
import java.util.Map;

public class StoreBuilder {
    private final Pane root;
    public StoreBuilder(Pane root) { this.root = root; }

    public Map<String, Waypoint> buildDefaultStore() {
        Shelf drinksShelf = new ShelfBuilder("Drinks").addProduct("Cola",10).addProduct("Water",15).build();
        Shelf snacksShelf = new ShelfBuilder("Snacks").addProduct("Chips",20).addProduct("Cookies",8).build();

        return new WaypointBuilder(root)
                .add("Entrance",518,194)
                .add("Drinks",518,268,drinksShelf,Waypoint.WaypointType.SHELF)
                .add("Snacks",450,268,snacksShelf,Waypoint.WaypointType.SHELF)
                .add("Checkout1",427,263,null,Waypoint.WaypointType.CHECKOUT)
                .add("Checkout2",427,215,null,Waypoint.WaypointType.CHECKOUT)
                .add("Checkout3",427,315,null,Waypoint.WaypointType.CHECKOUT)
                .add("Exit",392,194)
                .connect("Entrance","Drinks")
                .connect("Drinks","Snacks")
                .connect("Snacks","Checkout1")
                .connect("Snacks","Checkout2")
                .connect("Snacks","Checkout3")
                .connect("Checkout1","Exit")
                .connect("Checkout2","Exit")
                .connect("Checkout3","Exit")
                .build();
    }
}
