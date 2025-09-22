package org.example.supermarktsimulatie;

public class Product {
    private final String name;
    private int stock;

    public Product(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public boolean takeFromShelf() {
        if (stock > 0) {
            stock--;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (stock: " + stock + ")";
    }
}
