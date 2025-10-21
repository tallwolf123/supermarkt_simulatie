package org.example.supermarktsimulatie;

import java.util.HashMap;
import java.util.Map;

public class Shelf {
    private final String name;
    private final Map<String, Product> products = new HashMap<>();

    public Shelf(String name) { this.name = name; }

    public void addProduct(Product product) { products.put(product.getName(), product); }

    public boolean takeProduct(String productName) {
        Product p = products.get(productName);
        return p != null && p.takeFromShelf();
    }

    public Map<String, Product> getProducts() { return products; }
    public String getName() { return name; }
}
