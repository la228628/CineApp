package be.helha.applicine.controllers;

import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.dao.impl.ClientsDAOImpl;
import be.helha.applicine.models.Client;
import be.helha.applicine.models.HashedPassword;
import be.helha.applicine.models.Session;
import be.helha.applicine.views.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Login window.
 */
public class LoginController extends Application implements LoginViewController.LoginViewListener {
    /**
     * The parent controller of the Login window used to navigate between windows.
     *
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
     *
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
     *
     * @param args the arguments of the application.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Handles the input from the user.
     *
     * @param username
     * @param password
     * @return true if the input is correct, false otherwise.
     */
    @Override
    public boolean inputHandling(String username, String password) {
        //try {
            if (username.equals("admin") && password.equals("admin")) {
                toAdmin();
                return true;
            }

            Client client = clientsDAO.getClientByUsername(username);
            String passwordToDecode = client.getPassword();
            boolean isEquals = HashedPassword.checkPassword(password, passwordToDecode);
            if (client != null && username.equals(client.getUsername()) && isEquals) {
                Session session = parentController.getSession();
                session.setCurrentClient(client);
                session.setLogged(true);
                toClient();
                return true;
            }
        //} catch (Exception e) {
            //System.out.println("An error occurred: " + e.getMessage());
        //}
        return false;
    }

    /**
     * Ask the master controller to navigate to the client window.
     */
    public void toClient() {
        parentController.toClient();
    }

    /**
     * Ask the master controller to navigate to the admin window.
     */
    public void toAdmin(){
        parentController.toAdmin();
    }

    public void toClientWithoutLogin(){
        parentController.toClient();
    }



    @Override
    public void toRegistration(){
        parentController.toRegistration();
    }
}
