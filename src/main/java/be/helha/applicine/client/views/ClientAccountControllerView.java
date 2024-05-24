package be.helha.applicine.client.views;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.net.URL;

/**
 * This class is used to display the client account page.
 */

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

    /**
     * Sets the listener for the client account page.
     * @param listener The listener to set.
     */

    public void setListener(ClientAccountListener listener) {
        this.listener = listener;
    }

    /**
     * Returns the window of the client account page.
     * @return The window of the client account page.
     */

    public Window getAccountWindow() {
        return accountWindow;
    }

    /**
     * Returns the URL of the FXML resource of the client account page.
     * @return The URL of the FXML resource of the client account page.
     */
    public static URL getFXMLResource() {
        return ClientAccountControllerView.class.getResource("ClientAccount.fxml");
    }

    /**
     * Sets the stage of the client account page.
     * @param fxmlLoader The FXML loader to set the stage of.
     * @param listener The listener to set.
     * @throws Exception If the stage can't be set.
     */
    public static void setStageOf(FXMLLoader fxmlLoader, ClientAccountListener listener) throws Exception{
        accountWindow = new Stage(); //crée une nouvelle fenêtre
        accountWindow.setOnCloseRequest(event -> {
            try {
                AlertViewController.showInfoMessage("Vos données n'ont pas étés enregistrées.");
                listener.toClientSide();
            } catch (Exception e) {
                AlertViewController.showErrorMessage("Erreur lors de la fermeture de la fenêtre du compte client.");
            }
        });
        Scene scene = new Scene(fxmlLoader.load()); //charge le fichier FXML et crée une nouvelle scène en définissant sa taille
        accountWindow.setScene(scene); //définit la scène de la fenêtre
        accountWindow.setTitle("Client Account"); //définit le titre de la fenêtre
        accountWindow.show();
    }

    /**
     * Call the listener to go back to the client side.
     * @param actionEvent The event that triggered the method.
     */
    public void onCloseButtonClicked(ActionEvent actionEvent) {
        listener.toClientSide();
    }

    /**
     *add a ticket to the ticket container.
     */
    public void addTicket(Ticket ticket) throws Exception{
        FXMLLoader ticketPane = new FXMLLoader(TicketPaneViewController.getFXMLResource());
        System.out.println("ticketPane: " + ticketPane);
        HBox pane = ticketPane.load();
        System.out.println("pane: " + pane);
        TicketPaneViewController controller = ticketPane.getController();
        controller.setTicket(ticket);
        ticketContainer.getItems().add(new HBox(pane));
    }

    /**
     * Fill the labels of the client account page.
     * @param client The client to fill the labels with.
     */
    public void fillLabels(Client client) {
        LabelNom.setText(client.getName());
        LabelEmail.setText(client.getEmail());
        LabelPseudo.setText(client.getUsername());
    }

    /**
     * Initialize the client account page.
     * @param client The client to initialize the page with.
     */
    public void initializeClientAccountPage(Client client) {
        try {
            fillLabels(client);
        } catch (Exception e) {
            AlertViewController.showErrorMessage("Erreur lors de l'initialisation de la page du compte client.");
            listener.toClientSide();
        }
    }

    /**
     * This interface is used to listen to the client account page.
     */
    public interface ClientAccountListener {

        //je retourne à la fenêtre du client
        void toClientSide();

        void toClientAccount() throws Exception;

        Client getClientAccount() throws Exception;
    }


}

