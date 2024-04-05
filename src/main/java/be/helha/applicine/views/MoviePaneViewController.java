package be.helha.applicine.views;

import be.helha.applicine.models.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;

/**
 * Controller for the movie pane.
 */
public class MoviePaneViewController {
    /**
     * The root of the movie pane.
     */
    @FXML
    private VBox root;
    /**
     * The image view of the movie pane.
     */
    @FXML
    private ImageView imageView;
    /**
     * The title label of the movie pane.
     */
    @FXML
    public Label titleLabel;

    public static URL getFXMLResource() {
        return MoviePaneViewController.class.getResource("/be/helha/applicine/views/components/MoviePane.fxml");
    }

    /**
     * Sets the movie of the movie pane.
     * @param movie the movie to set.
     */
    public void setMovie(Movie movie) {
        titleLabel.setText(movie.getTitle());
        imageView.setImage(new Image(movie.getImagePath()));
    }

    /**
     * Gets the root of the movie pane.
     * @return the root of the movie pane.
     */
    public Pane getRoot() {
        return root;
    }
}