module com.example.applicine {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.sql.rowset;
    requires okhttp3;
    requires org.json;

    opens com.example.applicine.controllers to javafx.fxml;
    exports com.example.applicine.views;
    opens com.example.applicine.views to javafx.fxml;
    exports com.example.applicine.controllers;
    exports com.example.applicine.models;
    exports com.example.applicine;
    opens com.example.applicine to javafx.fxml;
}