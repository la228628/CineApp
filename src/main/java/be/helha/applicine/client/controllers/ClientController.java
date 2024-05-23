package be.helha.applicine.client.controllers;

import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Session;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.client.views.ClientViewController;
import be.helha.applicine.client.views.MoviePaneViewController;
import be.helha.applicine.common.models.event.Event;
import be.helha.applicine.common.models.event.EventListener;
import be.helha.applicine.common.models.request.GetMoviesRequest;
import be.helha.applicine.common.models.request.UpdateMovieRequest;
import be.helha.applicine.common.models.request.UpdateViewableRequest;
import be.helha.applicine.server.dao.MovieDAO;
import be.helha.applicine.server.dao.ViewableDAO;
import be.helha.applicine.server.dao.impl.MovieDAOImpl;
import be.helha.applicine.server.dao.impl.ViewableDAOImpl;
import be.helha.applicine.common.models.request.GetViewablesRequest;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.Window;


import javax.swing.text.View;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the main class for the client interface application.
 */
public class ClientController extends Application implements ClientViewController.ClientViewListener, MoviePaneViewController.MoviePaneViewListener {
    private final MasterApplication parentController;
    private ClientViewController clientViewController;

    private ServerRequestHandler serverRequestHandler;

    public ClientController(MasterApplication masterApplication) throws IOException {
        this.parentController = masterApplication;
        serverRequestHandler = ServerRequestHandler.getInstance();
//        try {
//            HandleEventFromServer(serverRequestHandler);
//        } catch (Exception e) {
//            AlertViewController.showErrorMessage("Erreur lors de la récupération du serverRequestHandler" + e.getMessage());
//        }
    }

    /**
     * starts the client view.
     *
     * @param clientWindow
     * @throws Exception
     */
    public void start(Stage clientWindow) throws Exception {
        try {
            //serverRequestHandler = ServerRequestHandler.getInstance();
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
            //HandleEventFromServer(serverRequestHandler);
        } catch (IOException e) {
            AlertViewController.showErrorMessage("Erreur lors de l'affichage de la fenêtre client: " + e.getMessage());
            parentController.toLogin();
        }
    }

    private void HandleEventFromServer(ServerRequestHandler serverRequestHandler) {
//        //classe anonyme qui implémente l'interface EventListener, je définis ce que je veux faire quand je reçois un event de type MOVIE_UPDATED
//        serverRequestHandler.addEventListener(new EventListener() {
//            @Override
//            public void onEventReceived(Event event) {
//                System.out.println("Je vais refresh la liste des films quand je reçois un event de type MOVIE_UPDATED");
//                if (event.getType().equals("EVENT: UPDATE_MOVIE")) {
//                    try {
//                        List<Viewable> movies = getMovies();
//                        clientViewController.clearMovies();
//                        addMovies(clientViewController, movies);
//                    } catch (IOException | ClassNotFoundException e) {
//                        AlertViewController.showErrorMessage("Erreur lors de la récupération des films: " + e.getMessage());
//                    }
//                }
//            }
//        });
    }

    private List<Viewable> getMovies() throws IOException, ClassNotFoundException {
        GetViewablesRequest request = new GetViewablesRequest();
        serverRequestHandler.sendRequest(request);
        return serverRequestHandler.readResponse();
    }

    /**
     * Add movies to the client view.
     *
     * @param controller
     * @param movies
     */
    public void addMovies(ClientViewController controller, List<Viewable> movies) {
        String moviesBugged = "";
        for (Viewable movie : movies) {
            try {
                controller.addMovie(movie, this);
            }catch (IOException e){
                moviesBugged = "Problème de chargement du/des film(s) suivant(s):\n";
                moviesBugged += movie.getTitle() + "\n";
            }
        }
        if (!moviesBugged.isEmpty())
            AlertViewController.showErrorMessage(moviesBugged);
    }

    /**
     * Switches to the login page.
     *
     * @throws Exception when the login page cannot be displayed.
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
            ticketPageController.setViewable(movie);
            ticketPageController.start(new Stage());
        } else {
            clientViewController.showNotLoggedInAlert();
        }
    }
}