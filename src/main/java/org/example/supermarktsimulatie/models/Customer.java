package org.example.supermarktsimulatie.models;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Customer extends Person {
    private final List<String> shoppingList = new ArrayList<>();

    public Customer(String name, Waypoint startPosition) {
        super(name, startPosition);
        this.color = Color.DEEPSKYBLUE;
    }

    public Customer(String name, Waypoint startPosition, Color color) {
        super(name, startPosition, color);
    }

    /** Simpel gedrag: bij shelf pak items van boodschappenlijst */
    @Override
    public void act() {
        if (currentPosition != null && currentPosition.hasShelf()) {
            Shelf shelf = currentPosition.getShelf();
            for (Iterator<String> it = shoppingList.iterator(); it.hasNext(); ) {
                String wanted = it.next();
                Product p = shelf.getProduct(wanted);
                if (p != null && p.takeFromShelf()) {
                    basket.add(new Product(p.getName(), 1));
                    it.remove();
                }
            }
            state = shoppingList.isEmpty() ? State.DONE : State.SHOPPING;
        }
    }

    public void addToShoppingList(String... items) {
        for (String s : items) if (s != null) shoppingList.add(s);
    }

    public String getShoppingListDescription() {
        return shoppingList.isEmpty() ? "(alles gekocht)" : String.join(", ", shoppingList);
    }
}
