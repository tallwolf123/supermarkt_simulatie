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

    public void addStock(int amount) {
        stock += amount;
    }

    public void reduceStock(int amount) {
        if (amount <= stock) stock -= amount;
        else stock = 0;
    }

    @Override
    public String toString() {
        return name + " (stock: " + stock + ")";
    }
}
