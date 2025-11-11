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

    public Product getProduct(String name) { return products.get(name); }

    public void resetStock() {
        for (Map.Entry<String,Integer> e : originalStock.entrySet()) {
            Product p = products.get(e.getKey());
            if (p != null) p.setStock(e.getValue());
        }
    }

    public String getName() { return name; }
}
