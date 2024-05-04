package be.helha.applicine.client.views;

import be.helha.applicine.common.models.Visionable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;

/**
 * Controller for the movie pane.
 */
public class MoviePaneViewController {
    @FXML
    public VBox imageVbox;
    @FXML
    public Button moreInfoButton;
    @FXML
    public Button lessInfoButton;
    @FXML
    public Label infoMovie;
    @FXML
    public ScrollPane movieInfoScrollPane;
    public AnchorPane anchorPane;
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

    private MoviePaneViewListener listener;

    public void setListener(MoviePaneViewListener listener) {
        this.listener = listener;
    }

    private Visionable movie;

    public static URL getFXMLResource() {
        return MoviePaneViewController.class.getResource("/be/helha/applicine/client/views/components/MoviePane.fxml");
    }

    /**
     * Sets the movie of the movie pane.
     *
     * @param movie the movie to set.
     */
    public void setMovie(Visionable movie) {
        this.movie = movie;
        titleLabel.setText(movie.getTitle());
        imageView.setImage(new Image(movie.getImagePath()));
        infoMovie.setText(movie.getSynopsis());
    }

    /**
     * Gets the root of the movie pane.
     *
     * @return the root of the movie pane.
     */
    public Pane getRoot() {
        return root;
    }

    public void toBuyTicketPage(ActionEvent actionEvent) {
        try {
            listener.onBuyTicketClicked(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moreInfoHandling(MouseEvent mouseEvent) {
        imageView.setFitHeight(imageView.getFitHeight() / 3);
        imageVbox.setAlignment(Pos.TOP_CENTER);
        moreInfoButton.setVisible(false);
        lessInfoButton.setVisible(true);
        movieInfoScrollPane.setPrefHeight(200);
        int size = countLines(infoMovie) * 20;
        System.out.println(size);
        anchorPane.setPrefHeight((double) size / 2);
        infoMovie.setPrefHeight((double) size / 2);
    }

    public void lessInfoHandling(MouseEvent mouseEvent) {
        imageView.setFitHeight(imageView.getFitHeight() * 3);
        imageVbox.setAlignment(Pos.TOP_CENTER);
        moreInfoButton.setVisible(true);
        lessInfoButton.setVisible(false);
        movieInfoScrollPane.setPrefHeight(40);
        anchorPane.setPrefHeight(movieInfoScrollPane.getPrefHeight() - 2);
        infoMovie.setPrefHeight(0);
    }

    public int countLines(Label label) {
        anchorPane.setPrefHeight(1000);
        label.setPrefHeight(1000);
        String text = label.getText();
        double labelWidth = label.getWidth();
        double characterWidth = new Text("W").getLayoutBounds().getWidth();
        System.out.println(characterWidth);
        double charactersPerLine = Math.floor(labelWidth / characterWidth);
        return (int) Math.ceil(text.length() / charactersPerLine);
    }

    public interface MoviePaneViewListener {
        void onBuyTicketClicked(Visionable movie) throws Exception;
    }


}