package be.helha.applicine.client.controllers;

import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.client.controllers.managercontrollers.ManagerController;
import be.helha.applicine.common.models.Session;
import be.helha.applicine.common.models.request.ClientEvent;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * Constructor of the MasterApplication.
     * It initializes the session.
     */
    public MasterApplication() {
        session = new Session();
    }

    /**
     * Setter for the current window.
     * @param currentWindow The window to set as the current window.
     */
    public void setCurrentWindow(Window currentWindow) {
        this.currentWindow = currentWindow;
    }

    /**
     * Starts the application.
     * @param stage The stage of the application.
     */
    @Override
    public void start(Stage stage) {
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
     */
    public void toAdmin() {
        try {
            closeAllWindows();
            ManagerController managerController = new ManagerController(this);
            managerController.start(new Stage());
        } catch (IOException e) {
            AlertViewController.showErrorMessage("Erreur lors de l'affichage de la fenêtre manager, veuillez réessayer plus tard.");
            closeAllWindows();
            toLogin();
        }catch (SQLException | ClassNotFoundException e){
            AlertViewController.showErrorMessage("Impossible de se connecter à la base de données");
        }
    }

    /**
     * Switch to the client account window and close the currentWindow.
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
}


