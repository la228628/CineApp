package com.example.applicine.views;

import com.example.applicine.controllers.HelloApplication;
import com.example.applicine.models.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.ArrayList;

public class HelloController {
    @FXML
    private AnchorPane currentSelectionField;

    @FXML
    private ImageView movieImage;

    @FXML
    private ListView<Button> MovieListContainer;

    @FXML
    private ListView<Label> DetailsList;
    @FXML
    public Button nextButton;

    @FXML
    public Button previousButton;

    private ArrayList<Button> moviesLabels = new ArrayList<Button>();

    private int currentSelection = -1;

    private HelloApplication controller;


    public void setAppController(HelloApplication controller) {
        this.controller = controller;
    }


    public static URL getFXMLResource() {
        return HelloController.class.getResource("hello-view.fxml");
    }

    public void addMovieLabel(int movieID) {
        Movie movie = controller.getMovieFrom(movieID);

        Button movieLabel = new Button(movie.getTitle());
        movieLabel.prefWidthProperty().bind(MovieListContainer.widthProperty());
        movieLabel.onMouseClickedProperty().set((event) -> {

            currentSelection = moviesLabels.indexOf(movieLabel);
            setInitialStyle();
            setSelection(currentSelection);
            showMovieDetails(movie);
        });
        moviesLabels.add(movieLabel);
        MovieListContainer.getItems().add(movieLabel);
        setInitialStyle();


    }

    public Movie getMovieFrom(int index) {
        return controller.getMovieFrom(index);
    }


    public void showMovieDetails(Movie movie) {

        clearDetails();
        String imagePath = controller.getMovieImagePath(movie.getID());
        Image image = new Image(imagePath);

        movieImage.setImage(image);

        Label titleLabel = new Label("Title: " + movie.getTitle());
        Label genreLabel = new Label("Genre: " + movie.getGenre());
        Label directorLabel = new Label("Director: " + movie.getDirector());
        Label durationLabel = new Label("Duration: " + movie.getDuration());
        Label synopsisLabel = new Label("Synopsis: " + movie.getSynopsis());

        DetailsList.getItems().add(titleLabel);
        DetailsList.getItems().add(genreLabel);
        DetailsList.getItems().add(directorLabel);
        DetailsList.getItems().add(durationLabel);
        DetailsList.getItems().add(synopsisLabel);

    }

    private void clearDetails() {
        DetailsList.getItems().clear();
        movieImage.setImage(null);
    }

    private void setInitialStyle() {
        for (Button b : moviesLabels) {
            b.setStyle("-fx-background-color: white; " +
                    "-fx-text-fill: black; " +
                    "-fx-font-size: 15px; " +
                    "-fx-font-family: \"Arial Black\"; " +
                    "-fx-border-radius: 5px;");
        }
    }

    private void setSelection(int index) {
        Button button = moviesLabels.get(index);
        button.setStyle("-fx-background-color: black; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 15px; " +
                "-fx-font-family: \"Arial Black\"; " +
                "-fx-border-radius: 5px;");
    }


    public void selectNext(ActionEvent event) {
        if (currentSelection < moviesLabels.size() - 1) {
            currentSelection++;


            showMovieDetails(controller.getMovieFrom(currentSelection));
        }else{
            currentSelection = 0;
        }


        setInitialStyle();
        setSelection(currentSelection);
        showMovieDetails(controller.getMovieFrom(currentSelection));
    }

    public void selectPrevious(ActionEvent event) {

        if (currentSelection > 0) {
            currentSelection--;

        } else {
            currentSelection = moviesLabels.size() - 1;
        }
        setInitialStyle();
        setSelection(currentSelection);
        showMovieDetails(controller.getMovieFrom(currentSelection));


    }




}
