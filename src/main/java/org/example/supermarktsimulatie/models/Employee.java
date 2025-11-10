package org.example.supermarktsimulatie.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Employee extends Person {

    public Employee(String name, Waypoint startPosition) {
        super(name, startPosition);
    }

    @Override
    public void act() {
        return;
    }

    public void restock(Map<String, Waypoint> storeLayout) {

        if (!hasProduct("Stock")) {
            System.out.println(getName() + " heeft geen 'Stock' product bij zich en kan dus niet restocken.");
            return;
        }

        // verzamel alle shelves in de winkel
        List<Shelf> allShelves = new ArrayList<>();
        for (Waypoint wp : storeLayout.values()) {
            if (wp.hasShelf() && wp.getShelf() != null) {
                allShelves.add(wp.getShelf());
            }
        }

        if (allShelves.isEmpty()) {
            System.out.println("Geen shelves gevonden om te restocken.");
            return;
        }

        // kies willekeurige shelf
        Random rnd = new Random();
        Shelf chosenShelf = allShelves.get(rnd.nextInt(allShelves.size()));

        // voer restock uit
        chosenShelf.resetStock();
        System.out.println(getName() + " heeft de voorraad van shelf '" + chosenShelf.getName() + "' opnieuw aangevuld.");
    }
}
