package be.helha.applicine.views;

import be.helha.applicine.controllers.ClientAccountApplication;
import be.helha.applicine.models.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class ClientAccountControllerView {
    private static Stage accountWindow;
    private ClientAccountListener listener;
    private ListView<HBox> ticketContainer;

    private final ClientAccountApplication parentController = new ClientAccountApplication();

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
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750); //charge le fichier FXML et crée une nouvelle scène en définissant sa taille
        accountWindow.setScene(scene); //définit la scène de la fenêtre
        accountWindow.setTitle("Client Account"); //définit le titre de la fenêtre
        accountWindow.show(); //affiche la fenêtre à l'écran
    }

    public void onCloseButtonClicked(ActionEvent actionEvent) throws Exception {
        //TO DO je sauvegarde les données du client
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

    public interface ClientAccountListener {
        //je retourne à la fenêtre du client
        void toClientSide() throws Exception;

        void toClientAccount() throws Exception;
    }
}
