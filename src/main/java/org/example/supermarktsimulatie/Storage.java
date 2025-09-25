package org.example.supermarktsimulatie;

public class Storage extends Shelf {

    public Storage(String name) {
        super(name);
    }

    @Override
    public boolean takeFromShelf(String productName) {
        if (getProduct(productName) != null) {
            System.out.println("Storage provides " + productName + " (infinite stock)");
            return true; // voorraad raakt nooit op
        }
        return false;
    }
}
