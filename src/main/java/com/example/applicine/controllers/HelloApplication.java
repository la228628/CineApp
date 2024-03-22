package com.example.applicine.controllers;

import com.example.applicine.views.HelloController;
import com.example.applicine.models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;
import java.util.ArrayList;



public class HelloApplication extends Application {
    ArrayList<Movie> movieList;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        HelloController controller = fxmlLoader.getController();
        controller.setAppController(this);
        stage.setTitle("Movie List Manager");
        this.movieList = new ArrayList<Movie>();
        create20Movie(movieList);
        for (Movie m : movieList) {
            controller.addMovieLabel(m.getID());
        }
        stage.setScene(scene);

        stage.show();
    }

    public void create20Movie (ArrayList<Movie> movieList){

        String[] movieName = {"Sausage Party", "The Godfather", "The Dark Knight", "The Lord of the Rings: The Return of the King", "L'orange mécanique", "L'ArrayList de Schindler", "Inception", "Fight Club", "The Lord of the Rings: The Fellowship of the Ring", "Forrest Gump", "The Shawshank Redemption", "The Lord of the Rings: The Two Towers", "The Matrix", "The Godfather: Part II", "The Dark Knight Rises", "The Lord of the Rings: The Fellowship of the Ring", "The Lord of the Rings: The Two Towers", "The Lord of the Rings: The Return of the King", "The Lord of the Rings: The Fellowship of the Ring", "The Lord of the Rings: The Two Towers"};

        for (int i = 0; i < 10; i++) {
            Movie m = new Movie(i, movieName[i], "Genre" + i, "Director" + i, 120, "Synopsis" + i, getMovieImagePath(i));
            movieList.add(m);
        }
    }


    public static void main(String[] args) {
        launch();
    }

    public Movie getMovieFrom(int index) {
        return movieList.get(index);
    }

    public String getMovieImagePath(int id) {
        return "file:src/main/resources/com/example/applicine/views/images/" + id + ".jpg";
    }

}