package org.example.supermarktsimulatie;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    public enum State { WALKING, WAITING, CHECKOUT, DONE }

    private final String name;
    private final List<Product> basket = new ArrayList<>();
    private final List<String> shoppingList = new ArrayList<>();
    private Waypoint currentPosition;
    private State state = State.WALKING;

    public Customer(String name, Waypoint startPosition) {
        this.name = name;
        this.currentPosition = startPosition;
    }

    public void addToShoppingList(String product) {
        shoppingList.add(product);
    }

    public List<String> getShoppingList() {
        return shoppingList;
    }

    public void moveTo(Waypoint waypoint) {
        this.currentPosition = waypoint;
        if (waypoint.getType() == Waypoint.WaypointType.CHECKOUT) {
            this.state = State.WAITING;
        } else {
            this.state = State.WALKING;
        }
    }

    public void setState(State s) { this.state = s; }
    public State getState() { return state; }

    public void takeProductsFromShelf() {
        if (currentPosition != null && currentPosition.hasShelf()) {
            Shelf shelf = currentPosition.getShelf();
            List<String> taken = new ArrayList<>();
            for (String productName : shoppingList) {
                if (shelf.takeProduct(productName)) {
                    basket.add(new Product(productName, 1));
                    System.out.println(name + " took " + productName + " from " + shelf.getName());
                    taken.add(productName);
                }
            }
            // verwijder gekochte items uit shoppingList
            shoppingList.removeAll(taken);
        }
    }

    public String getBasketDescription() {
        if (basket.isEmpty()) return "(leeg)";
        StringBuilder sb = new StringBuilder();
        for (Product p : basket) sb.append(p.getName()).append(", ");
        return sb.substring(0, sb.length() - 2);
    }

    public String getShoppingListDescription() {
        if (shoppingList.isEmpty()) return "(alles gekocht)";
        return String.join(", ", shoppingList);
    }

    public String getName() { return name; }
    public Waypoint getCurrentPosition() { return currentPosition; }
}
