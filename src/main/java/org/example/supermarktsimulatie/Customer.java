package org.example.supermarktsimulatie;

import java.util.List;
import java.util.ArrayList;

public class Customer {
    private final String name;
    private final List<Product> basket = new ArrayList<>();
    private Waypoint currentPosition;

    public Customer(String name, Waypoint startPosition) {
        this.name = name;
        this.currentPosition = startPosition;
    }

    public void moveTo(Waypoint waypoint) {
        System.out.println(name + " moves from "
                + (currentPosition != null ? currentPosition.getId() : "nowhere")
                + " to " + waypoint.getId());
        this.currentPosition = waypoint;

        if (waypoint.hasShelf()) {
            System.out.println(name + " reached shelf: " + waypoint.getShelf().getName());
        }
    }

    public void takeProductFromShelf(String productName) {
        if (currentPosition != null && currentPosition.hasShelf()) {
            Shelf shelf = currentPosition.getShelf();
            if (shelf.takeProduct(productName)) {
                basket.add(new Product(productName, 1));
                System.out.println(name + " took " + productName + " from " + shelf.getName());
            } else {
                System.out.println(name + " tried to take " + productName + ", but it's out of stock!");
            }
        } else {
            System.out.println(name + " is not at a shelf!");
        }
    }

    public void showBasket() {
        System.out.println(name + "'s basket:");
        for (Product p : basket) {
            System.out.println("- " + p.getName());
        }
    }

    public String getName() {
        return name;
    }
}
