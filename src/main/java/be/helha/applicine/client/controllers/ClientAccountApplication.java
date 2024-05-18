package be.helha.applicine.client.controllers;

import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.Session;
import be.helha.applicine.common.models.Ticket;
import be.helha.applicine.client.views.ClientAccountControllerView;
import be.helha.applicine.common.models.request.GetTicketByClientRequest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientAccountApplication extends Application implements ClientAccountControllerView.ClientAccountListener {
    //renvoie le fichier FXML de la vue ClientAccount
    private final FXMLLoader fxmlLoader = new FXMLLoader(ClientAccountControllerView.getFXMLResource());

    //permet de communiquer avec le parentController (MasterApplication) pour changer de fenêtre et de contrôleur de vue.
    private MasterApplication parentController;

    public ClientAccountApplication(MasterApplication masterApplication){
            this.parentController = masterApplication;
    }

    //permet de fermer la fenêtre du client account et de retourner à la fenêtre du client. Je parle au parentController (masterApplication) pour changer de fenêtre.


    /**
     * Permit to close the client account window and return to the client window.
     *
     * @throws Exception
     */
    @Override
    public void toClientSide(){
        parentController.toClient();
    }

    /**
     * Permit to close the client account window and return to the login window.
     *
     * @throws Exception
     */
    @Override
    public void toClientAccount() throws Exception {
        parentController.toClientAccount();
    }

    /**
     * Permit to get the client account from the actual session.
     *
     * @return
     * @throws SQLException
     */
    @Override
    public Client getClientAccount() throws SQLException{
            Session session = parentController.getSession();
            return session.getCurrentClient();
    }

    /**
     * starts the client account window by setting the stage of the fxmlLoader and initializing the client account page.
     *
     * @param stage
     * @throws Exception
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
            List<Ticket> tickets = getTicketsByClient(getClientAccount().getId());
            addTickets(tickets, clientAccountControllerView);
        } catch (Exception e){
            System.out.println("Error handling client: " + e.getMessage());
            AlertViewController.showErrorMessage("Problème de chargement de la page, veuillez réésayer plus tard. Contactez un administrateur si le problème se maintient.");
            parentController.closeAllWindows();
            parentController.toClient();
        }
    }

    private List<Ticket> getTicketsByClient(int id) throws SQLException, IOException, ClassNotFoundException {
        ServerRequestHandler serverRequestHandler = parentController.getServerRequestHandler();
        return (List<Ticket>) serverRequestHandler.sendRequest(new GetTicketByClientRequest(id));
    }

    /**
     * adds tickets to the client account page.
     * @param tickets
     */
    public void addTickets(List<Ticket> tickets, ClientAccountControllerView clientAccountControllerView) throws Exception{
        List<Ticket> ticketsWithNullSession = new ArrayList<>();
        for (Ticket ticket : tickets) {
            try {
                clientAccountControllerView.addTicket(ticket);
            }catch (Exception e){
                ticketsWithNullSession.add(ticket);
            }
        }

//        if (!ticketsWithNullSession.isEmpty()) {
////            clientAccountControllerView.showDeletedSessionsAlert();
////            for (Ticket ticket : ticketsWithNullSession) {
////                ticketDAO.deleteTicket(ticket.getId());
////            }
////        }

    }
}
