package org.example.supermarktsimulatie.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.example.supermarktsimulatie.builders.WaypointBuilder;
import org.example.supermarktsimulatie.models.Customer;
import org.example.supermarktsimulatie.models.Person;
import org.example.supermarktsimulatie.models.Product;
import org.example.supermarktsimulatie.models.Shelf;
import org.example.supermarktsimulatie.models.Waypoint;
import org.example.supermarktsimulatie.services.EntityManager;
import org.example.supermarktsimulatie.services.MovementService;
import org.example.supermarktsimulatie.services.RoutePlanner;

import java.util.*;

/**
 * StoreController – bouwt waypoints/shelves, spawnt klanten en laat ze bewegen.
 * Tekent tevens waypoints/edges en highlight het actieve pad.
 *
 * Vereist:
 *  - MovementService.moveAlong(Person, List<Waypoint>, java.util.function.Consumer<Waypoint>, Runnable)
 *  - RoutePlanner.shortestPath(Waypoint, Waypoint)
 *  - Waypoint.connectTo(...) is bidirectioneel
 */
public class StoreController {

    @FXML private Pane root;
    @FXML private ImageView imageView;
    @FXML private VBox hudBox;

    private Map<String, Waypoint> waypoints;
    private final Map<String, Shelf> shelves = new HashMap<>();
    private final List<Customer> activeCustomers = new ArrayList<>();
    private final Deque<Customer> checkoutQueue = new ArrayDeque<>();

    private RoutePlanner routePlanner;
    private MovementService movementService;
    @SuppressWarnings("unused")
    private EntityManager entityManager; // momenteel niet gebruikt voor spawn (we spawnen direct met Circle)

    @FXML
    public void initialize() {
        // UI basis
        root.setCursor(Cursor.CROSSHAIR);
        if (imageView != null) {
            imageView.fitWidthProperty().bind(root.widthProperty());
            imageView.fitHeightProperty().bind(root.heightProperty());
        }

        // 1) Waypoints + shelves opbouwen
        WaypointBuilder wb = new WaypointBuilder(root)
                .add("Entrance", 50, 300)
                .add("Exit",     750, 300)
                .add("A1",      200, 220)
                .add("A2",      300, 220)
                .add("B1",      200, 380)
                .add("B2",      300, 380)
                .add("Checkout", 600, 300);

        // Shelves + voorraad
        Shelf fruit = new Shelf("Fruit");
        fruit.addProduct(new Product("Appel", 10));
        fruit.addProduct(new Product("Banaan", 8));
        shelves.put("Fruit", fruit);
        wb.add("SHELF_FRUIT", 250, 220, fruit, Waypoint.WaypointType.SHELF);

        Shelf snacks = new Shelf("Snacks");
        snacks.addProduct(new Product("Chips", 12));
        shelves.put("Snacks", snacks);
        wb.add("SHELF_SNACKS", 250, 380, snacks, Waypoint.WaypointType.SHELF);

        // Verbindingen (tweerichtings via Waypoint.connectTo)
        wb.connect("Entrance", "A1").connect("A1", "A2").connect("A2", "Checkout");
        wb.connect("Entrance", "B1").connect("B1", "B2").connect("B2", "Checkout");
        wb.connect("Checkout", "Exit");

        // Shelves aansluiten op de "gang"
        wb.connect("A2", "SHELF_FRUIT");
        wb.connect("B2", "SHELF_SNACKS");
        // (optioneel extra aansluitingen:)
        wb.connect("A1", "SHELF_FRUIT");
        wb.connect("B1", "SHELF_SNACKS");

        waypoints = wb.build();

        // DEBUG basis
        System.out.println("Waypoints count = " + waypoints.size());
        System.out.println("IDs = " + waypoints.keySet());
        if (!waypoints.containsKey("Entrance") || !waypoints.containsKey("Checkout")) {
            throw new IllegalStateException("Verwachte waypoints ontbreken (Entrance/Checkout)!");
        }

        // 2) Services
        routePlanner    = new RoutePlanner(waypoints); // Map constructor
        movementService = new MovementService();
        entityManager   = new EntityManager(root, waypoints);

        // 3) Teken waypoints/edges op de scene
        drawWaypointsAndEdges();

        // 4) Spawn om de seconde, 3 klanten
        Timeline spawner = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> spawnRandomCustomer()));
        spawner.setCycleCount(3);
        spawner.play();

        // 5) HUD elke 0.5s verversen
        Timeline hud = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> refreshHUD()));
        hud.setCycleCount(Timeline.INDEFINITE);
        hud.play();
    }

    /** Maakt een klant, zet avatar neer, berekent pad en start beweging. */
    private void spawnRandomCustomer() {
        Waypoint entrance = waypoints.get("Entrance");
        if (entrance == null) {
            System.err.println("Entrance waypoint ontbreekt!");
            return;
        }

        Customer c = new Customer(randomName(), entrance, Color.DEEPSKYBLUE);
        c.addToShoppingList(pickTwo("Appel", "Chips", "Banaan"));

        // Avatar aan root hangen (layoutX/layoutY!)
        Circle avatar = new Circle(8, c.getColor());
        avatar.setLayoutX(entrance.getX());
        avatar.setLayoutY(entrance.getY());
        root.getChildren().add(avatar);
        c.setAvatar(avatar);

        activeCustomers.add(c);

        // Route samenstellen: Entrance -> (random shelf) -> Checkout -> Exit
        List<Waypoint> route = new ArrayList<>();
        route.add(waypoints.get("Entrance"));
        route.add(waypoints.get(new Random().nextBoolean() ? "SHELF_FRUIT" : "SHELF_SNACKS"));
        route.add(waypoints.get("Checkout"));
        route.add(waypoints.get("Exit"));

        List<Waypoint> fullPath = buildPath(route);

        // Highlight het actieve pad (oranje) ter visualisatie
        highlightPath(fullPath);

        moveCustomer(c, fullPath);
    }

    /** Concateneert kortste paden tussen opeenvolgende routepunten. */
    private List<Waypoint> buildPath(List<Waypoint> route) {
        List<Waypoint> full = new ArrayList<>();
        for (int i = 0; i < route.size() - 1; i++) {
            Waypoint from = route.get(i);
            Waypoint to   = route.get(i + 1);
            if (from == null || to == null) {
                System.out.println("⚠️ routepunt ontbreekt: " + (from == null ? "from=null" : from.getId())
                        + " -> " + (to == null ? "to=null" : to.getId()));
                continue;
            }
            List<Waypoint> leg = routePlanner.shortestPath(from, to);
            if (leg.size() < 2) {
                System.out.println("⚠️ geen pad tussen " + from.getId() + " en " + to.getId());
                continue;
            }
            if (full.isEmpty()) full.addAll(leg);
            else full.addAll(leg.subList(1, leg.size())); // overlap (from) overslaan
        }
        System.out.println("Path length = " + full.size());
        return full;
    }

    /** Start de beweging; roept act() aan wanneer een shelf wordt bereikt. */
    private void moveCustomer(Customer c, List<Waypoint> path) {
        if (path == null || path.size() <= 1) {
            System.out.println("⚠️ path te kort, niet bewegen.");
            return;
        }
        c.setSpeed(120.0); // pixels/seconde

        movementService.moveAlong(
                c,
                path,
                wp -> { // onArrive bij ELK waypoint
                    if (wp.hasShelf()) {
                        c.act(); // pakt items indien op boodschappenlijst → status wijzigt
                    }
                    if ("Checkout".equalsIgnoreCase(wp.getId())) {
                        c.setState(Person.State.CHECKING_OUT);
                        checkoutQueue.addLast(c);
                    }
                    if ("Exit".equalsIgnoreCase(wp.getId())) {
                        c.setState(Person.State.DONE);
                    }
                },
                () -> System.out.println(c.getName() + " route klaar, state=" + c.getState())
        );
    }

    /** HUD linksboven: simpele lijst met klanten en status. */
    private void refreshHUD() {
        hudBox.getChildren().clear();
        hudBox.getChildren().add(new Label("— Klanten —"));
        for (Customer c : activeCustomers) {
            hudBox.getChildren().add(new Label(
                    c.getName()
                            + " | Mandje: " + c.getBasketDescription()
                            + " | Nog: " + c.getShoppingListDescription()
                            + " | State: " + c.getState()
            ));
        }
        if (!checkoutQueue.isEmpty()) {
            hudBox.getChildren().add(new Label("— Checkout rij: " + checkoutQueue.size() + " —"));
        }
    }

    // ---- Visualisatie: teken waypoints + edges ----
    private void drawWaypointsAndEdges() {
        // edges (lichtgrijs)
        for (Waypoint a : waypoints.values()) {
            for (Waypoint b : a.getConnections()) {
                if (a.getId().compareTo(b.getId()) < 0) { // minder dubbele lijnen
                    javafx.scene.shape.Line edge =
                            new javafx.scene.shape.Line(a.getX(), a.getY(), b.getX(), b.getY());
                    edge.setManaged(false);
                    edge.setMouseTransparent(true);
                    edge.setViewOrder(-5); // kleiner = meer vooraan
                    edge.setStroke(javafx.scene.paint.Color.gray(0.25, 0.6));
                    edge.setStrokeWidth(1.0);
                    root.getChildren().add(edge);
                }
            }
        }
        // nodes (rood)
        for (Waypoint w : waypoints.values()) {
            javafx.scene.shape.Circle dot = new javafx.scene.shape.Circle(3, javafx.scene.paint.Color.RED);
            dot.setManaged(false);
            dot.setMouseTransparent(true);
            dot.setLayoutX(w.getX());
            dot.setLayoutY(w.getY());
            dot.setViewOrder(-6); // vóór de edges
            root.getChildren().add(dot);
        }
    }

    // ---- Visualisatie: highlight het actieve pad (oranje) ----
    private void highlightPath(List<Waypoint> path) {
        if (path == null || path.size() < 2) return;
        for (int i = 0; i < path.size() - 1; i++) {
            Waypoint a = path.get(i), b = path.get(i + 1);
            javafx.scene.shape.Line hl =
                    new javafx.scene.shape.Line(a.getX(), a.getY(), b.getX(), b.getY());
            hl.setManaged(false);
            hl.setMouseTransparent(true);
            hl.setViewOrder(-10); // helemaal vooraan
            hl.setStroke(javafx.scene.paint.Color.ORANGE);
            hl.setStrokeWidth(2.0);
            root.getChildren().add(hl);
        }
    }

    // Helpers
    private static String randomName() {
        String[] names = {"Alex","Sam","Bo","Mila","Noa","Finn","Lea"};
        return names[new Random().nextInt(names.length)];
    }

    private static String[] pickTwo(String a, String b, String c) {
        List<String> list = new ArrayList<>(List.of(a, b, c));
        Collections.shuffle(list);
        return new String[]{ list.get(0), list.get(1) };
    }
}
