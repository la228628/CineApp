package be.helha.applicine.client.controllers;

import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.request.ClientRegistrationRequest;
import be.helha.applicine.common.models.Client;
import be.helha.applicine.client.views.RegistrationViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController extends Application implements RegistrationViewController.RegistrationViewListener {
    private MasterApplication parentController;
    private final FXMLLoader fxmlLoader = new FXMLLoader(RegistrationViewController.getFXMLResource());

    private RegistrationViewController registrationViewController;

    private ServerRequestHandler serverRequestHandler;

    public RegistrationController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
    }

    @Override
    public void start(Stage stage) {
        try {
            serverRequestHandler = ServerRequestHandler.getInstance();
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
            String response = serverRequestHandler.sendRequest(request);

            if ("Registration successful".equals(response)) {
                return true;
            }
        } catch (Exception e) {
            AlertViewController.showErrorMessage("Error registering: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void cancelRegistration(){
        boolean alertResult = AlertViewController.showConfirmationMessage("Voulez-vous vraiment quittez la création du compte ?");
        if (alertResult) {
            toLogin();
        }
    }

    @Override
    public void toLogin() {
        parentController.toLogin();
    }


}