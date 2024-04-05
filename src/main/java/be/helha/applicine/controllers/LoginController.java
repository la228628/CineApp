package be.helha.applicine.controllers;
import be.helha.applicine.views.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Login window.
 */
public class LoginController extends Application implements LoginViewController.LoginViewListener{
    /**
     * The parent controller of the Login window used to navigate between windows.
     */
    private final MasterApplication parentController = new MasterApplication();
    /**
     * The FXML loader of the Login window.
     */
    private final FXMLLoader fxmlLoader = new FXMLLoader(LoginViewController.getFXMLResource());

    /**
     * Starts the Login window.
     * @param stage the stage of the Login window.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        LoginViewController.setStageOf(fxmlLoader);
        LoginViewController controller = fxmlLoader.getController();
        controller.setListener(this);
        parentController.setCurrentWindow(LoginViewController.getStage());
    }

    /**
     * Launches the Login window.
     * @param args the arguments of the application.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Handles the input from the user.
     * @param username
     * @param password
     * @return true if the input is correct, false otherwise.
     * @throws Exception
     */
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
    /**
     * Ask the master controller to navigate to the client window.
     * @throws Exception
     */
    public void toClient() throws Exception {
        parentController.toClient();
    }
    /**
     * Ask the master controller to navigate to the admin window.
     * @throws Exception
     */
    public void toAdmin() throws Exception {
        parentController.toAdmin();
    }
}
