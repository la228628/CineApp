package com.example.applicine.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ControllerLogin {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label emptyErrorLabel;
    public void initialize(){
        System.out.println("Hello World");
    }
    public void checkLogin() {
        if(username.getText().isEmpty() || password.getText().isEmpty()){
            emptyErrorLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        System.out.println(username.getText());
        System.out.println(password.getText());
        if(username.getText().equals("admin") && password.getText().equals("admin")){
            try {
                toClientPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            emptyErrorLabel.setText("Nom d'utilisateur ou mot de passe incorrect");
        }
    }
    public void toAdminPage() throws IOException {}
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