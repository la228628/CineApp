package be.helha.applicine.client.controllers;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.Session;
import be.helha.applicine.common.models.Ticket;
import be.helha.applicine.client.views.ClientAccountControllerView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

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

    @Override
    public void alertError(String errorMessage) {
        popUpAlert(errorMessage);
    }

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

    private void popUpAlert(String message) {
        parentController.popUpAlert(message);
    }

    /**
     * starts the client account window by setting the stage of the fxmlLoader and initializing the client account page.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            ClientAccountControllerView.setStageOf(fxmlLoader);
            ClientAccountControllerView clientAccountControllerView = fxmlLoader.getController();
            clientAccountControllerView.setListener(this);

            //définit la fenêtre courante dans le parentController comme étant la fenêtre gérée par ManagerViewController.
            parentController.setCurrentWindow(clientAccountControllerView.getAccountWindow());

            //initialise la page du client account (affiche les tickets et les informations du client)
            clientAccountControllerView.initializeClientAccountPage(getClientAccount());
            List<Ticket> tickets = getTicketsByClient(getClientAccount().getId());
            addTickets(tickets);
        }catch(SQLException e){
            System.out.println("Error handling client: " + e.getMessage());
            popUpAlert("Problème de récupération du compte, veuillez rééssayer plus tard.");
        }catch (Exception e){
            System.out.println("Error handling client: " + e.getMessage());
            popUpAlert("Problème de chargement de la page, veuillez réésayer plus tard. Contactez un administrateur si le problème se maintient.");
        }
    }

    private List<Ticket> getTicketsByClient(int id) throws SQLException, IOException, ClassNotFoundException {
        ServerRequestHandler serverRequestHandler = parentController.getServerRequestHandler();
        return (List<Ticket>) serverRequestHandler.sendRequest("GET_TICKETS_BY_CLIENT " + id);
    }

    /**
     * adds tickets to the client account page.
     *
     * @param tickets
     */
    public void addTickets(List<Ticket> tickets) {
        ClientAccountControllerView clientAccountControllerView = fxmlLoader.getController();
        List<Ticket> ticketsWithNullSession = new ArrayList<>();
        for (Ticket ticket : tickets) {
            try {
                clientAccountControllerView.addTicket(ticket);
            } catch (NullPointerException e) {
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
