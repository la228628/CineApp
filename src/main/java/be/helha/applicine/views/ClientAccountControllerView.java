package be.helha.applicine.views;

import be.helha.applicine.models.Client;
import be.helha.applicine.models.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.IOException;
import java.net.URL;



public class ClientAccountControllerView {

    @FXML
    private Label LabelNom;
    @FXML
    private Label LabelPseudo;
    @FXML
    private Label LabelEmail;
    @FXML
    private Label LabelPassword;

    private static Stage accountWindow;
    private ClientAccountListener listener;
    @FXML
    private ListView<HBox> ticketContainer;

    public void setListener(ClientAccountListener listener) {
        this.listener = listener;
    }

    public static Window getAccountWindow() {
        return accountWindow;
    }

    public static URL getFXMLResource() {
        return ClientAccountControllerView.class.getResource("ClientAccount.fxml");
    }

    //utilisée pour initialiser et afficher une nouvelle fenêtre (ou "stage") dans une application JavaFX
    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        accountWindow = new Stage(); //crée une nouvelle fenêtre
        Scene scene = new Scene(fxmlLoader.load(), 392, 734); //charge le fichier FXML et crée une nouvelle scène en définissant sa taille
        accountWindow.setScene(scene); //définit la scène de la fenêtre
        accountWindow.setTitle("Client Account"); //définit le titre de la fenêtre
        accountWindow.show();//affiche la fenêtre à l'écran

    }

    public void onCloseButtonClicked(ActionEvent actionEvent) throws Exception {
        //TO DO j'informe le client que ses modifications ne seront pas enregistrées
        //je ferme la fenêtre
        System.out.println("Close button clicked");
        //je retourne à la fenêtre précédente (celle du client)
        listener.toClientSide();
    }

    public void addTicket(Ticket ticket) {
        try {
            FXMLLoader ticketPane = new FXMLLoader(TicketPaneViewController.getFXMLResource());
            System.out.println("ticketPane: " + ticketPane);
            HBox pane = ticketPane.load();
            System.out.println("pane: " + pane);
            TicketPaneViewController controller = ticketPane.getController();
            controller.setTicket(ticket);
            ticketContainer.getItems().add(new HBox(pane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeClientAccountPage(Client client) {
        try {
            fillLabels(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillLabels(Client client) {
        LabelNom.setText(client.getName());
        LabelEmail.setText(client.getEmail());
        LabelPseudo.setText(client.getUsername());
    }

    public interface ClientAccountListener {
        //je retourne à la fenêtre du client
        void toClientSide() throws Exception;

        void toClientAccount() throws Exception;

        Client getClientAccount() throws Exception;
    }
}
