package org.example.supermarktsimulatie.models;

public class Product {
    private final String name;
    private int stock;

    public Product(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }
    public Product(String name) {
        this.name = name;
        this.stock = 10000;
    }

    public String getName() { return name; }
    public int getStock() { return stock; }

    public void setStock(int stock) {
        this.stock = stock;
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
