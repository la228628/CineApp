package com.example.applicine.controllers;


import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import com.example.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerApplication extends Application implements ManagerViewController.ManagerViewListener{
    public ArrayList<Movie> movieList;
    @Override
    public void start(Stage stage) throws Exception {
        Stage adminPage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        ManagerViewController managerViewController = fxmlLoader.getController();
        managerViewController.setListener(this);
        adminPage.setTitle("Movie List Manager");


        //DatabaseConnection.fillDB();

        this.movieList = DatabaseConnection.getAllMovies();


        for (Movie m : movieList) {
            managerViewController.addMovieLabel(m);
        }


        //DatabaseConnection.createTableMovie();


        adminPage.setScene(scene);
        adminPage.show();





    }

    public String createMovieImagePath(int id) {
        return "file:src/main/resources/com/example/applicine/views/images/" + id + ".jpg";
    }

    @Override
    public Movie getMovieFrom(int index) {
        return movieList.get(index);
    }

    public static void main(String[] args) {
        launch();
    }
}
