package com.example.applicine.controllers;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.views.ManagerViewController;
import com.example.applicine.models.Movie;
import com.example.applicine.views.ControllerClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

/*
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
        */
public class HelloApplication extends Application {
    private Button rightButton;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerClient.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        stage.setTitle("Côté client");
        stage.setScene(scene);
        stage.show();
    }

    public void create20Movie (ArrayList<Movie> movieList){

        String[] movieName = {"Sausage Party", "The Godfather", "The Dark Knight", "The Lord of the Rings: The Return of the King", "L'orange mécanique", "L'ArrayList de Schindler", "Inception", "Fight Club", "The Lord of the Rings: The Fellowship of the Ring", "Forrest Gump", "The Shawshank Redemption", "The Lord of the Rings: The Two Towers", "The Matrix", "The Godfather: Part II", "The Dark Knight Rises", "The Lord of the Rings: The Fellowship of the Ring", "The Lord of the Rings: The Two Towers", "The Lord of the Rings: The Return of the King", "The Lord of the Rings: The Fellowship of the Ring", "The Lord of the Rings: The Two Towers"};

        for (int i = 0; i < 10; i++) {
            Movie m = new Movie(movieName[i], "Genre" + i, "Director" + i, 120, "Synopsis" + i, createMovieImagePath(i));
            movieList.add(m);
        }
    }

//    //permet d'ajouter un film à la base de données
//    public void createMovie(Movie movie) {
//
//        int id = DatabaseConnection.AddMovie(movie);
//    }


    public static void main(String[] args) {
        launch();
    }

    public Movie getMovieFrom(int index) {
        return DatabaseConnection.getMovie(index);
    }

    public String createMovieImagePath(int id) {
        return "file:src/main/resources/com/example/applicine/views/images/" + id + ".jpg";
    }
}