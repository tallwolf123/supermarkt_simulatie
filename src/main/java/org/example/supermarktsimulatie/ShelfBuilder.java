package org.example.supermarktsimulatie;

public class ShelfBuilder {
    private final Shelf shelf;
    public ShelfBuilder(String name){ shelf=new Shelf(name); }
    public ShelfBuilder addProduct(String name,int stock){ shelf.addProduct(new Product(name,stock)); return this; }
    public Shelf build(){ return shelf; }
}
