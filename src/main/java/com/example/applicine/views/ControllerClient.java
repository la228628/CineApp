package com.example.applicine.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;

public class ControllerClient {
    @FXML
    private HBox filmContainer;
    @FXML
    private Button rightButton;
    public void initialize() {
        rightButton.setOnAction(event -> {
            Label text = new Label("Hello, JavaFX!");
            filmContainer.getChildren().add(text);
        });
    }

    public static URL getFXMLResource() {
        return ControllerClient.class.getResource("hello-view.fxml");
    }
}