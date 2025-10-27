package org.example.supermarktsimulatie;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.*;

public class StoreController {
    @FXML private ImageView imageView;
    @FXML private Pane root;
    @FXML private Label cursorLabel;

    private VBox hudBox = new VBox(8);
    private Map<String, Waypoint> waypoints;

    private static class CustomerRecord {
        final Customer customer;
        final Circle avatar;
        CustomerRecord(Customer c, Circle a){ customer=c; avatar=a; }
    }
    private final List<CustomerRecord> activeCustomers = new ArrayList<>();

    /** Onthoud per klant vanaf welk waypoint hij de checkout benadert (voor rijposities op het pad). */
    private final Map<Customer, Waypoint> checkoutPrev = new HashMap<>();

    @FXML
    public void initialize(){
        root.setCursor(Cursor.CROSSHAIR);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.setPreserveRatio(true);
        root.setOnMouseMoved(e -> cursorLabel.setText(
                String.format("X: %.0f, Y: %.0f", e.getX(), e.getY())
        ));

        StoreBuilder builder=new StoreBuilder(root);
        waypoints=builder.buildDefaultStore();

        hudBox.setLayoutX(600); hudBox.setLayoutY(20);
        hudBox.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-padding: 10; -fx-border-color: black;");
        root.getChildren().add(hudBox);

        Timeline spawner=new Timeline(new KeyFrame(Duration.seconds(3), e->spawnCustomer()));
        spawner.setCycleCount(Timeline.INDEFINITE);
        spawner.play();
    }

    private Waypoint chooseCheckout(){
        List<Waypoint> checkouts = Arrays.asList(
                waypoints.get("Checkout1"),
                waypoints.get("Checkout2"),
                waypoints.get("Checkout3")
        );
        return checkouts.stream()
                .min(Comparator.comparingInt(Waypoint::getQueueSize))
                .orElse(checkouts.get(0));
    }

    private void spawnCustomer(){
        String name="Customer-"+UUID.randomUUID().toString().substring(0,4);
        Customer c=new Customer(name, waypoints.get("Entrance"));
        Circle avatar=new Circle(10, Color.color(Math.random(),Math.random(),Math.random()));
        avatar.setTranslateX(c.getCurrentPosition().getX());
        avatar.setTranslateY(c.getCurrentPosition().getY());
        root.getChildren().add(avatar);
        activeCustomers.add(new CustomerRecord(c,avatar));

        // winkelboodschappenlijst genereren
        List<String> allProducts = Arrays.asList("Cola", "Water", "Chips", "Cookies");
        int items = 1 + new Random().nextInt(4); // 1 tot 4 producten
        Collections.shuffle(allProducts);
        for (int i=0; i<items; i++) {
            c.addToShoppingList(allProducts.get(i));
        }
        System.out.println(c.getName()+" shopping list: "+c.getShoppingListDescription());

        List<String> shelves=Arrays.asList("Drinks","Snacks");
        Collections.shuffle(shelves);
        Waypoint selectedCheckout=chooseCheckout();

        List<Waypoint> route=new ArrayList<>();
        route.add(waypoints.get("Entrance"));
        route.add(waypoints.get(shelves.get(0)));
        if(new Random().nextBoolean()) route.add(waypoints.get(shelves.get(1)));
        route.add(selectedCheckout);
        route.add(waypoints.get("Exit"));

        List<Waypoint> fullPath=buildPath(route);
        moveCustomerAlong(c,avatar,fullPath,1.5,new String[]{}); // shoppingList regelt producten
    }

    private List<Waypoint> buildPath(List<Waypoint> route){
        List<Waypoint> fullPath=new ArrayList<>();
        for(int i=0;i<route.size()-1;i++){
            fullPath.addAll(findPath(route.get(i),route.get(i+1)));
            if(i<route.size()-2) fullPath.remove(fullPath.size()-1);
        }
        return fullPath;
    }

    private List<Waypoint> findPath(Waypoint start,Waypoint end){
        Map<Waypoint,Waypoint> parent=new HashMap<>();
        Queue<Waypoint> q=new LinkedList<>();
        q.add(start); parent.put(start,null);

        while(!q.isEmpty()){
            Waypoint cur=q.poll();
            if(cur==end) break;
            for(Waypoint n:cur.getConnections()){
                if(!parent.containsKey(n)){
                    parent.put(n,cur); q.add(n);
                }
            }
        }

        List<Waypoint> path=new ArrayList<>();
        for(Waypoint step=end; step!=null; step=parent.get(step)) path.add(step);
        Collections.reverse(path);
        return path;
    }

    private void moveCustomerAlong(Customer c,Circle avatar,List<Waypoint> path,double speed,String[] preferredProducts){
        moveStep(c,avatar,path,1,speed,preferredProducts);
    }

    private void moveStep(Customer c,Circle avatar,List<Waypoint> path,int i,double speed,String[] preferredProducts){
        if(i>=path.size()){ customerLeaves(c,avatar); return; }
        Waypoint next=path.get(i);

        if(next.getType()==Waypoint.WaypointType.CHECKOUT){
            Waypoint prev=path.get(i-1);

            // Registreer benaderingspunt en zet currentPosition op checkout zodat de rijupdate hem ziet
            checkoutPrev.put(c, prev);
            c.moveTo(next); // -> zet state WAITING in Customer
            next.enqueueAtCheckout(c);

            int pos=next.getQueueSize()-1;
            double[] posXY=(pos==0) ? new double[]{next.getX(),next.getY()} : next.getQueuePositionOnPath(prev,pos);
            if(pos==0) c.setState(Customer.State.CHECKOUT);

            TranslateTransition tt=new TranslateTransition(Duration.seconds(speed),avatar);
            tt.setToX(posXY[0]); tt.setToY(posXY[1]);
            tt.setOnFinished(ev->{
                if(pos==0){
                    // eerste klant in de rij -> afrekenen starten
                    startCheckoutProcess(c,avatar,path,i,speed,preferredProducts,next);
                }
            });
            tt.play();
            return;
        }

        TranslateTransition tt=new TranslateTransition(Duration.seconds(speed),avatar);
        tt.setToX(next.getX()); tt.setToY(next.getY());
        tt.setOnFinished(e->{
            c.moveTo(next);
            if(next.hasShelf()){
                c.takeProductsFromShelf(); // meerdere producten
            }
            moveStep(c,avatar,path,i+1,speed,preferredProducts);
            updateHUD();
        });
        tt.play();
    }

    private void startCheckoutProcess(Customer c, Circle avatar, List<Waypoint> path, int i,
                                      double speed, String[] preferredProducts, Waypoint checkout){
        c.setState(Customer.State.CHECKOUT);
        Timeline checkoutTimeline=new Timeline(new KeyFrame(Duration.seconds(2),ev2->{
            checkout.dequeueFromCheckout();
            c.setState(Customer.State.DONE);
            checkoutPrev.remove(c);

            // animatie naar Exit
            Waypoint exit = waypoints.get("Exit");
            List<Waypoint> route = Arrays.asList(checkout, exit);
            List<Waypoint> fullPath = buildPath(route);

            // 🟢 Laat de klant via de waypoints lopen in plaats van in een rechte lijn
            moveCustomerAlong(c, avatar, fullPath, speed, preferredProducts);

            // update visuals van de rij
            updateCheckoutQueueVisual(checkout);
//            TranslateTransition tt = new TranslateTransition(Duration.seconds(speed), avatar);
//            tt.setToX(exit.getX());
//            tt.setToY(exit.getY());
//            tt.setOnFinished(e2 -> {
//                customerLeaves(c, avatar);
//                updateCheckoutQueueVisual(checkout); // schuif de volgende in de rij door
//            });
//            tt.play();
        }));
        checkoutTimeline.play();
    }

    /** Visualiseer en beheer schuiven van de wachtrij op basis van de echte queue-volgorde in Waypoint. */
    private void updateCheckoutQueueVisual(Waypoint checkout){
        List<Customer> order = checkout.getQueueSnapshot(); // FIFO volgorde
        for(int idx=0; idx<order.size(); idx++){
            Customer cust = order.get(idx);
            CustomerRecord rec = findRecord(cust);
            if(rec==null) continue;

            Waypoint prev = checkoutPrev.getOrDefault(cust, nearestNeighborTowards(checkout));

            if(idx==0){
                // eerste: naar de kassa bewegen (alleen de eerste keer als hij nog WACHTEND is)
                if(cust.getState() == Customer.State.WAITING) {
                    cust.setState(Customer.State.CHECKOUT);
                    TranslateTransition tt=new TranslateTransition(Duration.seconds(0.5), rec.avatar);
                    tt.setToX(checkout.getX()); tt.setToY(checkout.getY());
                    tt.setOnFinished(e -> {
                        // pas nu het afrekenproces starten
                        List<Waypoint> shortPath = buildPath(Arrays.asList(checkout, waypoints.get("Exit")));
                        startCheckoutProcess(cust, rec.avatar, shortPath, 0, 1.5, new String[]{}, checkout);
                    });
                    tt.play();
                }
                // als hij al CHECKOUT is, niet meer bewegen
            } else {
                // rest netjes op het pad
                double[] target = checkout.getQueuePositionOnPath(prev, idx);
                if(cust.getState()!=Customer.State.WAITING) cust.setState(Customer.State.WAITING);
                TranslateTransition tt=new TranslateTransition(Duration.seconds(0.3), rec.avatar);
                tt.setToX(target[0]); tt.setToY(target[1]);
                tt.play();
            }
        }
        updateHUD();
    }

    /** Vind de CustomerRecord voor een gegeven klant. */
    private CustomerRecord findRecord(Customer c){
        for(CustomerRecord r: activeCustomers) if(r.customer==c) return r;
        return null;
    }

    private Waypoint nearestNeighborTowards(Waypoint checkout){
        List<Waypoint> conns = checkout.getConnections();
        return conns.isEmpty() ? checkout : conns.get(0);
    }

    private void customerLeaves(Customer c,Circle avatar){
        root.getChildren().remove(avatar);
        activeCustomers.removeIf(cr->cr.customer==c);
        checkoutPrev.remove(c);
        updateHUD();
    }

    private void updateHUD(){
        hudBox.getChildren().clear();
        for(CustomerRecord cr:activeCustomers){
            hudBox.getChildren().add(new Label(
                    cr.customer.getName()
                            +" | Mandje: "+cr.customer.getBasketDescription()
                            +" | Nog te kopen: "+cr.customer.getShoppingListDescription()
                            +" | State: "+cr.customer.getState()
            ));
        }
    }
}
