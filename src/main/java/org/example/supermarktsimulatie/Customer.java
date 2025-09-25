package org.example.supermarktsimulatie;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String name;
    private final List<Product> basket = new ArrayList<>();
    private Waypoint currentPosition;
    private final InventoryManager inventoryManager;

    public Customer(String name, Waypoint startPosition, InventoryManager inventoryManager) {
        this.name = name;
        this.currentPosition = startPosition;
        this.inventoryManager = inventoryManager;
    }

    public void moveTo(Waypoint waypoint) {
        System.out.println(name + " moves from "
                + (currentPosition != null ? currentPosition.getId() : "nowhere")
                + " to " + waypoint.getId());
        this.currentPosition = waypoint;
    }

    public void takeProductFromShelf(String productName) {
        if (currentPosition != null && currentPosition.hasShelf()) {
            Shelf shelf = currentPosition.getShelf();
            if (inventoryManager.takeFromShelf(shelf, productName)) {
                basket.add(new Product(productName, 1));
                System.out.println(name + " took " + productName + " from " + shelf.getName());
            } else {
                System.out.println(name + " tried to take " + productName + ", but it's out of stock!");
            }
        }
    }

    public void showBasket() {
        System.out.println(name + "'s basket:");
        for (Product p : basket) {
            System.out.println("- " + p.getName());
        }
    }
}
