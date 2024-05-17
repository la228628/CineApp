package be.helha.applicine.client.controllers;

import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.server.FileManager;
import be.helha.applicine.client.controllers.managercontrollers.ManagerController;
import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.server.dao.MovieDAO;
import be.helha.applicine.server.dao.RoomDAO;
import be.helha.applicine.server.dao.impl.ClientsDAOImpl;
import be.helha.applicine.server.dao.impl.MovieDAOImpl;
import be.helha.applicine.server.dao.impl.RoomDAOImpl;
import be.helha.applicine.server.database.ApiRequest;
import be.helha.applicine.common.models.Session;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * It is the main class of the application.
 * It is responsible for starting the application and switching between windows.
 */
public class MasterApplication extends Application {
    /**
     * The current opened window of the application.
     */
    private Window currentWindow;

    /**
     * The session of the user.
     */
    private final Session session;

    /**
     * The server request handler.
     */
    private ServerRequestHandler serverRequestHandler;

    /**
     * Constructor of the MasterApplication.
     * It initializes the session.
     */
    public MasterApplication() {
        session = new Session();
    }

    /**
     * client
     * Setter for the current window.
     *
     * @param currentWindow The window to set as the current window.
     */
    public void setCurrentWindow(Window currentWindow) {
        this.currentWindow = currentWindow;
    }

    /**
     * Starts the application.
     *
     * @param stage The stage of the application.
     */
    @Override
    public void start(Stage stage) {
        try {
            serverRequestHandler = new ServerRequestHandler();
        } catch (IOException e) {
            AlertViewController.showErrorMessage("Erreur lors de la connexion au serveur, veuillez réessayer plus tard.");
            return;
        }
        setCurrentWindow(stage);
        toClient();
    }

    /**
     * Switch to the login window and close the currentWindow.
     */
    public void toLogin() {
        try {
            closeAllWindows();
            LoginController loginController = new LoginController(this);
            loginController.start(new Stage());
        } catch (IOException e) {
            AlertViewController.showErrorMessage("Erreur lors de l'ouverture de la fenêtre de connexion, veuillez réessayer plus tard.");
            closeAllWindows();
        }
    }

    /**
     * Switch to the client window and close the currentWindow.
     */
    public void toClient() {
        if (currentWindow != null)
            closeAllWindows();
        try {
            ClientController clientController = new ClientController(this);
            clientController.start(new Stage());
        } catch (Exception e) {
            AlertViewController.showErrorMessage("Erreur lors de l'ouverture de la fenêtre client, veuillez réessayer plus tard.");
            toLogin();
        }
    }

    //Dites moi s'il faut un exception ici
    public void closeAllWindows() {
        List<Window> stages = new ArrayList<>(Window.getWindows());
        for (Window window : stages) {
            if (window instanceof Stage && window.isShowing()) {
                ((Stage) window).close();
            }
        }
    }

    /**
     * Switch to the manager window and close the currentWindow.
     *
     * @throws Exception
     */
    public void toAdmin() {
        try {
            closeAllWindows();
            ManagerController managerController = null;
            try {
                managerController = new ManagerController(this);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            managerController.start(new Stage());
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            AlertViewController.showErrorMessage("Erreur lors de l'ouverture de la fenêtre manager, veuillez réessayer plus tard.");
            closeAllWindows();
            toLogin();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switch to the client account window and close the currentWindow.
     *
     * @throws Exception
     */
    public void toClientAccount() {
        closeAllWindows();
        ClientAccountApplication clientAccountApplication = new ClientAccountApplication(this);
        clientAccountApplication.start(new Stage());
    }

    public void toRegistration() {
        closeAllWindows();
        RegistrationController registrationController = new RegistrationController(this);
        registrationController.start(new Stage());
    }

    public static void main(String[] args) {
        launch();
    }

    public Session getSession() {
        return session;
    }

    public void closeCurrentWindow() {
        currentWindow.hide();
    }

    public void toTicketPage() throws Exception {
        TicketPageController ticketPageController = new TicketPageController(this);
        ticketPageController.start(new Stage());
    }

    public ServerRequestHandler getServerRequestHandler() {
        return serverRequestHandler;
    }
}


