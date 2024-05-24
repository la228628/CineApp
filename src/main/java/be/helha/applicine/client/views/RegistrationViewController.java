package be.helha.applicine.client.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

/**
 * This class is the controller for the registration view.
 */
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


    /**
     * This method returns the URL of the FXML resource.
     * @return the URL of the FXML resource.
     */
    public static URL getFXMLResource() {
        return FXML_RESOURCE;
    }

    /**
     * This method sets the stage of the FXML loader.
     * @param fxmlLoader the FXML loader to set the stage of.
     * @throws IOException if the FXML loader can't be loaded.
     */
    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    /**
     * This method returns the stage of the FXML loader.
     * @return the stage of the FXML loader.
     */
    public static Window getStage() {
        return stage;
    }

    /**
     * sets the listener for the registration view.
     * @param listener the listener to set.
     */

    public void setListener(RegistrationViewListener listener) {
        this.listener = listener;
    }

    /**
     * This method registers a user.
     * It calls the register method of the listener.
     */
    public void register() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!password.equals(confirmPassword)) {
            AlertViewController.showInfoMessage("Passwords do not match");
            return;
        }
        if (listener.register(name, username, email, password)) {
            listener.toLogin();
        }
    }

    /**
     * This method cancels the registration.
     * It calls the cancelRegistration method of the listener.
     */
    public void cancelRegistration() {
        listener.cancelRegistration();
    }

    /**
     * Interface for the registration view listener.
     */
    public interface RegistrationViewListener {
        boolean register(String name,String username, String email, String password);
        void toLogin();
        void cancelRegistration();
    }
}
