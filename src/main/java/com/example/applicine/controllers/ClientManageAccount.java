package com.example.applicine.controllers;

import com.example.applicine.views.LoginControllerView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientManageAccount extends Application {

    private final FXMLLoader fxmlLoader = new FXMLLoader(LoginControllerView.getFXMLResource());
    @Override
    public void start(Stage stage) throws IOException {
        LoginControllerView.setStageOf(fxmlLoader);
        LoginControllerView controller = fxmlLoader.getController();
        controller.setListener(this);
        parentController.setCurrentWindow(LoginControllerView.getStage());
    }


}
