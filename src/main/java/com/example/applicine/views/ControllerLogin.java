package com.example.applicine.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class ControllerLogin {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    public void initialize() throws IOException {
        System.out.println("Hello World");
    }
    public void checkLogin(){
        System.out.println(username.getText());
        System.out.println(password.getText());
        if(username.getText().equals("admin") && password.getText().equals("admin")){
            try {
                toClientPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void toClientPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerClient.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        Stage stage = new Stage();
        stage.setTitle("Client");
        stage.setScene(scene);
        stage.show();
        Stage thisWindow = (Stage) username.getScene().getWindow();
        thisWindow.close();
    }
    public static URL getFXMLResource() {
            return ControllerLogin.class.getResource("loginView.fxml");
    }
}
