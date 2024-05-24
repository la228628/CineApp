package be.helha.applicine.client.controllers;

import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.Session;
import be.helha.applicine.client.views.LoginViewController;
import be.helha.applicine.common.models.request.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller for the Login window.
 */
public class LoginController extends Application implements LoginViewController.LoginViewListener, ServerRequestHandler.Listener, RequestVisitor {
    /**
     * The parent controller of the Login window used to navigate between windows.
     *
     * @see MasterApplication
     */
    private final MasterApplication parentController;
    private final FXMLLoader fxmlLoader = new FXMLLoader(LoginViewController.getFXMLResource());

    private LoginViewController loginViewController;

    private final ServerRequestHandler serverRequestHandler;

    public LoginController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
        serverRequestHandler = ServerRequestHandler.getInstance();
        serverRequestHandler.addListener(this);
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
     */
    @Override
    public boolean inputHandling(String username, String password) {
        if (Objects.equals(username, "admin") && Objects.equals(password, "admin")) {
            toAdmin();
            return true;
        }
        CheckLoginRequest checkLoginRequest = new CheckLoginRequest(username, password);
        try {
            serverRequestHandler.sendRequest(checkLoginRequest);
        } catch (IOException e) {
            e.printStackTrace();
            AlertViewController.showErrorMessage("Perte de connexion avec le serveur. Nous testons votre connection.");
            try {
                serverRequestHandler.sendRequest(new PingServer());
            } catch (IOException ex) {
                AlertViewController.showInfoMessage("Impossible de se connecter au serveur. Serveur en maintenance. Veuillez réessayer plus tard.");
                parentController.closeAllWindows();
            }
        }
        return false;
    }

    /**
     * Ask the master controller to navigate to the client window.
     */
    public void toClient() {
        parentController.toClient();
    }

    /**
     * Ask the master controller to navigate to the admin window.
     */
    public void toAdmin() {
        parentController.toAdmin();
    }

    /**
     * Ask the master controller to navigate to the not logged client window.
     */

    public void toClientWithoutLogin() {
        parentController.toClient();
    }

    /**
     * Ask the master controller to navigate to the registration window.
     */
    @Override
    public void toRegistration() {
        parentController.toRegistration();
    }

    /**
     * When a response is received, it is dispatched to the appropriate method.
     * @param response
     */
    @Override
    public void onResponseReceive(ClientEvent response) {
        response.dispatchOn(this);
    }

    /**
     * Called when the connection is lost.
     * Redirects the user to the login page.
     */
    @Override
    public void onConnectionLost() {
        AlertViewController.showErrorMessage("Connection perdue avec le serveur. Redémarrage de l'application.");
        Platform.exit();
    }

    /**
     * Sends a request to the server to check the login.
     * It set the current client in the session.
     * If the login is correct, the user is redirected to the client window.
     * @param checkLoginRequest
     *

     */
    @Override
    public void visit(CheckLoginRequest checkLoginRequest) {
        Platform.runLater(() -> {
            Client client = checkLoginRequest.getClient();
            System.out.println("Client: " + client);
            if (client != null) {
                Session session = parentController.getSession();
                session.setCurrentClient(client);
                session.setLogged(true);
                toClient();
            }
        });
    }

    @Override
    public void visit(ErrorMessage errorMessage) {
        Platform.runLater(() -> {
            AlertViewController.showErrorMessage(errorMessage.getMessage());
        });
    }
}
