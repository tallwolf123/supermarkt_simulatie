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
        products.merge(product.getName(), product, (oldP, newP) -> {
            oldP.addStock(newP.getStock());
            return oldP;
        });
    }

    public Product getProduct(String name) {
        return products.get(name);
    }

    public boolean takeFromShelf(String productName) {
        Product p = products.get(productName);
        if (p != null && p.getStock() > 0) {
            p.reduceStock(1);
            return true;
        }
        return false;
    }

    public void showStock() {
        System.out.println("Shelf " + name + " stock:");
        products.values().forEach(System.out::println);
    }

    public String getName() {
        return name;
    }
}
