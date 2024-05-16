package be.helha.applicine.client.controllers;

import be.helha.applicine.common.models.Session;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.client.views.ClientViewController;
import be.helha.applicine.client.views.MoviePaneViewController;
import be.helha.applicine.server.dao.MovieDAO;
import be.helha.applicine.server.dao.ViewableDAO;
import be.helha.applicine.server.dao.impl.MovieDAOImpl;
import be.helha.applicine.server.dao.impl.ViewableDAOImpl;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.text.View;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.IOException;
import java.util.List;

/**
 * This class is the main class for the client interface application.
 */
public class ClientController extends Application implements ClientViewController.ClientViewListener, MoviePaneViewController.MoviePaneViewListener {
    private final MasterApplication parentController;
    private ClientViewController clientViewController;

    public ClientController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
    }

    /**
     * starts the client view.
     *
     * @param clientWindow
     * @throws Exception
     */
    public void start(Stage clientWindow) throws Exception {
        try {
            FXMLLoader clientFXML = new FXMLLoader(ClientViewController.getFXMLResource());
            clientViewController = new ClientViewController();
            clientFXML.setController(clientViewController);
            clientViewController.setListener(this);
            ClientViewController.setStageOf(clientFXML);
            setCurrentWindow(clientViewController.getStage());

            Session session = parentController.getSession();
            boolean isLogged = session.isLogged();
            clientViewController.updateButtonText(isLogged);

            List<Viewable> movies = getMovies();
            addMovies(clientViewController, movies);
        } catch (IOException e) {
            parentController.popUpAlert("Erreur lors de l'affichage de la fenêtre client: ");
            parentController.toLogin();
        }
    }

    private List<Viewable> getMovies() throws IOException, ClassNotFoundException {
        ServerRequestHandler serverRequestHandler = parentController.getServerRequestHandler();
        List<Viewable> movies = (List<Viewable>) serverRequestHandler.sendRequest("GET_MOVIES");
        return movies;
    }

    /**
     * Add movies to the client view.
     *
     * @param controller
     * @param movies
     */
    public void addMovies(ClientViewController controller, List<Viewable> movies) {
        for (Viewable movie : movies) {
            controller.addMovie(movie, this);
        }
    }

    /**
     * Switches to the login page.
     *
     * @throws Exception
     */
    @Override
    @FXML
    public void toLoginPage() throws Exception {
        parentController.toLogin();
    }

    /**
     * Setter for the current window.
     *
     * @param currentWindow
     */
    @Override
    public void setCurrentWindow(Window currentWindow) {
        parentController.setCurrentWindow(currentWindow);
    }

    /**
     * Switches to the client account page.
     *
     * @throws Exception
     */
    @Override
    @FXML
    public void toClientAccount() throws Exception {
        System.out.println("Account button clicked, je vais afficher les informations du compte");
        parentController.toClientAccount();
    }

    @Override
    public void onBuyTicketClicked(Viewable movie) throws Exception {
        Session session = parentController.getSession();
        if (session.isLogged()) {
            TicketPageController ticketPageController = new TicketPageController(parentController);
            ticketPageController.setMovie(movie);
            ticketPageController.start(new Stage());
        } else {
            clientViewController.showNotLoggedInAlert();
        }
    }
}