package be.helha.applicine.client.views;


import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Viewable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import java.util.List;

/**
 * This class is the controller for the ticket shopping view.
 */
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

    @FXML
    private Label movieDirector;

    @FXML
    private ImageView movieImageVIew;

    @FXML
    private Label movieDuration;

    @FXML
    private Label movieTitle;

    private TicketViewListener listener;
    @FXML
    private ListView<Pair<Integer, String>> sessionList;

    /**
     * Set the listener for the ticket view.
     * @param listener the listener to set.
     */
    public void setListener(TicketViewListener listener) {
        this.listener = listener;
    }

    /**
     * Set the movie to display in the ticket view.
     * @param movie the movie to display.
     */
    public void setMovie(Viewable movie) {
        movieTitle.setText(movie.getTitle());
        movieDuration.setText(movie.getTotalDuration() + " minutes");
        movieDirector.setText(movie.getDirector());
        Image image = new Image(movie.getImagePath());
        movieImageVIew.setImage(image);
    }

    /**
     * Get the selected session from the list view.
     * Get the session ID and call the listener to handle the selection.
     */
    @FXML
    public void onBuyTicketClicked() {
        String selectedSessionId = null;
        try {
            selectedSessionId = String.valueOf(sessionList.getSelectionModel().getSelectedItem().getKey());
        }catch (NullPointerException e){
            AlertViewController.showInfoMessage("Veuillez sélectionner une séance.");
        }
        int normalTickets = Integer.parseInt(normalPlaceNumber.getText());
        int seniorTickets = Integer.parseInt(seniorPlaceNumber.getText());
        int minorTickets = Integer.parseInt(minorPlaceNumber.getText());
        int studentTickets = Integer.parseInt(studentPlaceNumber.getText());

        // Appelez la méthode qui gère l'achat des tickets
        listener.buyTickets(selectedSessionId, normalTickets, seniorTickets, minorTickets, studentTickets);
        ticketsBought();
    }

    /**
     * Show an info message when the tickets are bought.
     */
    private void ticketsBought() {
        AlertViewController.showInfoMessage("Les tickets ont été achetés avec succès. Vous pouvez les consultez sur votre profil.");
        listener.closeWindow();
    }

    /**
     * Get the text field of the button that was clicked.
     * @param actionEvent the event that triggered the method.
     * @return the text field of the button that was clicked.
     */
    private TextField getTextFieldOfButton(ActionEvent actionEvent) {
        errorBox.setVisible(false);
        Node sourceNode = (Node) actionEvent.getSource();
        HBox container = (HBox) sourceNode.getParent();
        return (TextField) container.getChildren().stream()
                .filter(node -> node instanceof TextField)
                .findFirst()
                .orElse(null);
    }

    /**
     * Add a ticket to the text field of the button that was clicked.
     * @param actionEvent the event that triggered the method.
     */
    public void addTicket(ActionEvent actionEvent) {
        TextField buttonTextField = getTextFieldOfButton(actionEvent);
        int currentNumber = Integer.parseInt(buttonTextField.getText());
        buttonTextField.setText(String.valueOf(currentNumber + 1));
        updatePrice();
    }

    /**
     * Remove a ticket from the text field of the button that was clicked.
     * @param actionEvent the event that triggered the method.
     */

    public void removeTicket(ActionEvent actionEvent) {
        TextField buttonTextField = getTextFieldOfButton(actionEvent);
        int currentNumber = Integer.parseInt(buttonTextField.getText());
        if (currentNumber > 0) {
            buttonTextField.setText(String.valueOf(currentNumber - 1));
        }
        updatePrice();
    }

    /**
     * Update the price of the selected tickets to buy.
     */
    private void updatePrice() {
        int normalTickets = Integer.parseInt(normalPlaceNumber.getText());
        int seniorTickets = Integer.parseInt(seniorPlaceNumber.getText());
        int minorTickets = Integer.parseInt(minorPlaceNumber.getText());
        int studentTickets = Integer.parseInt(studentPlaceNumber.getText());

        int totalPrice = normalTickets * 8 + seniorTickets * 6 + minorTickets * 5 + studentTickets * 4;
        price.setText(totalPrice + " €");
    }

    /**
     * Set the sessions to display in the ticket view.
     * @param sessions the sessions to display.
     */
    public void setSessions(List<MovieSession> sessions) {
        List<String> sessionStrings = sessions.stream()
                .map(session -> "Session à " + session.getTime())
                .toList();

        for (int i = 0; i < sessions.size(); i++) {
            sessionList.getItems().add(new Pair<>(sessions.get(i).getId(), sessionStrings.get(i)));
        }
    }

    /**
     * Interface for the ticket view listener.
     */
    public interface TicketViewListener {
        void buyTickets(String sessionId, int normalTickets, int seniorTickets, int minorTickets, int studentTickets);
        void onSessionSelected(String session);
        void closeWindow();
    }
}