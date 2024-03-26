package com.example.applicine.controllers;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import com.example.applicine.views.ControllerClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientApplication extends Application {
    private Button rightButton;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerClient.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        stage.setTitle("Côté client");
        stage.setScene(scene);
        stage.show();
    }


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