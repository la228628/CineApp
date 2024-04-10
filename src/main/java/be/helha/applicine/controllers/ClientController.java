package be.helha.applicine.controllers;

import be.helha.applicine.dao.MovieDAO;
import be.helha.applicine.dao.impl.MovieDAOImpl;
import be.helha.applicine.models.Movie;
import be.helha.applicine.views.ClientViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

/**
 * This class is the main class for the client interface application.
 */
public class ClientController extends Application implements ClientViewController.ClientViewListener {
    private final MasterApplication parentController;
    private MovieDAO movieDao = new MovieDAOImpl();

    public ClientController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
    }

    /**
     * starts the client view.
     * @param clientWindow
     * @throws Exception
     */
    public void start(Stage clientWindow) throws Exception {
        FXMLLoader clientFXML = new FXMLLoader(ClientViewController.getFXMLResource());
        ClientViewController controller = new ClientViewController();
        clientFXML.setController(controller); // Set the controller manually
        controller.setListener(this);
        ClientViewController.setStageOf(clientFXML);
        setCurrentWindow(controller.getStage());

        boolean isLogged = parentController.isLogged();
        controller.updateButtonText(isLogged);

        List<Movie> movies = movieDao.getAllMovies();
        if (movies != null) {
            addMovies(controller, movies);
        }
    }

    /**
     * Add movies to the client view.
     * @param controller
     * @param movies
     */
    public void addMovies(ClientViewController controller, List<Movie> movies) {
        for (Movie movie : movies) {
            controller.addMovie(movie);
        }
    }


    public static void main(String[] args) {
        launch();
    }


    /**
     * Switches to the login page.
     * @throws Exception
     */
    @Override
    public void toLoginPage() throws Exception {
        parentController.toLogin();
    }

    /**
     * Setter for the current window.
     * @param currentWindow
     */
    @Override
    public void setCurrentWindow(Window currentWindow) {
        parentController.setCurrentWindow(currentWindow);
    }

    /**
     * Switches to the client account page.
     * @throws Exception
     */
    @Override
    public void toClientAccount() throws Exception {
        parentController.toClientAccount();
    }
}