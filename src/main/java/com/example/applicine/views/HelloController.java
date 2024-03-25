package com.example.applicine.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;

public class HelloController {
    @FXML
    private HBox filmContainer;
    @FXML
    private Button rightButton;

    @FXML
    protected void onHelloButtonClick() {
        Label text = new Label("Hello, JavaFX!");
        filmContainer.getChildren().add(text);
    }

    public static URL getFXMLResource() {
        return HelloController.class.getResource("hello-view.fxml");
    }
}
