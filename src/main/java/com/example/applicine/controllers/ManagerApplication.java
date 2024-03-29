package com.example.applicine.controllers;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import com.example.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ManagerApplication extends Application implements ManagerViewController.ManagerViewListener{
    private final FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
    private final MasterApplication parentController = new MasterApplication();
    private final ArrayList<Movie> movieList = DatabaseConnection.getAllMovies();
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

    public Movie getMovieFrom(int index) {
        return movieList.get(index);
    }
    public void toLogin() throws IOException{
        parentController.toLogin();
    }
}
