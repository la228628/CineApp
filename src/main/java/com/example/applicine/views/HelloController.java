package com.example.applicine.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public static URL getFXMLResource() {
        return HelloController.class.getResource("hello-view.fxml");
    }
}
