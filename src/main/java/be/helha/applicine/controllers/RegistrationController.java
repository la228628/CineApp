package be.helha.applicine.controllers;

import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.dao.impl.ClientsDAOImpl;
import be.helha.applicine.models.Client;
import be.helha.applicine.views.RegistrationViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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

    public void setCurrentWindow(Window currentWindow) {
        parentController.setCurrentWindow(currentWindow);
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
                clientsDAO.createClient(name, email, username, password);
            } catch (Exception e) {
                isValid = false;
            }
        }

        return isValid;
    }

    @Override
    public void toLogin() throws IOException {
        parentController.toLogin();
    }
}