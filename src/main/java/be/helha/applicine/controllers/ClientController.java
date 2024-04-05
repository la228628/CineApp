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
    private final MasterApplication parentController = new MasterApplication();
    private MovieDAO movieDao = new MovieDAOImpl();

    public void start(Stage clientWindow) throws Exception {
        FXMLLoader clientFXML = new FXMLLoader(ClientViewController.getFXMLResource());
        ClientViewController controller = new ClientViewController();
        clientFXML.setController(controller); // Set the controller manually
        controller.setListener(this);
        ClientViewController.setStageOf(clientFXML);

        setCurrentWindow(controller.getStage());

        List<Movie> movies = movieDao.getAllMovies();
        if (movies != null) {
            addMovies(controller, movies);
        }
    }

    public void addMovies(ClientViewController controller, List<Movie> movies) {
        for (Movie movie : movies) {
            controller.addMovie(movie);
        }
    }

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void toLoginPage() throws Exception {
        parentController.toLogin();
    }

    @Override
    public void setCurrentWindow(Window currentWindow) {
        parentController.setCurrentWindow(currentWindow);
    }

    @Override
    public void toClientAccount() throws Exception {
        parentController.toClientAccount();
    }
}