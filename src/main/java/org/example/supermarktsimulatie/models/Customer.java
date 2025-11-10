package org.example.supermarktsimulatie.models;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
    private final List<String> shoppingList = new ArrayList<>();

    public Customer(String name, Waypoint startPosition) {
        super(name, startPosition);
    }

    /** Klantgedrag: check of er producten te halen zijn */
    @Override
    public void act() {
        if (currentPosition != null && currentPosition.hasShelf()) {
            grabFrom(currentPosition.getShelf());
        }
    }

    public void addToShoppingList(String product) {
        shoppingList.add(product);
    }

    public void addAllToShoppingList(List<String> products) {
        shoppingList.addAll(products);
    }

    public List<String> getShoppingListSnapshot() {
        return List.copyOf(shoppingList);
    }

    private void grabFrom(Shelf shelf) {
        List<String> bought = new ArrayList<>();
        for (String productName : new ArrayList<>(shoppingList)) {
            if (shelf.takeProduct(productName)) {
                basket.add(new Product(productName, 1));
                bought.add(productName);
                System.out.printf("%s kocht %s van %s%n", name, productName, shelf.getName());
            }
        }
        shoppingList.removeAll(bought);
    }

    public String getShoppingListDescription() {
        return shoppingList.isEmpty() ? "(alles gekocht)" : String.join(", ", shoppingList);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] | Mandje: %s | Nog te kopen: %s",
                name, state, getBasketDescription(), getShoppingListDescription());
    }
}
