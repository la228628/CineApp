package com.example.applicine.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;

public class LoginControllerView {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label emptyErrorLabel;
    LoginViewListener listener;
    public void setListener(LoginViewListener listener){
        this.listener = listener;
    }
    public void initialize(){
        System.out.println("Hello World");
    }
    public void checkLogin() throws Exception {
        if(username.getText().isEmpty() || password.getText().isEmpty()){
            emptyErrorLabel.setText("Veuillez remplir tous les champs");
            return;
        }
        if(username.getText().equals("admin") && password.getText().equals("admin")){
            listener.toAdmin(username);
        }else if(username.getText().equals("client") && password.getText().equals("client")) {
            listener.toClient(username);
        }else {
            emptyErrorLabel.setText("Nom d'utilisateur ou mot de passe incorrect");
        }
    }
    public interface LoginViewListener{
        void toClient(TextField windowItem) throws Exception;
        void toAdmin(TextField windowItem) throws Exception;
    }
    public static URL getFXMLResource() {
            return LoginControllerView.class.getResource("loginView.fxml");
    }
}
