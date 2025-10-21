package org.example.supermarktsimulatie;

import javafx.scene.paint.Color;

public class CustomerBuilder {
    private String name="Customer";
    private Waypoint startPosition;
    private double speed=1.0;
    private Color color=Color.ORANGE;
    private String[] preferredProducts = new String[0];

    public CustomerBuilder withName(String name){ this.name=name; return this; }
    public CustomerBuilder atStart(Waypoint startPosition){ this.startPosition=startPosition; return this; }
    public CustomerBuilder withSpeed(double speed){ this.speed=speed; return this; }
    public CustomerBuilder withColor(Color color){ this.color=color; return this; }
    public CustomerBuilder withPreferredProducts(String...products){ this.preferredProducts=products; return this; }

    public BuiltCustomer build(){
        if(startPosition==null) throw new IllegalStateException("Startpositie niet ingesteld voor "+name);
        Customer customer=new Customer(name,startPosition);
        return new BuiltCustomer(customer,speed,color,preferredProducts);
    }

    public static class BuiltCustomer{
        private final Customer customer;
        private final double speed;
        private final Color color;
        private final String[] preferredProducts;
        public BuiltCustomer(Customer c,double s,Color col,String[] products){ this.customer=c; this.speed=s; this.color=col; this.preferredProducts=products; }
        public Customer getCustomer(){return customer;}
        public double getSpeed(){return speed;}
        public Color getColor(){return color;}
        public String[] getPreferredProducts(){return preferredProducts;}
    }
}
