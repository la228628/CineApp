package com.example.applicine.controllers;

import com.example.applicine.views.ControllerLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerLogin.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
