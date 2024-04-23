package be.helha.applicine.views.managerviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;

public class SpecialViewableViewController {
    private SpecialViewableListener listener;

    @FXML
    private Button addMovieButton;

    @FXML
    private ListView<Label> movieList;

    @FXML
    private ComboBox<String> movieChoice;

    @FXML
    private Button removeMovieButton;

    public static URL getFXMLResource() {
        return SpecialViewableViewController.class.getResource("SpecialViewableView.fxml");
    }

    @FXML
    void onAddMovieButtonClick(ActionEvent event) {
        listener.onAddMovieButtonClick();
    }

    @FXML
    void onMovieChoising(ActionEvent event) {
        listener.displayAllMovie();
    }

    @FXML
    void onRemoveMovieButtonClick(ActionEvent event) {
        listener.onRemoveMovieButtonClick();
    }





    public interface SpecialViewableListener {
        void onAddMovieButtonClick();
        void onRemoveMovieButtonClick();
        void displayAllMovie();
    }

    public void setListener(SpecialViewableListener listener) {
        this.listener = listener;
    }
}
