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
    public ArrayList<Movie> movieList = DatabaseConnection.getAllMovies();
    @Override
    public void start(Stage adminPage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        ManagerViewController managerViewController = fxmlLoader.getController();
        managerViewController.setListener(this);
        adminPage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
        for (Movie movie : movieList) {
            managerViewController.addMovieLabel(movie);
        }
        adminPage.setScene(scene);
        adminPage.setTitle("Movie List Manager");
        adminPage.setScene(scene);
        adminPage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public Movie getMovieFrom(int index) {
        return movieList.get(index);
    }
    public void logoutButtonClick() throws IOException{
        LoginApplication loginApplication = new LoginApplication();
        loginApplication.start(new Stage());
        //Stage thisWindow = (Stage)previousButton.getScene().getWindow();
        //thisWindow.close();
    }
}
