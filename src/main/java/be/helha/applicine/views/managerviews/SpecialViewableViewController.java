package be.helha.applicine.views.managerviews;

import be.helha.applicine.models.Viewable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    @FXML
    private Button validateButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label totalDurationLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private TextField sagaNameField;

    @FXML
    private ScrollPane sagaList;


    VBox VboxToDisplay = new VBox();

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
            System.out.println("Index du film choisi: "+movieChoice.getSelectionModel().getSelectedIndex());
            listener.onMovieChoising(movieChoice.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    void onRemoveMovieButtonClick(ActionEvent event) {
        listener.onRemoveMovieButtonClick();
    }

    @FXML
    void onValidateButtonClick(ActionEvent event) {
        listener.onValidateButtonClick(sagaNameField.getText());
    }


    @FXML
    void onCancelButtonClick(ActionEvent event) {
        listener.onCancelButtonClick();

    }



    //methode d'initialisation de la vue (remplissage des listes, des combobox, etc)
    public void init() {
        VboxToDisplay.prefWidthProperty().bind(sagaList.widthProperty());
        fillMovieChoice();
        displaySagas();
    }


    public void fillMovieChoice(){
        moviesTitleToChoose = listener.displayAllMovie();
        for(String title : moviesTitleToChoose) {
            movieChoice.getItems().add(title);
        }
    }

    public void displaySagas(){
        VboxToDisplay.getChildren().clear();
        listener.displaySagas();
    }

    public void fillAddedMovieChoice(List<String> addedViewablesTitles, Integer totalDuration) {
        movieList.getItems().clear();
        for(String title : addedViewablesTitles){
            Label label = new Label(title);
            movieList.getItems().add(label);
        }
        setTotalDuration(totalDuration);
    }

    private void setTotalDuration(Integer totalDuration) {
        totalDurationLabel.setText("Durée totale: "+convertToDurationString(totalDuration)+" minutes");
    }

    private String convertToDurationString(Integer totalDuration){
        int hours = totalDuration / 60;
        int minutes = totalDuration % 60;
        return hours + "h" + minutes;
    }

    public void onCancelConfirm() {
        sagaNameField.clear();
        movieList.getItems().clear();
        totalDurationLabel.setText("");
    }

    public void displaySaga(Viewable viewable) {
        Button button = new Button(viewable.getTitle());
        button.getStyleClass().set(0,"buttonS");
        button.prefWidthProperty().bind(VboxToDisplay.widthProperty());
        button.setOnAction(e -> {
            onSagaDisplayButtonClick(viewable);
        });
        VboxToDisplay.getChildren().add(button);
        sagaList.setContent(VboxToDisplay);

    }

    private void onSagaDisplayButtonClick(Viewable viewable) {
    }


    public interface SpecialViewableListener {
        void onAddMovieButtonClick();
        void onRemoveMovieButtonClick();
        ArrayList<String> displayAllMovie();

        void onMovieChoising(int selectedIndex);


        void onValidateButtonClick(String name);

        void onCancelButtonClick();

        void displaySagas();
    }

    public void refresh(){
        fillMovieChoice();
        displaySagas();
    }

    public void setListener(SpecialViewableListener listener) {
        this.listener = listener;
    }
}
