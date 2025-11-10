module org.example.supermarktsimulatie {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.desktop;
    requires org.example.supermarktsimulatie;

    opens org.example.supermarktsimulatie to javafx.fxml;
    exports org.example.supermarktsimulatie;
    exports org.example.supermarktsimulatie.builders;
    opens org.example.supermarktsimulatie.builders to javafx.fxml;
    exports org.example.supermarktsimulatie.models;
    opens org.example.supermarktsimulatie.models to javafx.fxml;
    exports org.example.supermarktsimulatie.controllers;
    opens org.example.supermarktsimulatie.controllers to javafx.fxml;
}