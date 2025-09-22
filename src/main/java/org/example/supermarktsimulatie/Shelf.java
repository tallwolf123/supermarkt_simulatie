package org.example.supermarktsimulatie;

import java.util.HashMap;
import java.util.Map;

public class Shelf {
    private final String name;
    private final Map<String, Product> products = new HashMap<>();

    public Shelf(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {
        products.put(product.getName(), product);
    }

    public boolean takeProduct(String productName) {
        Product p = products.get(productName);
        if (p != null) {
            return p.takeFromShelf();
        }
        return false;
    }

    public void showStock() {
        System.out.println("Stock of shelf " + name + ":");
        products.values().forEach(System.out::println);
    }

    public String getName() {
        return name;
    }
}
