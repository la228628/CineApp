package be.helha.applicine.controllers;

import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.dao.impl.ClientsDAOImpl;
import be.helha.applicine.models.Client;
import be.helha.applicine.models.HashedPassword;
import be.helha.applicine.views.RegistrationViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController extends Application implements RegistrationViewController.RegistrationViewListener {
    private MasterApplication parentController;
    private ClientsDAO clientsDAO;
    private final FXMLLoader fxmlLoader = new FXMLLoader(RegistrationViewController.getFXMLResource());

    private RegistrationViewController registrationViewController;

    public RegistrationController(MasterApplication masterApplication) {
        clientsDAO = new ClientsDAOImpl();
        this.parentController = masterApplication;
    }

    @Override
    public void start(Stage stage) {
        try {
            RegistrationViewController.setStageOf(fxmlLoader);
            RegistrationViewController controller = fxmlLoader.getController();
            controller.setListener(this);
            registrationViewController = controller;
            parentController.setCurrentWindow(RegistrationViewController.getStage());
        }catch (IOException e){
            parentController.popUpAlert("Erreur lors de l'affichage de la fenêtre d'inscription: ");
            parentController.toLogin();
        }
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

            String hashedPassword = HashedPassword.getHashedPassword(password);
            clientsDAO.createClient(name, email, username, hashedPassword);
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