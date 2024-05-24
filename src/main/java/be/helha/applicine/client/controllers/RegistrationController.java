package be.helha.applicine.client.controllers;

import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.request.ClientEvent;
import be.helha.applicine.common.models.request.ClientRegistrationRequest;
import be.helha.applicine.common.models.Client;
import be.helha.applicine.client.views.RegistrationViewController;
import be.helha.applicine.common.models.request.ErrorMessage;
import be.helha.applicine.common.models.request.RequestVisitor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the controller for the registration window.

 */

public class RegistrationController extends Application implements RegistrationViewController.RegistrationViewListener, ServerRequestHandler.Listener,RequestVisitor {
    private final MasterApplication parentController;
    private final FXMLLoader fxmlLoader = new FXMLLoader(RegistrationViewController.getFXMLResource());

    private RegistrationViewController registrationViewController;

    private ServerRequestHandler serverRequestHandler;

    public RegistrationController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
    }

    /**
     * Starts the registration window.
     * @param stage The stage of the registration window.
     *
     */
    @Override
    public void start(Stage stage) {
        try {
            serverRequestHandler = ServerRequestHandler.getInstance();
            serverRequestHandler.addListener(this);
            RegistrationViewController.setStageOf(fxmlLoader);
            RegistrationViewController controller = fxmlLoader.getController();
            controller.setListener(this);
            registrationViewController = controller;
            parentController.setCurrentWindow(RegistrationViewController.getStage());
        } catch (IOException e) {
            AlertViewController.showErrorMessage("Erreur lors de l'affichage de la fenêtre d'inscription: ");
            parentController.toLogin();
        }
    }

    /**
     * Registers a new client.
     * @param name The name of the client.
     * @param username The username of the client.
     * @param email The email of the client.
     * @param password The password of the client.
     * @return True if the registration was successful, false otherwise.
     */
    @Override
    public boolean register(String name, String username, String email, String password) {
        try {
            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }

            if (!Client.isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format");
            }

            Client client = new Client(name, email, username, password);
            ClientRegistrationRequest request = new ClientRegistrationRequest(client);
            serverRequestHandler.sendRequest(request);
        } catch (Exception e) {
            AlertViewController.showErrorMessage("Error registering: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cancels the registration.
     *
     */
    @Override
    public void cancelRegistration() {
        boolean alertResult = AlertViewController.showConfirmationMessage("Voulez-vous vraiment quittez la création du compte ?");
        if (alertResult) {
            toLogin();
        }
    }

    /**
     * Switches to the login window.
     */
    @Override
    public void toLogin() {
        parentController.toLogin();
    }


    /**
     * Handles the response from the server and dispatches it to the appropriate method.
     * @param response The response from the server.
     */
    @Override
    public void onResponseReceive(ClientEvent response) {
        response.dispatchOn(this);
    }

    /**
     * Handles the registration request.
     * @param request The registration request.
     * if the registration was successful, an info message is shown and the user is redirected to the login window.
     * if the registration failed, an error message is shown.
     */

    @Override
    public void visit(ClientRegistrationRequest request) {
        Platform.runLater(() -> {
            System.out.println("Registration request received");
            if (request.getStatus()) {
                AlertViewController.showInfoMessage("Registration successful");
                toLogin();
            } else {
                AlertViewController.showErrorMessage("Registration failed");
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