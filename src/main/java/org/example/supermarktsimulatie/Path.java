package org.example.supermarktsimulatie;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Path {
    private final Waypoint start;
    private final Waypoint end;
    private final Line line;

    public Path(Waypoint start, Waypoint end) {
        this.start = start;
        this.end = end;

        line = new Line(start.getX(), start.getY(), end.getX(), end.getY());
        line.setStroke(Color.RED);
        line.setStrokeWidth(2);
    }

    public Waypoint getStart() { return start; }
    public Waypoint getEnd() { return end; }
    public Line getLine() { return line; }
}
