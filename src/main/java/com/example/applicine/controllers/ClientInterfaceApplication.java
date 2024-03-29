package com.example.applicine.controllers;
import com.example.applicine.views.ControllerClient;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ClientInterfaceApplication extends Application implements ControllerClient.ClientViewListener {
    @FXML
    private Button rightButton;
    @Override
    public void start(Stage clientWindow) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerClient.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        clientWindow.setScene(scene);
        ControllerClient controller = fxmlLoader.getController();
        controller.loadPage(clientWindow, fxmlLoader, scene);
        controller.setListener(this);
    }
    public static void main(String[] args) {
        launch();
    }

    @Override
    public int incrementOffset(int offset) {
        return offset + 3;
    }

    @Override
    public int decrementOffset(int offset) {
        return offset - 3;
    }
}