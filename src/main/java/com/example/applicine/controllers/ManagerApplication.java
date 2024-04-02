package com.example.applicine.controllers;
import com.example.applicine.dao.impl.MovieDAOImpl;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import com.example.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import com.example.applicine.dao.MovieDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ManagerApplication class is the controller class for the Manager view.
 */
public class ManagerApplication extends Application implements ManagerViewController.ManagerViewListener{
    private final FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
    /**
     * parentController is useful to say Master which window is currently open.
     */
    private final MasterApplication parentController = new MasterApplication();
    private MovieDAO movieDAO;
    private List<Movie> movieList;

    /**
     * It fetches all the movies from the database to movieList.
     * It follows the DAO design pattern https://www.digitalocean.com/community/tutorials/dao-design-pattern.
     */
    public ManagerApplication() {
        movieDAO = new MovieDAOImpl();
        movieList = movieDAO.getAllMovies();
    }

    @Override
    public void start(Stage adminPage) throws Exception {
        ManagerViewController.setStageOf(fxmlLoader);
        ManagerViewController managerViewController = fxmlLoader.getController();
        managerViewController.setListener(this);
        for (Movie movie : movieList) {
            managerViewController.addMovieLabel(movie);
        }
        parentController.setCurrentWindow(ManagerViewController.getStage());
        adminPage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * It returns a movie to the movieList at index.
     * @param index
     * @return movieList
     */
    public Movie getMovieFrom(int index) {
        return movieList.get(index);
    }

    /**
     * Redirects to the login view and disconnect the user.
     * @throws IOException
     */
    public void toLogin() throws IOException{
        parentController.toLogin();
    }
}
