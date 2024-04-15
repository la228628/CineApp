package be.helha.applicine.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class RegistrationViewController {
    private RegistrationViewListener listener;
    private static Stage stage;
    @FXML
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    private static final URL FXML_RESOURCE = RegistrationViewController.class.getResource("registrationView.fxml");

    public static URL getFXMLResource() {
        return FXML_RESOURCE;
    }

    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public static Window getStage() {
        return stage;
    }

    public void setListener(RegistrationViewListener listener) {
        this.listener = listener;
    }

    public void initialize() {
        System.out.println("Hello World");
    }

    public void register() throws IOException {
        String name = nameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match");
            return;
        }
        if (listener.register(name, username, email, password)) {
            System.out.println("Registration successful");
            listener.toLogin();
        }
    }

    public void cancelRegistration() throws IOException {
        listener.cancelRegistration();
    }

    public interface RegistrationViewListener {
        boolean register(String name,String username, String email, String password);
        void toLogin() throws IOException;
        void cancelRegistration() throws IOException;
    }
}
