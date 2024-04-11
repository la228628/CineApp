package be.helha.applicine.views;

import be.helha.applicine.models.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.Objects;

public class TicketShoppingViewController {
    @FXML
    public Label price;
    @FXML
    public Label errorBox;
    @FXML
    public TextField normalPlaceNumber;

    private TextField getButtonTextField(ActionEvent actionEvent){
        errorBox.setVisible(false);
        Node sourceNode = (Node) actionEvent.getSource();
        HBox container = (HBox) sourceNode.getParent();
        return (TextField) container.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .findFirst()
                .orElse(null);
    }
    public void addTicket(ActionEvent actionEvent) {
        TextField buttonTextField = getButtonTextField(actionEvent);
        try {
            int currentTicketNumber = Integer.parseInt(Objects.requireNonNull(buttonTextField).getText());
            buttonTextField.setText(String.valueOf(++currentTicketNumber));
        }catch (Exception e){
            errorBox.setVisible(true);
        }
    }
    public void removeTicket(ActionEvent actionEvent){
        TextField buttonTextField = getButtonTextField(actionEvent);
        try {
            int currentTicketNumber = Integer.parseInt(Objects.requireNonNull(buttonTextField).getText());
            if(currentTicketNumber>0)
                buttonTextField.setText(String.valueOf(--currentTicketNumber));
        }catch (Exception e){
            errorBox.setVisible(true);
        }
    }
}