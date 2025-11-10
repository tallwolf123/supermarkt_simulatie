package org.example.supermarktsimulatie.builders;

import javafx.scene.paint.Color;
import org.example.supermarktsimulatie.models.Employee;
import org.example.supermarktsimulatie.models.Waypoint;

public class EmployeeBuilder {
    private final String name;
    private Waypoint startPosition;
    private Color color = Color.BLUE;

    public EmployeeBuilder(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Naam van werknemer mag niet leeg zijn.");
        this.name = name;
    }

    public EmployeeBuilder atStart(Waypoint p) { startPosition = p; return this; }
    public EmployeeBuilder withColor(Color c) { color = c; return this; }

    public BuiltEmployee build() {
        if (startPosition == null)
            throw new IllegalStateException("Startpositie niet ingesteld voor werknemer: " + name);
        return new BuiltEmployee(new Employee(name, startPosition), color);
    }

    public static class BuiltEmployee {
        private final Employee employee;
        private final Color color;
        public BuiltEmployee(Employee e, Color col) { employee = e; color = col; }
        public Employee getEmployee() { return employee; }
        public Color getColor() { return color; }
    }
}
