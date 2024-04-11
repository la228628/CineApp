package be.helha.applicine.views;

import eu.hansolo.tilesfx.TestLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class TicketShoppingViewController {
    @FXML
    public Label price;
    @FXML
    public Label errorBox;
    @FXML
    public TextField normalPlaceNumber;

    private List<Double> ticketPrices =  new ArrayList<>();
    private TextField getButtonTextField(ActionEvent actionEvent){
        errorBox.setVisible(false);
        Node sourceNode = (Node) actionEvent.getSource();
        HBox container = (HBox) sourceNode.getParent();
        TextField textField = (TextField) container.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .findFirst()
                .orElse(null);
        updatePrice((VBox) container.getParent());
        return textField;
    }

    public List<TextField> getTextFields(HBox menuButton) {
        return menuButton.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .map(node -> (TextField) node)
                .toList();
    }

    public ArrayList<Integer> getTicketsNumber(){

    }

    public void updatePrice(VBox menuButton){
        System.out.println("updatePrice");
        for (int i = 0; i<menuButton.getChildren().size(); i++){
            HBox hBox = (HBox) menuButton.getChildren().get(i);
            List<TextField> textFields = getTextFields(hBox);
            for (TextField textField : textFields){
                try {
                    int currentTicketNumber = Integer.parseInt(textField.getText());
                    ticketPrices.set(i, (currentTicketNumber * 8.5));
                }catch (Exception e){
                    errorBox.setVisible(true);
                }
            }
        }
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