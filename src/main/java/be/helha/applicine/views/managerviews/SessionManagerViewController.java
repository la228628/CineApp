package be.helha.applicine.views.managerviews;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Session;
import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import kotlin.Pair;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SessionManagerViewController {


    @FXML
    private DatePicker DateSelector;

    @FXML
    private ChoiceBox<String> hourSelector;

    @FXML
    private ChoiceBox<String> minuteSelector;

    @FXML
    private ComboBox<String> movieSelector;

    @FXML
    private ChoiceBox<Integer> roomSelector;

    @FXML
    private ListView<Button> sessionsList;

    @FXML
    private AnchorPane sessionEditPane;

    @FXML
    private Label timeShowLabel;

    @FXML
    private Label freePlacesLabel;

    @FXML
    private Button validateButton;

    @FXML
    private Button cancelButton;
    @FXML
    private Label editTypeInfoLabel;


    @FXML
    private ChoiceBox<String> versionSelector;

    @FXML
    private Button deleteButton;

    private List<Button> sessionButtons = new ArrayList<Button>();
    private SessionManagerViewListener listener;

    private String currentEditionType;
    private Integer currentMovieSelection = -1;

    private Integer currentRoomSelection = -1;

    public ArrayList<Pair<Integer, Integer>> boxIdMovieIdAssociationList;
    private int currentSessionID = -1;


    public void intialize() {
        this.boxIdMovieIdAssociationList = new ArrayList<Pair<Integer, Integer>>();
        setHourSelectorPossibilities();
        setMinuteSelectorPossibilities();
        setVersionSelectorPossibilities();
        setPossibleMovies();
        setPossibleRooms();
        Button button = addButton();
        sessionsList.getItems().add(button);
        this.currentEditionType = "";
    }

    private Button addButton() {
        Button button = new Button("+");
        button.getStyleClass().add("addButton");
        button.prefWidthProperty().bind(sessionsList.widthProperty());
        button.setOnAction(event -> {
            onAddButtonClick();
        });
        return button;
    }

    private void onAddButtonClick() {
        setInitialStyleButtons();
        this.currentEditionType = "add";
        sessionEditPane.setVisible(true);
        this.deleteButton.setVisible(false);
        this.editTypeInfoLabel.setText("Ajouter une séance");
        clearFields();
        this.sessionEditPane.setVisible(true);
    }

    public static URL getFXMLResource() {
        return SessionManagerViewController.class.getResource("sessionManagerView.fxml");
    }


    public void setHourSelectorPossibilities() {
        for (Integer i = 0; i < 24; i++) {
            if (i < 10)
                hourSelector.getItems().add("0" + i);
            else
                hourSelector.getItems().add(i.toString());
        }
    }

    public void setMinuteSelectorPossibilities() {
        for (Integer i = 0; i < 60; i++) {
            if (i < 10)
                minuteSelector.getItems().add("0" + i);
            else
                minuteSelector.getItems().add(i.toString());
        }
    }

    public void setVersionSelectorPossibilities() {
        versionSelector.getItems().add("2D");
        versionSelector.getItems().add("3D");

    }

    public void onValidateButtonClick(ActionEvent event) throws SQLException, InvalideFieldsExceptions {
        if (currentEditionType.equals("add")) {
            try {
                listener.onValidateButtonClick(0, getMovie(currentMovieSelection).getId(), roomSelector.getValue(), versionSelector.getValue(), converDateAndHourToDateTime(), this.currentEditionType);
            }catch (IndexOutOfBoundsException e){
                listener.onValidateButtonClick(0, -1, roomSelector.getValue(), versionSelector.getValue(), converDateAndHourToDateTime(), this.currentEditionType);
            }
        }else if (currentEditionType.equals("modify")){
            listener.onValidateButtonClick(currentSessionID, getMovie(currentMovieSelection).getId(), roomSelector.getValue(), versionSelector.getValue(), converDateAndHourToDateTime(), this.currentEditionType);
        }
    }

    public void setListener(SessionManagerViewListener listener) {
        this.listener = listener;
    }

    public void addPossibleName(String name) {
        movieSelector.getItems().add(name);
    }

    public void addPossibleRoom(int number) {
        roomSelector.getItems().add(number);
    }

    public void onRoomSelectedEvent(ActionEvent event) throws SQLException {
        currentRoomSelection = roomSelector.getValue();
        listener.onRoomSelectedEvent(currentRoomSelection);
    }

    public void setRoomCapacity(int capacity) {
        freePlacesLabel.setText(capacity + " places libres");
    }

    public void onCancelButtonClick(ActionEvent event) {
        this.sessionsList.getItems().remove(this.sessionsList.getItems().size() - 1);
        this.sessionEditPane.setVisible(false);
        refreshAfterEdit();
    }

    public void displaySession(Session session) {
        Button button = new Button(session.getMovie().getTitle() + " " + session.getTime() + " " + session.getRoom().getNumber());
        button.prefWidthProperty().bind(sessionsList.widthProperty());

        button.onMouseClickedProperty().set((event -> {
            onSessionButtonClick(session);
            setSelection(button);

        }));
        sessionButtons.add(button);
        setInitialStyleButtons();
        sessionsList.getItems().add(button);
    }

    private void onSessionButtonClick(Session session) {


        this.currentSessionID = session.getId();
        System.out.println("l'ID de la session est "+currentSessionID);
        this.currentEditionType = "modify";
        sessionEditPane.setVisible(true);
        this.deleteButton.setVisible(true);
        this.editTypeInfoLabel.setText("Modifier une séance");
        this.sessionEditPane.setVisible(true);

        setSessionFields(session);
    }

    private void setSessionFields(Session session) {
        DateSelector.setValue(session.getDate());
        hourSelector.setValue(session.getHourFromTime());
        minuteSelector.setValue(session.getMinuteFromTime());
        movieSelector.setValue(session.getMovie().getTitle());
        roomSelector.setValue(session.getRoom().getNumber());
        versionSelector.setValue(session.getSession());
    }

    private void clearFields() {
        DateSelector.setValue(null);
        hourSelector.setValue(null);
        minuteSelector.setValue(null);
        movieSelector.setValue(null);
        roomSelector.setValue(null);
        versionSelector.setValue(null);
        timeShowLabel.setText("...");
        freePlacesLabel.setText("");
    }

    public void clearSessions() {
        sessionsList.getItems().clear();
        sessionButtons.clear();
    }

    public void refreshAfterEdit() {
        clearFields();
        setInitialStyleButtons();
        this.currentEditionType = "";
        this.currentSessionID = -1;
        sessionEditPane.setVisible(false);
        sessionsList.getItems().add(addButton());
    }

    public void onDeleteButtonClick(ActionEvent event) {
        listener.onDeleteButtonClick(currentSessionID);
    }

    public interface SessionManagerViewListener {
        void onValidateButtonClick(Integer sessionId, Integer movieId, Integer roomId, String version, String convertedDateTime, String currentEditType) throws SQLException, InvalideFieldsExceptions;

        void setPossibleMovies();

        Integer getMovieDuration(int id);

        void setPossibleRooms();

        Movie getMovieFrom(Integer currentSelection);

        void onRoomSelectedEvent(Integer value) throws SQLException;


        void onDeleteButtonClick(int currentSessionID);
    }

    public void setPossibleMovies() {
        listener.setPossibleMovies();
    }

    public void setPossibleRooms() {
        listener.setPossibleRooms();

    }

    public void onHourSelectedEvent(ActionEvent e) {
        if (!(minuteSelector.getValue() == null) && movieSetted() && !(hourSelector.getValue() == null)){
            setTimeShowLabel();
        }
    }


    public void onMinuteSelectedEvent(ActionEvent e) {
        if (!(hourSelector.getValue() == null) && movieSetted() &&!(minuteSelector.getValue() == null)){
            setTimeShowLabel();
        }
    }

    private void setTimeShowLabel() {
        Integer hour = Integer.parseInt(hourSelector.getValue());
        Integer minute = Integer.parseInt(minuteSelector.getValue());
        int movieId = getMovie(currentMovieSelection).getId();
        Integer duration = getMovieDuration(movieId);
        LocalTime time = LocalTime.of(hour, minute);
        LocalTime time1 = time.plusMinutes(duration);
        timeShowLabel.setText(time + " -> " + time1);
    }

    private Movie getMovie(int id) {
        return listener.getMovieFrom(id);
    }


    private Integer getMovieDuration(int id) {
        return listener.getMovieDuration(id);
    }

    @FXML
    private void onMovieSelectedEvent(ActionEvent e) {
        currentMovieSelection = movieSelector.getSelectionModel().getSelectedIndex();
        if (timeSetted()) {
            setTimeShowLabel();
        }
        System.out.println(currentMovieSelection);
    }

    private boolean timeSetted() {
        return !(hourSelector.getValue() == null || minuteSelector.getValue() == null);
    }

    private boolean movieSetted() {
        return !(movieSelector.getValue() == null);
    }

    private String converDateAndHourToDateTime() {
        try{
            return DateSelector.getValue().toString() + " " + hourSelector.getValue() + ":" + minuteSelector.getValue();
        } catch (NullPointerException e) {
            return "";
        }
    }


    private void setInitialStyleButtons() {
        for (Button button : sessionButtons) {
            button.getStyleClass().set(0,"buttonS");
            button.getStyleClass().remove("Selected");
        }
    }

    public void setSelection(Button button) {
        try {
            setInitialStyleButtons();
            button.getStyleClass().add("Selected");
        } catch (IndexOutOfBoundsException e) {

        }
    }

    public void clearPossibleNames() {
        movieSelector.getItems().clear();
    }


}
