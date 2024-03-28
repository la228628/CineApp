package com.example.applicine.controllers;

import com.example.applicine.views.LoginControllerView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application implements LoginControllerView.LoginViewListener {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginControllerView.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        LoginControllerView controller = fxmlLoader.getController();
        controller.setListener(this);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void toClient(TextField windowItem) throws Exception {
        ClientInterfaceApplication clientInterfaceApplication = new ClientInterfaceApplication();
        clientInterfaceApplication.start(new Stage());
        Stage thisWindow = (Stage) windowItem.getScene().getWindow();
        thisWindow.close();
    }
    @Override
    public void toAdmin(TextField windowItem) throws Exception {
        ManagerApplication managerApplication = new ManagerApplication();
        managerApplication.start(new Stage());
        Stage thisWindow = (Stage) windowItem.getScene().getWindow();
        thisWindow.close();
    }
}
