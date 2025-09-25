package org.example.supermarktsimulatie;

public class InventoryManager {

    public boolean takeFromShelf(Shelf shelf, String productName) {
        return shelf.takeFromShelf(productName);
    }

    public boolean restockShelf(Storage storage, Shelf shelf, String productName, int amount) {
        if (storage.takeFromShelf(productName)) {
            shelf.addProduct(new Product(productName, amount));
            System.out.println("Restocked " + productName + " (" + amount + ") to shelf " + shelf.getName());
            return true;
        }
        return false;
    }
}
