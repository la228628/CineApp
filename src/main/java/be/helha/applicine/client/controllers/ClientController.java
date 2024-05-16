package be.helha.applicine.client.controllers;

import be.helha.applicine.common.models.Session;
import be.helha.applicine.common.models.Visionable;
import be.helha.applicine.client.views.ClientViewController;
import be.helha.applicine.client.views.MoviePaneViewController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.ByteArrayInputStream;
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
        FXMLLoader clientFXML = new FXMLLoader(ClientViewController.getFXMLResource());
        clientViewController = new ClientViewController();
        clientFXML.setController(clientViewController); // Set the controller manually
        clientViewController.setListener(this);
        ClientViewController.setStageOf(clientFXML);
        setCurrentWindow(clientViewController.getStage());

        Session session = parentController.getSession();
        boolean isLogged = session.isLogged();
        clientViewController.updateButtonText(isLogged);

        List<Visionable> movies = getMovies();
        addMovies(clientViewController, movies);
    }

    private List<Visionable> getMovies() throws IOException, ClassNotFoundException {
        ServerRequestHandler serverRequestHandler = parentController.getServerRequestHandler();
        List<Visionable> movies = (List<Visionable>) serverRequestHandler.sendRequest("GET_MOVIES");
        return movies;
    }

    /**
     * Add movies to the client view.
     *
     * @param controller
     * @param movies
     */
    public void addMovies(ClientViewController controller, List<Visionable> movies) {
        for (Visionable movie : movies) {
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
    public void onBuyTicketClicked(Visionable movie) throws Exception {
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