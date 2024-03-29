package com.example.applicine.controllers;
import com.example.applicine.views.ControllerClient;
import com.example.applicine.views.LoginControllerView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ClientInterfaceApplication extends Application implements ControllerClient.ClientViewListener {
    private final MasterApplication parentController = new MasterApplication();
    @FXML
    private Button rightButton;
    @Override
    public void start(Stage clientWindow) throws Exception {
        FXMLLoader clientFXML = new FXMLLoader(ControllerClient.getFXMLResource());
        ControllerClient.setStageOf(clientFXML);
        ControllerClient controller = clientFXML.getController();
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