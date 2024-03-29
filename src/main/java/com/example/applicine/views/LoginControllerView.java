package com.example.applicine.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class LoginControllerView {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label emptyErrorLabel;
    private LoginViewListener listener;
    private static Stage stage;
    public static Window getStage() {
        return stage;
    }
    public void setListener(LoginViewListener listener){
        this.listener = listener;
    }
    public void initialize(){
        System.out.println("Hello World");
    }
    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
    public void checkLogin() throws Exception {
        boolean loginSuccessful = listener.inputHandling(username.getText(), password.getText());
        if(loginSuccessful){
            System.out.println("Login successful");
        }else {
            emptyErrorLabel.setText("Incorrect username or password");
        }
    }
    public interface LoginViewListener{
        boolean inputHandling(String username, String password) throws Exception;
    }
    public static URL getFXMLResource() {
        return LoginControllerView.class.getResource("loginView.fxml");
    }
}
