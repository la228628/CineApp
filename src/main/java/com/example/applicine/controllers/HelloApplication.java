package com.example.applicine.controllers;

import com.example.applicine.views.ManagerViewController;
import com.example.applicine.models.Movie;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;
import java.util.ArrayList;



public class HelloApplication extends Application implements ManagerViewController.ManagerViewListener {
    public ArrayList<Movie> movieList;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        ManagerViewController managerViewController = fxmlLoader.getController();
        managerViewController.setListener(this);
        stage.setTitle("Movie List Manager");
        this.movieList = new ArrayList<Movie>();
        create20Movie(movieList);
        for (Movie m : movieList) {
            managerViewController.addMovieLabel(m.getID());
        }
        stage.setScene(scene);

        stage.show();
    }

    public void create20Movie (ArrayList<Movie> movieList){

        String[] movieName = {"Sausage Party", "The Godfather", "The Dark Knight", "The Lord of the Rings: The Return of the King", "L'orange mécanique", "Le monde de Narnia", "Inception", "Fight Club", "The Lord of the Rings: The Fellowship of the Ring", "Forrest Gump", "Ping Pong"};

        for (int i = 0; i < movieName.length; i++) {
            Movie m = new Movie(i, movieName[i], "Genre" + i, "Director" + i, 120, "Synopsis" + i, createMovieImagePath(i));
            movieList.add(m);
        }
    }


    public static void main(String[] args) {
        launch();
    }

    public Movie getMovieFrom(int index) {
        return movieList.get(index);
    }

    public String createMovieImagePath(int id) {
        return "file:src/main/resources/com/example/applicine/views/images/" + id + ".jpg";
    }
}