package be.helha.applicine.views;

import be.helha.applicine.models.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class TicketShoppingViewController {
    @FXML
    public TextField seniorPlaceNumber;
    @FXML
    public TextField minorPlaceNumber;
    @FXML
    public TextField studentPlaceNumber;
    @FXML
    private TextField normalPlaceNumber;

    ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    public void addNormal(ActionEvent actionEvent) {
            Node sourceNode = (Node) actionEvent.getSource();
            HBox container = (HBox) sourceNode.getParent();
            TextField textField = (TextField) container.getChildren().stream()
                    .filter(node -> node instanceof TextField)
                    .findFirst()
                    .orElse(null);
            if (textField != null) {
                System.out.println("text: " + textField.getText());
            }
        }
}
