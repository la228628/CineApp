package com.example.applicine.views;

import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import com.example.applicine.models.exceptions.InvalideFieldsExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerViewController {

    private String currentEditType = "";


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

    @FXML
    private Button validateButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nameTextField;

    @FXML

    private TextField genreTextField;

    @FXML

    private TextField directorTextField;

    @FXML

    private TextField durationTextField;

    @FXML

    private TextField synopsisTextField;

    @FXML

    private Button imageChoiceButton;

    @FXML
    private Label selectedPathLabel;

    @FXML
    private AnchorPane editPane;

    public ArrayList<Button> moviesLabels = new ArrayList<Button>();

    private int currentSelection = -1;
    private static Stage adminWindow;
    private ManagerViewListener listener;
    public static Window getStage() {
        return adminWindow;
    }
    public void setListener(ManagerViewListener listener) {
        this.listener = listener;
    }
    public static URL getFXMLResource() {
        return ManagerViewController.class.getResource("managerView.fxml");
    }
    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        adminWindow = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        scene.getStylesheets().add(ManagerViewController.class.getResource("/com/example/applicine/views/managerStyle.css").toExternalForm());
        adminWindow.setScene(scene);
        adminWindow.setTitle("Movie List Manager");
        adminWindow.setScene(scene);
        adminWindow.show();
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
        showEditDeleteButtons();
        clearDetails();
        String imagePath = movie.getImagePath();
        movieImage.setImage(new Image(imagePath));
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
            b.getStyleClass().set(0, "buttonS");
            b.getStyleClass().remove("Selected");
        }
    }
    private void setSelection(int index) {
        Button button = moviesLabels.get(index);
        button.getStyleClass().add("Selected");
    }


    private void showEditDeleteButtons() {
        if(currentSelection != -1) {
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        }
    }


    public void selectNext(ActionEvent event) {
        System.out.println("Current selection: " + currentSelection);
        if (currentSelection < moviesLabels.size() - 1) {
            currentSelection++;
            showMovieDetails(listener.getMovieFrom(currentSelection));
        } else {
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


    private void fillEditPane(Movie movie) {
        nameTextField.setText(movie.getTitle());
        genreTextField.setText(movie.getGenre());
        directorTextField.setText(movie.getDirector());
        durationTextField.setText(String.valueOf(movie.getDuration()));
        synopsisTextField.setText(movie.getSynopsis());
        selectedPathLabel.setText(movie.getImagePath());
    }

    private void clearEditPane() {
        nameTextField.setText("");
        genreTextField.setText("");
        directorTextField.setText("");
        durationTextField.setText("");
        synopsisTextField.setText("");
        selectedPathLabel.setText("...");
    }

    private void showEditPane() {
        this.editPane.setVisible(true);
    }

    private void hideEditPane() {
        this.editPane.setVisible(false);
    }



    public void onEditButtonClick(ActionEvent actionEvent) {
        currentEditType = "modify";
        showEditPane();
        System.out.println("Edit button clicked");
        System.out.println("modif du film : id = "+currentSelection);
        Movie movieToModify = listener.getMovieFrom(currentSelection);
        fillEditPane(movieToModify);

    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        //currentEditType permet de savoir si on est en mode ajout ou en mode édition d'un film existant (voir les méthodes showEditPane et hideEditPane)
        currentEditType = "add";
        clearEditPane();
        showEditPane();
    }

    public void onDeleteButtonClick(ActionEvent actionEvent) {
        currentEditType = "delete";
        System.out.println("Delete button clicked");
    }



    public void onValidateButtonClick(ActionEvent event) throws SQLException, InvalideFieldsExceptions {
        if(currentEditType.equals("add")) {
            listener.onValidateButtonClick(nameTextField.getText(), genreTextField.getText(), directorTextField.getText(), durationTextField.getText(), synopsisTextField.getText(), selectedPathLabel.getText(), this.currentEditType);
        } else if(currentEditType.equals("modify")) {
            listener.onValidateButtonClick(currentSelection+1, nameTextField.getText(), genreTextField.getText(), directorTextField.getText(), durationTextField.getText(), synopsisTextField.getText(), selectedPathLabel.getText(), this.currentEditType);
        } else if(currentEditType.equals("delete")) {
            System.out.println("Delete button clicked");
        }
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        currentEditType = "";
        clearEditPane();
        hideEditPane();
    }

    public void onImageChoiceButtonClick(ActionEvent actionEvent) {
        listener.onImageChoiceButtonClick();
    }

    public void clearMovies() {
        MovieListContainer.getItems().clear();
        moviesLabels.clear();
    }

    public void setImagePathLabel(String imagePath) {
        selectedPathLabel.setText(imagePath);
    }

    public interface ManagerViewListener {
        Movie getMovieFrom(int index);
        void toLogin() throws IOException;

        void onValidateButtonClick(String title, String genre, String director, String duration, String synopsis, String imagePath, String editType) throws SQLException;

        void onValidateButtonClick(int movieID, String title, String genre, String director, String duration, String synopsis, String imagePath, String editType);

        void onImageChoiceButtonClick();


    }
    @FXML
    private void toLoginPage(ActionEvent event) throws IOException {
        listener.toLogin();
    }
}