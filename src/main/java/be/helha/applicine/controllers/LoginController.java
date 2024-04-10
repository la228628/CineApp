package be.helha.applicine.controllers;
import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.dao.impl.ClientsDAOImpl;
import be.helha.applicine.models.Client;
import be.helha.applicine.views.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Login window.
 */
public class LoginController extends Application implements LoginViewController.LoginViewListener{
    /**
     * The parent controller of the Login window used to navigate between windows.
     * @see MasterApplication
     */
    private MasterApplication parentController;
    /**
     * The FXML loader of the Login window.
     */
    private ClientsDAO clientsDAO;
    private final FXMLLoader fxmlLoader = new FXMLLoader(LoginViewController.getFXMLResource());

    public LoginController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
    }

    /**
     * Starts the Login window.
     * @param stage the stage of the Login window.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        LoginViewController.setStageOf(fxmlLoader);
        LoginViewController controller = fxmlLoader.getController();
        controller.setListener(this);
        parentController.setCurrentWindow(LoginViewController.getStage());
        clientsDAO = new ClientsDAOImpl();
    }

    /**
     * Launches the Login window.
     * @param args the arguments of the application.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Handles the input from the user.
     * @param username
     * @param password
     * @return true if the input is correct, false otherwise.
     * @throws Exception
     */
    @Override
    public boolean inputHandling(String username, String password) throws Exception {

        Client client = clientsDAO.getClient(1);
        String usernameDB = client.getUsername();
        String passwordDB = client.getPassword();
        if(username.equals(usernameDB) && password.equals(passwordDB)){
            parentController.setLogged(true);
            toClient();
            return true;
        }else if (username.equals("admin") && password.equals("admin")) {
            toAdmin();
            return true;
        }
        return false;
    }
    /**
     * Ask the master controller to navigate to the client window.
     * @throws Exception
     */
    public void toClient() throws Exception {
        parentController.toClient();
    }
    /**
     * Ask the master controller to navigate to the admin window.
     * @throws Exception
     */
    public void toAdmin() throws Exception {
        parentController.toAdmin();
    }
}
