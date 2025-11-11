package org.example.supermarktsimulatie.builders;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.supermarktsimulatie.models.Waypoint;

public class AvatarBuilder {
    private double radius = 8;
    private Color fill = Color.DODGERBLUE;
    private Color stroke = Color.BLACK;
    private double strokeWidth = 1.5;
    private double x = 0, y = 0;

    public AvatarBuilder withSize(int diameterPx) {
        this.radius = Math.max(1, diameterPx / 2.0);
        return this;
    }
    public AvatarBuilder withColor(Color color) { if (color != null) this.fill = color; return this; }
    public AvatarBuilder withColor(java.awt.Color awt) {
        if (awt != null) this.fill = Color.rgb(awt.getRed(), awt.getGreen(), awt.getBlue(), awt.getAlpha()/255.0);
        return this;
    }
    public AvatarBuilder atPosition(Waypoint waypoint) { if (waypoint != null) { x = waypoint.getX(); y = waypoint.getY(); } return this; }
    public AvatarBuilder atPosition(double x, double y) { this.x = x; this.y = y; return this; }
    public AvatarBuilder withBorder(boolean enabled) { this.strokeWidth = enabled ? 1.5 : 0; return this; }
    public AvatarBuilder withBorder(Color stroke, double width) { if (stroke != null) this.stroke = stroke; this.strokeWidth = Math.max(0, width); return this; }

    public Circle build() {
        Circle c = new Circle(radius);
        c.setFill(fill);
        c.setStrokeWidth(strokeWidth);
        c.setStroke(strokeWidth > 0 ? stroke : null);
        // layout gebruiken (niet translate) voor consistente beweging
        c.setLayoutX(x);
        c.setLayoutY(y);
        c.setTranslateX(0);
        c.setTranslateY(0);
        return c;
    }
}
