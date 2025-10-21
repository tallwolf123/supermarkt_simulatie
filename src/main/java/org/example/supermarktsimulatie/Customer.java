package org.example.supermarktsimulatie;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String name;
    private final List<Product> basket = new ArrayList<>();
    private Waypoint currentPosition;

    public Customer(String name, Waypoint startPosition) { this.name=name; this.currentPosition=startPosition; }

    public void moveTo(Waypoint waypoint) { this.currentPosition = waypoint; }
    public void takeProductFromShelf(String productName) {
        if(currentPosition!=null && currentPosition.hasShelf()) {
            Shelf s = currentPosition.getShelf();
            if(s.takeProduct(productName)) basket.add(new Product(productName,1));
        }
    }
    public String getBasketDescription() {
        if(basket.isEmpty()) return "(leeg)";
        StringBuilder sb=new StringBuilder();
        for(Product p:basket) sb.append(p.getName()).append(", ");
        return sb.substring(0,sb.length()-2);
    }
    public Waypoint getCurrentPosition() { return currentPosition; }
    public String getName() { return name; }
}
