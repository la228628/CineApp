package be.helha.applicine.client.controllers;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.Session;
import be.helha.applicine.client.views.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Login window.
 */
public class LoginController extends Application implements LoginViewController.LoginViewListener {
    /**
     * The parent controller of the Login window used to navigate between windows.
     *
     * @see MasterApplication
     */
    private MasterApplication parentController;
    /**
     * The FXML loader of the Login window.
     */
    private final FXMLLoader fxmlLoader = new FXMLLoader(LoginViewController.getFXMLResource());

    private LoginViewController loginViewController;

    public LoginController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
    }

    /**
     * Starts the Login window.
     *
     * @param stage the stage of the Login window.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        LoginViewController.setStageOf(fxmlLoader);
        loginViewController = fxmlLoader.getController();
        loginViewController.setListener(this);
        parentController.setCurrentWindow(LoginViewController.getStage());
    }

    /**
     * Launches the Login window.
     *
     * @param args the arguments of the application.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Handles the input from the user.
     *
     * @param username
     * @param password
     * @return true if the input is correct, false otherwise.
     * @throws Exception
     */
    @Override
    public boolean inputHandling(String username, String password) {
        try {
            ServerRequestHandler serverRequestHandler = parentController.getServerRequestHandler();
            Client client = (Client) serverRequestHandler.sendRequest("CHECK_LOGIN " + username + " " + password);
            if (client != null) {
                Session session = parentController.getSession();
                session.setCurrentClient(client);
                session.setLogged(true);
                toClient();
                return true;
            }
        } catch (Exception e) {
            loginViewController.showError("Unable to connect to the server.");
        }
        return false;
    }

    /**
     * Ask the master controller to navigate to the client window.
     *
     * @throws Exception
     */
    public void toClient() throws Exception {
        parentController.toClient();
    }

    /**
     * Ask the master controller to navigate to the admin window.
     *
     * @throws Exception
     */
    public void toAdmin() throws Exception {
        parentController.toAdmin();
    }

    public void toClientWithoutLogin() throws Exception {
        parentController.toClient();
    }


    @Override
    public void toRegistration() throws IOException {
        parentController.toRegistration();
    }
}
