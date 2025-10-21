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
    private final List<CustomerRecord> activeCustomers = new ArrayList<>();

    private static class CustomerRecord { Customer customer; Circle avatar; CustomerRecord(Customer c,Circle a){this.customer=c;this.avatar=a;} }

    @FXML
    public void initialize() {
        root.setCursor(Cursor.CROSSHAIR);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.setPreserveRatio(true);
        root.setOnMouseMoved(e->cursorLabel.setText(String.format("X: %.0f, Y: %.0f",e.getX(),e.getY())));

        StoreBuilder builder = new StoreBuilder(root);
        waypoints = builder.buildDefaultStore();

        hudBox.setLayoutX(600); hudBox.setLayoutY(20);
        hudBox.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-padding: 10; -fx-border-color: black;");
        root.getChildren().add(hudBox);

        Timeline spawner=new Timeline(new KeyFrame(Duration.seconds(3),e->spawnCustomer()));
        spawner.setCycleCount(Timeline.INDEFINITE);
        spawner.play();
    }

    private Waypoint chooseCheckout() {
        List<Waypoint> checkouts = Arrays.asList(
                waypoints.get("Checkout1"),
                waypoints.get("Checkout2"),
                waypoints.get("Checkout3")
        );
        return checkouts.stream().min(Comparator.comparingInt(Waypoint::getQueueSize)).orElse(checkouts.get(0));
    }

    private void spawnCustomer(){
        String name="Customer-"+UUID.randomUUID().toString().substring(0,4);
        CustomerBuilder.BuiltCustomer built=new CustomerBuilder()
                .withName(name)
                .atStart(waypoints.get("Entrance"))
                .withSpeed(1.2+Math.random()*0.8)
                .withColor(Color.color(Math.random(),Math.random(),Math.random()))
                .withPreferredProducts("Cola","Cookies","Chips","Water")
                .build();

        Customer c=built.getCustomer();
        Circle avatar=new Circle(10,built.getColor());
        avatar.setTranslateX(c.getCurrentPosition().getX());
        avatar.setTranslateY(c.getCurrentPosition().getY());
        root.getChildren().add(avatar);
        activeCustomers.add(new CustomerRecord(c,avatar));

        List<String> shelves=Arrays.asList("Drinks","Snacks");
        Collections.shuffle(shelves);

        Waypoint selectedCheckout = chooseCheckout();

        List<Waypoint> route=new ArrayList<>();
        route.add(waypoints.get("Entrance"));
        route.add(waypoints.get(shelves.get(0)));
        if(new Random().nextBoolean()) route.add(waypoints.get(shelves.get(1)));
        route.add(selectedCheckout);
        route.add(waypoints.get("Exit"));

        List<Waypoint> fullPath=buildPath(route);
        moveCustomerAlongWithSpeed(c,avatar,fullPath,built.getSpeed(),built.getPreferredProducts());
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
                if(!parent.containsKey(n)){ parent.put(n,cur); q.add(n); }
            }
        }
        List<Waypoint> path=new ArrayList<>();
        for(Waypoint step=end; step!=null; step=parent.get(step)) path.add(step);
        Collections.reverse(path);
        return path;
    }

    private void moveCustomerAlongWithSpeed(Customer c,Circle avatar,List<Waypoint> path,double speed,String[] preferredProducts){
        moveStepWithSpeed(c,avatar,path,1,speed,preferredProducts);
    }

    private void moveStepWithSpeed(Customer c,Circle avatar,List<Waypoint> path,int i,double speed,String[] preferredProducts){
        if(i>=path.size()){ customerLeaves(c,avatar); return; }
        Waypoint next=path.get(i);
        TranslateTransition tt=new TranslateTransition(Duration.seconds(speed),avatar);
        tt.setToX(next.getX()); tt.setToY(next.getY());
        tt.setOnFinished(e->{
            c.moveTo(next);
            if(next.hasShelf()){
                Shelf s=next.getShelf();
                for(String p:preferredProducts){ if(s.takeProduct(p)){ c.takeProductFromShelf(p); break; } }
            }

            if(next.getType()==Waypoint.WaypointType.CHECKOUT){
                next.enqueueAtCheckout(c);
                int pos=next.getQueueSize();
                Timeline wait=new Timeline(new KeyFrame(Duration.seconds(2*pos),ev->{ next.dequeueFromCheckout(); moveStepWithSpeed(c,avatar,path,i+1,speed,preferredProducts); }));
                wait.play();
            } else moveStepWithSpeed(c,avatar,path,i+1,speed,preferredProducts);

            updateHUD();
        });
        tt.play();
    }

    private void customerLeaves(Customer c,Circle avatar){
        root.getChildren().remove(avatar);
        activeCustomers.removeIf(cr->cr.customer==c);
        updateHUD();
    }

    private void updateHUD(){
        hudBox.getChildren().clear();
        for(CustomerRecord cr:activeCustomers){
            Label label=new Label(cr.customer.getName()+"\nMandje: "+cr.customer.getBasketDescription());
            hudBox.getChildren().add(label);
        }
    }
}
