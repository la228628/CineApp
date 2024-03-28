package com.example.applicine.controllers;
import com.example.applicine.views.ControllerClient;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientInterfaceApplication extends Application {
    @FXML
    private Button rightButton;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerClient.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        stage.setTitle("Côté client");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}