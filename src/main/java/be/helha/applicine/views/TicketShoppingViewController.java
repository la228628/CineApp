package be.helha.applicine.views;

import be.helha.applicine.controllers.TicketPageController;
import be.helha.applicine.models.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.ArrayList;


public class TicketShoppingViewController {
    @FXML
    public Label price;
    @FXML
    public Label errorBox;
    @FXML
    public TextField normalPlaceNumber;
    @FXML
    public TextField seniorPlaceNumber;
    @FXML
    public TextField minorPlaceNumber;
    @FXML
    public TextField studentPlaceNumber;

    private TextField getTextFieldOfButton(ActionEvent actionEvent){
        errorBox.setVisible(false);
        Node sourceNode = (Node) actionEvent.getSource();
        HBox container = (HBox) sourceNode.getParent();
        return (TextField) container.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .findFirst()
                .orElse(null);
    }

    public void addTicket(ActionEvent actionEvent) {
        TextField buttonTextField = getTextFieldOfButton(actionEvent);
        TicketPageController.addTicket(buttonTextField);
    }

    public void updatePrice(double priceValue){
        System.out.println("update price");
        price.setText(priceValue + " €");
        System.out.println(price.getText());
    }

    public void removeTicket(ActionEvent actionEvent){
        TextField buttonTextField = getTextFieldOfButton(actionEvent);
        TicketPageController.removeTicket(buttonTextField);
    }

    public void buyTickets(ActionEvent actionEvent) {
        int normalTickets = Integer.parseInt(normalPlaceNumber.getText());
        int seniorTickets = Integer.parseInt(seniorPlaceNumber.getText());
        int minorTickets = Integer.parseInt(minorPlaceNumber.getText());
        int studentTickets = Integer.parseInt(studentPlaceNumber.getText());
        TicketPageController.buyTickets(normalTickets, seniorTickets, minorTickets, studentTickets);
    }
}