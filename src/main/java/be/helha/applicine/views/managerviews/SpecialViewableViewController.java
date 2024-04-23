package be.helha.applicine.views.managerviews;

import be.helha.applicine.models.Viewable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;

public class SpecialViewableViewController {
    private SpecialViewableListener listener;
    private ArrayList<String> moviesTitleToChoose;

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

    //fonction qui permet de récupérer le film choisi dans le combobox
    @FXML
    void onMovieChoising(ActionEvent event) {
        if(movieChoice.getSelectionModel().getSelectedItem() != null){
            System.out.println("Film s'électionné: "+movieChoice.getSelectionModel().getSelectedItem());
            //TO DO Ajouter le film à la liste des films (ListView) et c'est au controller de faire ça (listener)
        }
    }

    @FXML
    void onRemoveMovieButtonClick(ActionEvent event) {
        listener.onRemoveMovieButtonClick();
    }


    //methode d'initialisation de la vue (remplissage des listes, des combobox, etc)
    public void initialize() {
        fillMovieChoice();
    }


    //remplis le combobox avec les titres des films
    public void fillMovieChoice(){
        moviesTitleToChoose = listener.displayAllMovie();
        for(String title : moviesTitleToChoose){
            movieChoice.getItems().add(title);
        }
    }


    public interface SpecialViewableListener {
        void onAddMovieButtonClick();
        void onRemoveMovieButtonClick();
        ArrayList<String> displayAllMovie();
    }

    public void setListener(SpecialViewableListener listener) {
        this.listener = listener;
    }
}
