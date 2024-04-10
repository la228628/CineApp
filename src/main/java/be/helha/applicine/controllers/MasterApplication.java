package be.helha.applicine.controllers;

import be.helha.applicine.FileMangement.FileManager;
import be.helha.applicine.controllers.managercontrollers.ManagerController;
import be.helha.applicine.dao.MovieDAO;
import be.helha.applicine.dao.impl.MovieDAOImpl;
import be.helha.applicine.database.ApiRequest;
import be.helha.applicine.views.WaitingWindowViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * It is the main class of the application.
 * It is responsible for starting the application and switching between windows.
 */
public class MasterApplication extends Application {
    /**
     * The current opened window of the application.
     */
    private Window currentWindow;
    private boolean isLogged;

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
    /**client
     * Setter for the current window.
     * @param currentWindow The window to set as the current window.
     */
    public void setCurrentWindow(Window currentWindow) {
        this.currentWindow = currentWindow;
        System.out.println("Current window set to: " + currentWindow);
    }
    /**
     * Start point of the application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        WaitingWindowViewController waitingWindowViewController = new WaitingWindowViewController();
        Frame waitingWindow = waitingWindowViewController.getWaitingWindow();
        waitingWindow.setVisible(true);

        initializeAppdata();

        setCurrentWindow(stage);
        toClient();

        waitingWindow.dispose();
    }

    private void initializeAppdata() {
        FileManager.createDataFolder();

        MovieDAO movieDAO = new MovieDAOImpl();

        if (movieDAO.isMovieTableEmpty()){
            ApiRequest apiRequest = new ApiRequest();
            apiRequest.fillDatabase();
        }
    }

    /**
     * Switch to the login window and close the currentWindow.
     */
    public void toLogin() throws IOException {
        currentWindow.hide();
        LoginController loginController = new LoginController(this);
        loginController.start(new Stage());
    }
    /**
     * Switch to the client window and close the currentWindow.
     *
     * @throws Exception
     */
    public void toClient() throws Exception {
        currentWindow.hide();
        ClientController clientController = new ClientController(this);
        clientController.start(new Stage());
    }
    /**
     * Switch to the manager window and close the currentWindow.
     * @throws Exception
     */
    public void toAdmin() throws Exception {
        currentWindow.hide();
        ManagerController managerController = new ManagerController(this);
        managerController.start(new Stage());
    }

    /**
     * Switch to the client account window and close the currentWindow.
     * @throws Exception
     */
    public void toClientAccount() throws Exception {
        currentWindow.hide();
        ClientAccountApplication clientAccountApplication = new ClientAccountApplication(this);
        clientAccountApplication.start(new Stage());
    }
    public void toRegistration() throws IOException {
        currentWindow.hide();
        RegistrationController registrationController = new RegistrationController(this);
        registrationController.start(new Stage());
    }

    public static void main(String[] args) {
        launch();
    }



}


