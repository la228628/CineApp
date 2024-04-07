package be.helha.applicine.views.managerviews;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

import java.net.URL;

public class SessionManagerViewController {

    @FXML
    private DatePicker DateSelector;

    @FXML
    private ChoiceBox<?> movieSelector;

    @FXML
    private ChoiceBox<?> roomSelector;

    @FXML
    private ListView<?> sessionsList;

    public static URL getFXMLResource() {
        return SessionManagerViewController.class.getResource("sessionManagerView.fxml");
    }
}
