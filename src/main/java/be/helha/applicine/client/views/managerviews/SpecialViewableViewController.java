package be.helha.applicine.client.views.managerviews;


import be.helha.applicine.common.models.Viewable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller for the SpecialViewableView window.
 *
 * */

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
    @FXML
    private Button validateButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label totalDurationLabel;


    @FXML
    private TextField sagaNameField;

    @FXML
    private ScrollPane sagaList;
    @FXML
    private Button deleteButton;

    @FXML
    private AnchorPane editPane;


    final VBox VboxToDisplay = new VBox();

    /**
     * This method returns the URL of the FXML file associated with this controller.
     * @return URL
     */

    public static URL getFXMLResource() {
        return SpecialViewableViewController.class.getResource("SpecialViewableView.fxml");
    }

    /**
     * Call the listener to add a movie to the list of movies to add to the saga.
     * @param event the event
     */
    @FXML
    void onAddMovieButtonClick(ActionEvent event) {
        listener.onAddMovieButtonClick();
    }

    /**
     * Call the listener to set the selected movie in the combobox.
     * @param event the event
     */
    @FXML
    void onMovieChoising(ActionEvent event) {
        if (movieChoice.getSelectionModel().getSelectedItem() != null) {
            System.out.println("Film s'électionné: " + movieChoice.getSelectionModel().getSelectedItem());
            System.out.println("Index du film choisi: " + movieChoice.getSelectionModel().getSelectedIndex());
            listener.onMovieChoising(movieChoice.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Call the listener to remove a movie from the list of movies to add to the saga.
     * @param event the event
     */
    @FXML
    void onRemoveMovieButtonClick(ActionEvent event) {
        listener.onRemoveMovieButtonClick();
    }

    /**
     * Call the listener to validate the creation/ edition of a saga.
     * @param event the event
     * @throws SQLException if an error occurs
     */
    @FXML
    void onValidateButtonClick(ActionEvent event) throws SQLException {
        listener.onValidateButtonClick(sagaNameField.getText());
    }


    /**
     * Call the listener to cancel the creation/edition of a saga.
     * @param event the event
     */
    @FXML
    void onCancelButtonClick(ActionEvent event) {
        listener.onCancelButtonClick();
    }


    /**
     * Initialize the view.
     * @throws SQLException if an error occurs
     * @throws IOException if an error occurs
     */
    public void init() throws SQLException, IOException {
        VboxToDisplay.prefWidthProperty().bind(sagaList.widthProperty());

        fillMovieChoice();
        displaySagas();
    }


    /**
     * Fill the combobox with the list of movies.
     * Call the listener to get the list of title movies.
     * @throws SQLException if an error occurs
     * @throws IOException if an error occurs
     */
    public void fillMovieChoice() throws SQLException, IOException {


        listener.displayAllMovie();

        ArrayList<String> moviesTitleToChoose = listener.getMovieTitleList();
        movieChoice.getItems().clear();
        System.out.println("Début de l'affichage des titres ");
        System.out.println("////////////////////////////////");
        for (String title : moviesTitleToChoose) {
            movieChoice.getItems().add(title);
            System.out.println(title);
        }
        System.out.println("/////////////////////////////////");
        System.out.println("Fin affichage des titres");
    }

    /**
     * Call the listener to display the list of sagas.
     */

    public void displaySagas() {
        VboxToDisplay.getChildren().clear();
        listener.displaySagas();
    }


    /**
     * Fill the list of possible movies to add to the saga.
     * @param addedViewablesTitles the list of movies to add
     * @param totalDuration the total duration of the saga
     */

    public void fillAddedMovieChoice(List<String> addedViewablesTitles, Integer totalDuration) {
        movieList.getItems().clear();
        for (String title : addedViewablesTitles) {
            Label label = new Label(title);
            movieList.getItems().add(label);
        }
        setTotalDuration(totalDuration);
    }

    /**
     * Set the total duration of the saga.
     * @param totalDuration the total duration
     */
    private void setTotalDuration(Integer totalDuration) {
        totalDurationLabel.setText("Durée totale: " + convertToDurationString(totalDuration) + " minutes");
    }

    /**
     * Convert the total duration in minutes to a string with the format "h m".
     * @param totalDuration the total duration
     * @return the string
     */

    private String convertToDurationString(Integer totalDuration) {
        int hours = totalDuration / 60;
        int minutes = totalDuration % 60;
        return hours + "h" + minutes;
    }

    /**
     * Close the editPane and clear the fields.
     * reset the style of the buttons.
     */
    public void onCancelConfirm() {
        sagaNameField.clear();
        movieList.getItems().clear();
        totalDurationLabel.setText("");
        this.editPane.setVisible(false);
        this.setInitialStyleForAllButtons();

    }

    /**
     * Clear the list of sagas.
     */
    public void clearSagaList() {
        VboxToDisplay.getChildren().clear();
    }

    /**
     * Display a saga in the list of sagas.
     * @param viewable the saga to displaya
     */
    public void displaySaga(Viewable viewable) {
        Button button = new Button(viewable.getTitle());
        button.getStyleClass().set(0, "buttonS");
        button.prefWidthProperty().bind(VboxToDisplay.widthProperty());
        button.setOnAction(e -> {
            onSagaDisplayButtonClick(viewable, button);
        });
        VboxToDisplay.getChildren().add(button);
        sagaList.setContent(VboxToDisplay);

    }

    /**
     * Call the listener to display the details of selected saga.
     * @param viewable the saga
     * @param button the button
     */

    private void onSagaDisplayButtonClick(Viewable viewable, Button button) {
        showSelectedButton(button);
        listener.onSagaDisplayButtonClick(viewable);
    }

    /**
     * Set the style of the selected button.
     * @param button the button
     */
    private void showSelectedButton(Button button) {
        setInitialStyleForAllButtons();
        button.getStyleClass().add("Selected");
    }

    /**
     * Set the initial style for all buttons.
     */
    private void setInitialStyleForAllButtons() {
        for (int i = 0; i < VboxToDisplay.getChildren().size(); i++) {
            Button button = (Button) VboxToDisplay.getChildren().get(i);
            if (!button.getStyleClass().contains("addButton")) {
                button.getStyleClass().set(0, "buttonS");
                button.getStyleClass().remove("Selected");
            }
        }
    }

    /**
     * Show the editPane to edit a saga.
     * Fill the fields with the saga's information.
     *
     * @param name the name of the saga
     * @param addedViewablesTitles the list of movies
     * @param totalDuration the total duration of the saga
     */
    public void showSagaForEdit(String name, List<String> addedViewablesTitles, Integer totalDuration) {
        sagaNameField.setText(name);
        fillAddedMovieChoice(addedViewablesTitles, totalDuration);
        this.editPane.setVisible(true);
        this.deleteButton.setVisible(true);
    }

    /**
     * Prepare the view to add a saga.
     * Clear the fields and the list of movies.
     * Reset the style of the buttons.
     *
     */
    public void prepareForAddSaga() {
        sagaNameField.clear();
        totalDurationLabel.setText("");
        movieList.getItems().clear();
        setInitialStyleForAllButtons();
        this.editPane.setVisible(true);
        this.deleteButton.setVisible(false);
    }

    /**
     * Call the listener to add a saga.
     * @param event the event
     */
    public void onAddSagaButtonClick(ActionEvent event) {
        listener.onAddSagaButtonClick();
    }

    /**
     * Call the listener to delete a saga.
     * @param event the event
     * @throws SQLException if an error occurs
     */

    public void onDeleteSagaButtonClick(ActionEvent event) throws SQLException {
        listener.onSagaDeleteButtonClick();
    }

    /**
     * This interface is used to define the methods that the controller must implement.
     */

    public interface SpecialViewableListener {
        void onAddMovieButtonClick();

        void onRemoveMovieButtonClick();

        void displayAllMovie();

        void onMovieChoising(int selectedIndex);


        void onValidateButtonClick(String name) throws SQLException;

        void onCancelButtonClick();

        void displaySagas();

        void onSagaDisplayButtonClick(Viewable viewable);

        void onAddSagaButtonClick();

        void onSagaDeleteButtonClick() throws SQLException;

        ArrayList<String> getMovieTitleList();
    }

    /**
     * Refresh the view.
     * @throws SQLException
     */
    public void refresh() throws SQLException {
        try {
            fillMovieChoice();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        displaySagas();
    }

    /**
     * Set the listener.
     * @param listener the listener
     */

    public void setListener(SpecialViewableListener listener) {
        this.listener = listener;
    }
}
