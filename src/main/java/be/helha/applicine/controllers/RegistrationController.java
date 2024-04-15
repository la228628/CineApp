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
import javafx.stage.Window;

import java.io.IOException;

public class RegistrationController extends Application implements RegistrationViewController.RegistrationViewListener {
    private MasterApplication parentController;
    private ClientsDAO clientsDAO;
    private final FXMLLoader fxmlLoader = new FXMLLoader(RegistrationViewController.getFXMLResource());

    public RegistrationController(MasterApplication masterApplication) {
        clientsDAO = new ClientsDAOImpl();
        this.parentController = masterApplication;
    }

    @Override
    public void start(Stage stage) throws IOException {
        RegistrationViewController.setStageOf(fxmlLoader);
        RegistrationViewController controller = fxmlLoader.getController();
        controller.setListener(this);
        parentController.setCurrentWindow(RegistrationViewController.getStage());
    }

    @Override
    public boolean register(String name, String username, String email, String password) {
        boolean isValid = true;

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            isValid = false;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            isValid = false;
        }

        if (isValid) {
            try {
                String hashedPassword = HashedPassword.getHashedPassword(password);
                clientsDAO.createClient(name, email, username, hashedPassword);
            } catch (Exception e) {
                isValid = false;
            }
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