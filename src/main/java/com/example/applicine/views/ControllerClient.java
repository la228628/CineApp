package com.example.applicine.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;

public class ControllerClient {
    @FXML
    private HBox filmContainer;
    @FXML
    private Button rightButton;
    public void initialize() {
        for(int i = 0; i < 3; i ++){
            Pane pane = new Pane();
            pane.setPrefSize(200, 200);
            pane.setStyle("-fx-background-color: #f00; ;-fx-border-color: black ;-fx-border-width: 1; margin: 10px; -fx-padding: 5px;-fx-border-insets: 5px;-fx-background-insets: 5px;");
            Label label = new Label("Hello");
            label.setTranslateX(50);
            label.setTranslateY(50);
            pane.getChildren().add(label);
            filmContainer.getChildren().add(pane);
        }
    }
    public static URL getFXMLResource() {
            return ControllerClient.class.getResource("hello-view.fxml");
    }
}