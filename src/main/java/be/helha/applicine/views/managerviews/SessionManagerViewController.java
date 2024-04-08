package be.helha.applicine.views.managerviews;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;

public class SessionManagerViewController {


    @FXML
    private DatePicker DateSelector;

    @FXML
    private ChoiceBox<String> hourSelector;

    @FXML
    private ChoiceBox<String> minuteSelector;

    @FXML
    private ChoiceBox<String> movieSelector;

    @FXML
    private ChoiceBox<Integer> roomSelector;

    @FXML
    private ListView<Button> sessionsList;

    @FXML
    private AnchorPane sessionEditPane;


    public void intialize() {
        setHourSelectorPossibilities();
        setMinuteSelectorPossibilities();
        Button button = addButton();
        sessionsList.getItems().add(button);
    }

    private Button addButton() {
        Button button = new Button("+");
        button.getStyleClass().add("addButton");
        button.prefWidthProperty().bind(sessionsList.widthProperty());
        button.setOnAction(event -> {
            addNewSession();
        });
        return button;
    }

    private void addNewSession() {

    }

    public static URL getFXMLResource() {
        return SessionManagerViewController.class.getResource("sessionManagerView.fxml");
    }

    public DatePicker getDateSelector() {
        return DateSelector;
    }

    public ChoiceBox<?> getHourSelector() {
        return hourSelector;
    }

    public ChoiceBox<?> getMinuteSelector() {
        return minuteSelector;
    }

    public ChoiceBox<?> getMovieSelector() {
        return movieSelector;
    }

    public ChoiceBox<?> getRoomSelector() {
        return roomSelector;
    }

    public void setHourSelectorPossibilities() {
        for(Integer i = 0; i < 24; i++) {
            if(i < 10)
                hourSelector.getItems().add("0" + i);
            else
                hourSelector.getItems().add(i.toString());
        }
    }

    public void setMinuteSelectorPossibilities() {
        for(Integer i = 0; i < 60; i++) {
            if(i < 10)
                minuteSelector.getItems().add("0" + i);
            else
                minuteSelector.getItems().add(i.toString());
        }
    }

}
