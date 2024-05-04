package be.helha.applicine.client.controllers;

import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.server.dao.impl.ClientsDAOImpl;
import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.HashedPassword;
import be.helha.applicine.client.views.RegistrationViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController extends Application implements RegistrationViewController.RegistrationViewListener {
    private MasterApplication parentController;
    private final FXMLLoader fxmlLoader = new FXMLLoader(RegistrationViewController.getFXMLResource());

    private RegistrationViewController registrationViewController;

    public RegistrationController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
    }

    @Override
    public void start(Stage stage) throws IOException {
        RegistrationViewController.setStageOf(fxmlLoader);
        RegistrationViewController controller = fxmlLoader.getController();
        controller.setListener(this);
        registrationViewController = controller;
        parentController.setCurrentWindow(RegistrationViewController.getStage());
    }

    @Override
    public boolean register(String name, String username, String email, String password) {
        boolean isValid = true;

        try {
            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled");
            }

            if (!Client.isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format");
            }

            Client client = new Client(name, email, username, password);
            String response = (String) parentController.getServerRequestHandler().sendRequest(client);
            if (!"Registration successful".equals(response)) {
                throw new Exception("Registration failed");
            }
        } catch (Exception e) {
            isValid = false;
            registrationViewController.showAlert("Error", e.getMessage());
        }
        return isValid;
    }

    @Override
    public void cancelRegistration() throws IOException {
        boolean alertResult = parentController.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Perte des modifications", "Voulez-vous vraiment quittez la création du compte ?");
        if (alertResult) {
            toLogin();
        }
    }

    @Override
    public void toLogin() throws IOException {
        parentController.toLogin();
    }


}