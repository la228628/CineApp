package be.helha.applicine.client.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;

/**
 * This class is the controller for the login view.
 */
public class LoginViewController {
    /**
     * The username text field.
     */
    @FXML
    private TextField username;
    /**
     * The password field.
     */
    @FXML
    private PasswordField password;
    /**
     * The error label.
     */
    @FXML
    private Label emptyErrorLabel;
    /**
     * The listener for the login view.
     */
    private LoginViewListener listener;
    /**
     * The stage of the login view.
     */
    private static Stage stage;
    /**
     * Gets the stage of the login view.
     * @return the stage of the login view.
     */
    public static Window getStage() {
        return stage;
    }
    /**
     * Sets the listener for the login view.
     * @param listener the listener for the login view.
     */
    public void setListener(LoginViewListener listener){
        this.listener = listener;
    }
    /**
     * Initializes the login view.
     */
    public void initialize(){
        System.out.println("Hello World");
    }
    /**
     * Sets the stage of the login view.
     * @param fxmlLoader the fxml loader.
     * @throws IOException if an I/O error occurs.
     */
    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Checks the login.
     * @throws Exception if an error occurs.
     */
    public void checkLogin() throws Exception {
        boolean loginSuccessful = listener.inputHandling(username.getText(), password.getText());
        if(loginSuccessful){
            System.out.println("Login successful");
        }else {
            emptyErrorLabel.setText("Incorrect username or password");
        }
    }
    @FXML
    public void toRegistration() {
        listener.toRegistration();
    }

    @FXML
    public void goBackWithoutLogin() throws Exception {
        listener.toClientWithoutLogin();
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * The listener for the login view.
     */
    public interface LoginViewListener{
        boolean inputHandling(String username, String password);
        void toRegistration();
        void toClientWithoutLogin();
    }
    /**
     * Gets the FXML resource.
     * @return the FXML resource.
     */
    public static URL getFXMLResource() {
        return LoginViewController.class.getResource("loginView.fxml");
    }
}
