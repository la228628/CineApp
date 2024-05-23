package be.helha.applicine.client.controllers;

import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.Session;
import be.helha.applicine.common.models.Ticket;
import be.helha.applicine.client.views.ClientAccountControllerView;
import be.helha.applicine.common.models.request.GetTicketByClientRequest;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientAccountApplication extends Application implements ClientAccountControllerView.ClientAccountListener, ServerRequestHandler.Listener {
    //renvoie le fichier FXML de la vue ClientAccount
    private final FXMLLoader fxmlLoader = new FXMLLoader(ClientAccountControllerView.getFXMLResource());

    //permet de communiquer avec le parentController (MasterApplication) pour changer de fenêtre et de contrôleur de vue.
    private MasterApplication parentController;

    private ServerRequestHandler serverRequestHandler;


    public ClientAccountApplication(MasterApplication masterApplication) {
        try {
            this.serverRequestHandler = new ServerRequestHandler(this);
            this.parentController = masterApplication;
        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage());
            AlertViewController.showErrorMessage("Problème de chargement de la page, veuillez réésayer plus tard. Contactez un administrateur si le problème se maintient.");
            parentController.closeAllWindows();
            parentController.toClient();
        }
    }

    //permet de fermer la fenêtre du client account et de retourner à la fenêtre du client. Je parle au parentController (masterApplication) pour changer de fenêtre.

    /**
     * Permit to close the client account window and return to the client window.
     */
    @Override
    public void toClientSide() {
        parentController.toClient();
    }

    /**
     * Permit to close the client account window and return to the login window.
     */
    @Override
    public void toClientAccount() {
        parentController.toClientAccount();
    }

    /**
     * Permit to get the client account from the actual session.
     * @return the client account.
     */
    @Override
    public Client getClientAccount() {
        Session session = parentController.getSession();
        return session.getCurrentClient();
    }

    /**
     * starts the client account window by setting the stage of the fxmlLoader and initializing the client account page.
     * @param stage The stage of the application.
     */
    @Override
    public void start(Stage stage) {
        try {
            ClientAccountControllerView.setStageOf(fxmlLoader, this);
            ClientAccountControllerView clientAccountControllerView = fxmlLoader.getController();
            clientAccountControllerView.setListener(this);

            parentController.setCurrentWindow(clientAccountControllerView.getAccountWindow());

            //initialise la page du client account (affiche les tickets et les informations du client)
            clientAccountControllerView.initializeClientAccountPage(getClientAccount());
            sendRequestTicketByClient(getClientAccount().getId());
        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage());
            AlertViewController.showErrorMessage("Problème de chargement de la page, veuillez réésayer plus tard. Contactez un administrateur si le problème se maintient.");
            parentController.closeAllWindows();
            parentController.toClient();
        }
    }

    private void sendRequestTicketByClient(int id) throws IOException {
        serverRequestHandler.sendRequest(new GetTicketByClientRequest(id));
    }

    /**
     * adds tickets to the client account page.
     *
     * @param tickets The tickets to add.
     * @param clientAccountControllerView The view of the client account.
     */
    public void addTickets(List<Ticket> tickets, ClientAccountControllerView clientAccountControllerView) {
        List<Ticket> ticketsWithNullSession = new ArrayList<>();
        for (Ticket ticket : tickets) {
            try {
                clientAccountControllerView.addTicket(ticket);
            } catch (Exception e) {
                ticketsWithNullSession.add(ticket);
            }
        }
    }

    @Override
    public void onResponseReceive(Object response) {
        //on ajoute le Platform.runLater pour que le thread principal de JavaFX puisse mettre à jour l'interface graphique
        Platform.runLater(() -> {
            if (response instanceof List) {
                List<Ticket> tickets = (List<Ticket>) response;
                ClientAccountControllerView clientAccountControllerView = fxmlLoader.getController();
                addTickets(tickets, clientAccountControllerView);
            }
        });
    }

    @Override
    public void onConnectionLost() {

    }
}
