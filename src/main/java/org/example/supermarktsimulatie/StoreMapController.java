package org.example.supermarktsimulatie;

import javafx.scene.layout.Pane;

public class StoreMapController {
    private final Pane root;

    public StoreMapController(Pane root) {
        this.root = root;
    }

    public Waypoint[] setupMap(Shelf drinksShelf, Shelf snacksShelf) {
        Waypoint entrance = new Waypoint("Entrance", 518, 194, root);
        Waypoint drinksWP = new Waypoint("DrinksWP", 518, 268, drinksShelf, root);
        Waypoint snacksWP = new Waypoint("SnacksWP", 450, 268, snacksShelf, root);
        Waypoint exit = new Waypoint("Exit", 392, 194, root);

        entrance.connectTo(drinksWP, root);
        drinksWP.connectTo(snacksWP, root);
        snacksWP.connectTo(exit, root);

        return new Waypoint[]{entrance, drinksWP, snacksWP, exit};
    }
}
