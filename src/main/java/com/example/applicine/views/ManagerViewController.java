package com.example.applicine.views;

import com.example.applicine.models.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ManagerViewController {
    @FXML
    private AnchorPane currentSelectionField;

    @FXML
    private ImageView movieImage;

    @FXML
    private ListView<Button> MovieListContainer;

    @FXML
    private AnchorPane DetailsList;

    @FXML
    private Label titleLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Label directorLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label synopsisLabel;
    @FXML
    private Button logoutButton;
    @FXML
    public Button nextButton;

    @FXML
    public Button previousButton;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    public ArrayList<Button> moviesLabels = new ArrayList<Button>();

    private int currentSelection = -1;

    private ManagerViewListener listener;
    public void setListener(ManagerViewListener listener) {
        System.out.println("yes : " + listener.getMovieFrom(0));
        this.listener = listener;
    }
    public static URL getFXMLResource() {
        return ManagerViewController.class.getResource("managerView.fxml");
    }
    /**
     * Add a movie label to the list
     *
     * @param movie
     * @return
     */
    public void addMovieLabel(Movie movie) {
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

    public Movie getMovie(int index) {
        return listener.getMovieFrom(index);
    }

    /**
     * Show the details of a movie in the details pane
     *
     * @param movie
     */
    public void showMovieDetails(Movie movie) {
        clearDetails();
        String imagePath = movie.getImagePath();
        Image image = new Image(imagePath);
        movieImage.setImage(image);
        titleLabel.setText("Titre: " + movie.getTitle());
        genreLabel.setText("Genre: " + movie.getGenre());
        directorLabel.setText("Directeur: " + movie.getDirector());
        durationLabel.setText("Durée: " + movie.getDuration());
        synopsisLabel.setText("Synopsis: " + movie.getSynopsis());
    }
    /**
     * Clear the details pane
     */
    private void clearDetails() {
        titleLabel.setText("");
        genreLabel.setText("");
        directorLabel.setText("");
        durationLabel.setText("");
        synopsisLabel.setText("");
        movieImage.setImage(null);
    }
    /**
     *
     * The first label is selected
     * The style is set to white background and black text
     * The font size is 15px
     * The font family is Arial Black
     * The border radius is 5px
     *
     */
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
        System.out.println("Current selection: " + currentSelection);
        if (currentSelection < moviesLabels.size() - 1) {
            currentSelection++;
            showMovieDetails(listener.getMovieFrom(currentSelection));
        }else{
            currentSelection = 0;
        }
        setInitialStyle();
        setSelection(currentSelection);
        showMovieDetails(getMovie(currentSelection));
    }

    public void selectPrevious(ActionEvent event) {

        if (currentSelection > 0) {
            currentSelection--;

        } else {
            currentSelection = moviesLabels.size() - 1;
        }
        setInitialStyle();
        setSelection(currentSelection);
        showMovieDetails(listener.getMovieFrom(currentSelection));
    }
    public interface ManagerViewListener {
        Movie getMovieFrom(int index);

        void logoutButtonClick() throws IOException;
    }

    @FXML
    private void logoutButtonClick( ActionEvent event) throws IOException {
        listener.logoutButtonClick();
        Stage thisWindow = (Stage) logoutButton.getScene().getWindow();
        thisWindow.close();

    }
}