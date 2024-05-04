package be.helha.applicine.client.views;


import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Visionable;
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
import java.util.stream.Collectors;


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

    public void setListener(TicketViewListener listener) {
        this.listener = listener;
    }

    public void setMovie(Visionable movie) {
        movieTitle.setText(movie.getTitle());
        movieDuration.setText(movie.getTotalDuration() + " minutes");
        movieDirector.setText(movie.getDirector());
        Image image = new Image(movie.getImagePath());
        movieImageVIew.setImage(image);
    }

    @FXML
    public void onBuyTicketClicked() {
        // Récupérez l'ID de la séance sélectionnée
        String selectedSessionId = String.valueOf(sessionList.getSelectionModel().getSelectedItem().getKey());

        // Récupérez le nombre de tickets que l'utilisateur souhaite acheter
        int normalTickets = Integer.parseInt(normalPlaceNumber.getText());
        int seniorTickets = Integer.parseInt(seniorPlaceNumber.getText());
        int minorTickets = Integer.parseInt(minorPlaceNumber.getText());
        int studentTickets = Integer.parseInt(studentPlaceNumber.getText());

        // Appelez la méthode qui gère l'achat des tickets
        listener.buyTickets(selectedSessionId, normalTickets, seniorTickets, minorTickets, studentTickets);
    }

    private TextField getTextFieldOfButton(ActionEvent actionEvent) {
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
        int currentNumber = Integer.parseInt(buttonTextField.getText());
        buttonTextField.setText(String.valueOf(currentNumber + 1));
    }

    public void updatePrice(double priceValue) {
        System.out.println("update price");
        price.setText(priceValue + " €");
        System.out.println(price.getText());
    }

    public void removeTicket(ActionEvent actionEvent) {
        TextField buttonTextField = getTextFieldOfButton(actionEvent);
        int currentNumber = Integer.parseInt(buttonTextField.getText());
        if (currentNumber > 0) {
            buttonTextField.setText(String.valueOf(currentNumber - 1));
        }
    }

    public void setSessions(List<MovieSession> sessions) {
        // Convertir chaque session en une chaîne de caractères pour l'affichage.
        List<String> sessionStrings = sessions.stream()
                .map(session -> "Session à " + session.getTime())
                .collect(Collectors.toList());

        // Remplir la ListView avec les chaînes de caractères.
        for (int i = 0; i < sessions.size(); i++) {
            sessionList.getItems().add(new Pair<>(sessions.get(i).getId(), sessionStrings.get(i)));
        }
    }

    public void showNoSessionsAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Il n'y a pas de séances disponibles pour ce film.");
        alert.showAndWait();
    }

    public interface TicketViewListener {
        void buyTickets(String sessionId, int normalTickets, int seniorTickets, int minorTickets, int studentTickets);

        void onSessionSelected(String session);
    }
}