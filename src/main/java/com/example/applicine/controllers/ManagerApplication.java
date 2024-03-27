package com.example.applicine.controllers;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import com.example.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ManagerApplication extends Application implements ManagerViewController.ManagerViewListener{
    public ArrayList<Movie> movieList = DatabaseConnection.getAllMovies();
    @Override
    public void start(Stage adminPage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        ManagerViewController managerViewController = fxmlLoader.getController();
        managerViewController.setListener(this);
        adminPage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
        for (Movie movie : movieList) {
            System.out.println(ManagerViewController.moviesButtons.size());
            managerViewController.addMovieLabel(movie);
            System.out.println(ManagerViewController.moviesButtons.size());
        }
        adminPage.setTitle("Movie List Manager");
        adminPage.setScene(scene);
        adminPage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public Movie getMovieFrom(int index) {
        return DatabaseConnection.getMovie(index);
    }
}
