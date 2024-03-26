package com.example.applicine.controllers;


import com.example.applicine.models.Movie;
import com.example.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ManagerApplication extends Application implements ManagerViewController.ManagerViewListener{
    public ArrayList<Movie> movieList;
    @Override
    public void start(Stage stage) throws Exception {
        Stage adminPage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
        ManagerViewController managerViewController = fxmlLoader.getController();
        managerViewController.setListener(this);
        adminPage.setTitle("Movie List Manager");
        this.movieList = new ArrayList<>();
    }

    @Override
    public Movie getMovieFrom(int index) {
        return null;
    }
}
