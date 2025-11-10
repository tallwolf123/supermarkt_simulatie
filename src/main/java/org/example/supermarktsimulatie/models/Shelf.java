package org.example.supermarktsimulatie.models;

import java.util.HashMap;
import java.util.Map;

public class Shelf {
    private final String name;
    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, Integer> originalStock = new HashMap<>();


    public Shelf(String name) { this.name = name; }

    public void addProduct(Product product) {
        products.put(product.getName(), product);
        originalStock.put(product.getName(), product.getStock());
    }

    public boolean takeProduct(String productName) {
        Product p = products.get(productName);
        if (p != null) return p.takeFromShelf();
        return false;
    }

    public void resetStock() {
        for (Map.Entry<String, Integer> entry : originalStock.entrySet()) {
            Product p = products.get(entry.getKey());
            if (p != null) {
                p.setStock(entry.getValue());
            }
        }
        System.out.println("De voorraad van shelf '" + name + "' is hersteld.");
    }

    public void showStock() {
        System.out.println("Stock of shelf " + name + ":");
        products.values().forEach(System.out::println);
    }

    public String getName() { return name; }
}
