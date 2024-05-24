package be.helha.applicine.client.views.managerviews;

import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.exceptions.InvalideFieldsExceptions;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class is the controller of the movie manager view

 */

public class MovieManagerViewController {

    private String currentEditType = "";


    @FXML
    private AnchorPane currentSelectionField;

    @FXML
    private ImageView movieImage;

    @FXML
    private ScrollPane MovieListContainer;

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

    public ArrayList<Button> moviesDisplayButtons = new ArrayList<Button>();

    public int currentSelection = -1;
    private static Stage adminWindow;
    private ManagerViewListener listener;

    /**
     * Get the stage
     * @return
     */
    public static Window getStage() {
        return adminWindow;
    }

    /**
     * Set the listener
     * @param listener
     */
    public void setListener(ManagerViewListener listener) {
        this.listener = listener;
    }

    /**
     * Get the FXML resource
     * @return
     */

    public static URL getFXMLResource() {
        return MovieManagerViewController.class.getResource("movieManagerView.fxml");
    }

    /**
     * Set the stage of the manager view
     * @param fxmlLoader
     * @throws IOException
     */



    /**
     * Add a movie label to the list
     *
     * @param movie
     * @return
     */
    public void displayMovie(Movie movie) {
        Button movieLabel = new Button(movie.getTitle());
        movieLabel.prefWidthProperty().bind(MovieListContainer.widthProperty());
        movieLabel.setLayoutY(moviesDisplayButtons.size()* 50);
        System.out.println(movieLabel.getLayoutY());
        movieLabel.onMouseClickedProperty().set((event) -> {
            currentSelection = moviesDisplayButtons.indexOf(movieLabel);
            setInitialStyle();
            setSelection();
            showMovieDetails(movie);
        });
        moviesDisplayButtons.add(movieLabel);
        VBox toSetInPane = new VBox();
        toSetInPane.getChildren().addAll(moviesDisplayButtons);
        MovieListContainer.setContent(toSetInPane);

        setInitialStyle();
    }

    /**
     * Get a movie from the list
     *
     * @param index
     * @return
     */

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
        durationLabel.setText("Durée: " + movie.getTotalDuration());
        synopsisLabel.setText("Synopsis: " + movie.getSynopsis());
        System.out.println("id du movie = " + movie.getId());
        ;

        System.out.println(currentEditType);
        if (currentEditType.equals("modify")) {
            fillEditPane(movie);
        }
    }

    /**
     * Clear the details pane
     */
    public void clearDetails() {
        titleLabel.setText("");
        genreLabel.setText("");
        directorLabel.setText("");
        durationLabel.setText("");
        synopsisLabel.setText("");
        movieImage.setImage(null);
    }

    /**
     * The first label is selected
     * The style is set to white background and black text
     * The font size is 15px
     * The font family is Arial Black
     * The border radius is 5px
     */
    private void setInitialStyle() {
        for (Button b : moviesDisplayButtons) {
            b.getStyleClass().set(0, "buttonS");
            b.getStyleClass().remove("Selected");
        }
    }

    /**
     * The selected label is set to a different style
     * The style is set to white background and black text
     * The font size is 15px
     * The font family is Arial Black
     * The border radius is 5px
     */
    public void setSelection() {
        try {
            Button button = moviesDisplayButtons.get(currentSelection);
            button.getStyleClass().add("Selected");
        } catch (IndexOutOfBoundsException e) {

        }
    }

    /**
     * Hide the edit and delete buttons
     */

    private void hideEditDeleteButtons() {
        editButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    /**
     * Show the edit and delete buttons
     */

    private void showEditDeleteButtons() {
        if (currentSelection != -1) {
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        }
    }

    /**
     * The deletion of a movie is confirmed
     * We clear the details pane
     * We hide the edit pane
     * We set the current selection to -1
     * We hide the edit and delete buttons
     * We set the initial style
     *
     */

    public void deletionConfirmed() {
        clearDetails();
        hideEditPane();
        currentSelection = -1;
        hideEditDeleteButtons();
        setInitialStyle();
    }




    /**
     * Select the next movie
     *if the current selection is less than the size of the list of movies, we increment the current selection
     * else we set the current selection to 0
     * @param event
     */

    public void selectNext(ActionEvent event) {
        System.out.println("Current selection: " + currentSelection);
        try {
            if (currentSelection < moviesDisplayButtons.size() - 1) {
                currentSelection++;
                showMovieDetails(listener.getMovieFrom(currentSelection));
            } else {
                currentSelection = 0;
            }
            setInitialStyle();
            setSelection();
            showMovieDetails(getMovie(currentSelection));
        } catch (IndexOutOfBoundsException e) {
            titleLabel.setText("Aucun film à afficher");

        }
    }

    /**
     * Select the previous movie
     *if the current selection is greater than 0, we decrement the current selection
     * else we set the current selection to the size of the list of movies - 1
     * @param event
     */
    public void selectPrevious(ActionEvent event) {
        try {

            if (currentSelection > 0) {
                currentSelection--;

            } else {
                currentSelection = moviesDisplayButtons.size() - 1;
            }
            setInitialStyle();
            setSelection();
            showMovieDetails(listener.getMovieFrom(currentSelection));
        } catch (IndexOutOfBoundsException e) {
            titleLabel.setText("Aucun film à affciher");
        }
    }

    /**
     * Fill the edit pane with the movie details
     *
     * @param movie
     */
    private void fillEditPane(Movie movie) {
        nameTextField.setText(movie.getTitle());
        genreTextField.setText(movie.getGenre());
        directorTextField.setText(movie.getDirector());
        durationTextField.setText(String.valueOf(movie.getTotalDuration()));
        synopsisTextField.setText(movie.getSynopsis());
        selectedPathLabel.setText(movie.getImagePath());
    }

    /**
     * Clear the edit pane
     */
    public void clearEditPane() {
        nameTextField.setText("");
        genreTextField.setText("");
        directorTextField.setText("");
        durationTextField.setText("");
        synopsisTextField.setText("");
        selectedPathLabel.setText("...");
    }

    /**
     * Show the edit pane
     */
    private void showEditPane() {
        this.editPane.setVisible(true);
    }

    /**
     * Hide the edit pane
     */
    public void hideEditPane() {
        this.editPane.setVisible(false);
    }


    /**
     * The edit button is clicked
     *
     * @param actionEvent
     */
    public void onEditButtonClick(ActionEvent actionEvent) {
        currentEditType = "modify";
        showEditPane();
        System.out.println("Edit button clicked");
        Movie movieToModify = listener.getMovieFrom(currentSelection);
        fillEditPane(movieToModify);
    }

    /**
     * The add button is clicked
     *
     * @param actionEvent
     */
    public void onAddButtonClick(ActionEvent actionEvent) {
        currentEditType = "add";
        clearEditPane();
        showEditPane();
    }

    /**
     * Prepare the deletion of a movie
     * We get the movie to delete and send it to the listener
     * @param actionEvent
     */
    @FXML
    public void onDeleteButtonClick(ActionEvent actionEvent) {
        System.out.println("Delete button clicked");
        System.out.println("id dans la vue = " + currentSelection);
        Viewable movieToDelete = listener.getMovieFrom(currentSelection);
        System.out.println("id du movie = " + movieToDelete.getId());

        try {
            listener.onDeleteButtonClick(getIdFromMovie(movieToDelete));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * The validate button is clicked
     * We get the values from the text fields and send them to the listener
     *
     * @param event
     * @throws SQLException
     * @throws InvalideFieldsExceptions
     */
    public void onValidateButtonClick(ActionEvent event) throws SQLException, InvalideFieldsExceptions {
        if (currentEditType.equals("add")) {
            listener.onValidateButtonClick(0,nameTextField.getText(), genreTextField.getText(), directorTextField.getText(), durationTextField.getText(), synopsisTextField.getText(), imageToBytes(movieImage.getImage()), currentEditType);
        } else if (currentEditType.equals("modify")) {
            Viewable movieToEdit = listener.getMovieFrom(currentSelection);
            listener.onValidateButtonClick(getIdFromMovie(movieToEdit), nameTextField.getText(), genreTextField.getText(), directorTextField.getText(), durationTextField.getText(), synopsisTextField.getText(), imageToBytes(movieImage.getImage()), currentEditType);
        }
    }

    /***
     * Store an image in a byte array
     * @param image
     * @return
     */

    public byte[] imageToBytes(Image image) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteOutput);
            return byteOutput.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The cancel button is clicked
     * We clear the edit pane and hide it
     * @param actionEvent
     */
    public void onCancelButtonClick(ActionEvent actionEvent) {
        currentEditType = "";
        clearEditPane();
        hideEditPane();
    }

    /**
     * The image choice button is clicked
     * We send the event to the listener
     * @param actionEvent
     */
    public void onImageChoiceButtonClick(ActionEvent actionEvent) {
        listener.onImageChoiceButtonClick();
    }

    /**
     * Clear the movies list
     */
    public void clearMovies() {
        MovieListContainer.setContent(null);
        moviesDisplayButtons.clear();
    }

    /**
     * Refresh the view after an edit
     */
    public void refreshAfterEdit() {
        try {
            if (currentSelection != -1) {
                showMovieDetails(listener.getMovieFrom(currentSelection));
            }
        } catch (IndexOutOfBoundsException e) {
            titleLabel.setText("Aucun film à afficher");
        }
    }

    /**
     * Convert byte array in an image object and display it in imagePane
     * @param imageData
     */

    public void displayImage(byte[] imageData) {
        Image image = new Image(new ByteArrayInputStream(imageData));
        movieImage.setImage(image);
    }


    /**
     * The listener interface for the manager view
     */

    public interface ManagerViewListener {
        Movie getMovieFrom(int index);

        void toLogin() throws IOException;


        void onValidateButtonClick(int movieID, String title, String genre, String director, String duration, String synopsis, byte[] image, String editType) throws SQLException;

        void onImageChoiceButtonClick();


        void onDeleteButtonClick(int movieId) throws SQLException;
    }

    /**
     * The logout button is clicked
     * @param event
     * @throws IOException
     */
    @FXML
    private void toLoginPage(ActionEvent event) throws IOException {
        listener.toLogin();
    }

    /**
     * Get the id of a movie
     * @param movie
     * @return
     */
    private int getIdFromMovie(Viewable movie) {
        return movie.getId();
    }
}