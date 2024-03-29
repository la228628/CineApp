package com.example.applicine.controllers;

import com.example.applicine.views.LoginControllerView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application implements LoginControllerView.LoginViewListener{
    private final MasterApplication parentController = new MasterApplication();
    private final FXMLLoader fxmlLoader = new FXMLLoader(LoginControllerView.getFXMLResource());
    @Override
    public void start(Stage stage) throws IOException {
        LoginControllerView.setStageOf(fxmlLoader);
        LoginControllerView controller = fxmlLoader.getController();
        controller.setListener(this);
        parentController.setCurrentWindow(LoginControllerView.getStage());
    }
    public static void main(String[] args) {
        launch();
    }

    @Override
    public boolean inputHandling(String username, String password) throws Exception {
        if(username.equals("admin") && password.equals("admin")){
            toAdmin();
        }else if(username.equals("client") && password.equals("client")) {
            toClient();
        }else {
            return false;
        }
        return true;
    }
    public void toClient() throws Exception {
        parentController.toClient();
    }
    public void toAdmin() throws Exception {
        parentController.toAdmin();
    }
}
