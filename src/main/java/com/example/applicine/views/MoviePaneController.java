package com.example.applicine.views;

import com.example.applicine.models.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MoviePaneController {
    @FXML
    private VBox root;
    @FXML
    private ImageView imageView;
    @FXML
    public Label titleLabel;

    public void setMovie(Movie movie) {
        titleLabel.setText(movie.getTitle());
        imageView.setImage(new Image(movie.getImagePath()));
    }

    public Pane getRoot() {
        return root;
    }
}