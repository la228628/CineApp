package be.helha.applicine.controllers;

import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.dao.TicketDAO;
import be.helha.applicine.dao.impl.ClientsDAOImpl;
import be.helha.applicine.dao.impl.TicketDAOImpl;
import be.helha.applicine.models.Client;
import be.helha.applicine.models.Session;
import be.helha.applicine.models.Ticket;
import be.helha.applicine.views.ClientAccountControllerView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientAccountApplication extends Application implements ClientAccountControllerView.ClientAccountListener {

    private ClientsDAO clientsDAO = new ClientsDAOImpl();
    private TicketDAO ticketDAO = new TicketDAOImpl();
    //renvoie le fichier FXML de la vue ClientAccount
    private final FXMLLoader fxmlLoader = new FXMLLoader(ClientAccountControllerView.getFXMLResource());

    //permet de communiquer avec le parentController (MasterApplication) pour changer de fenêtre et de contrôleur de vue.
    private final MasterApplication parentController;

    public ClientAccountApplication(MasterApplication masterApplication) {
        this.parentController = masterApplication;
    }

    //permet de fermer la fenêtre du client account et de retourner à la fenêtre du client. Je parle au parentController (masterApplication) pour changer de fenêtre.

    /**
     * Permit to close the client account window and return to the client window.
     *
     * @throws Exception
     */
    @Override
    public void toClientSide() throws Exception {
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
    public Client getClientAccount() throws SQLException {
        try {
            Session session = parentController.getSession();
            Client currentClient = session.getCurrentClient();
            return clientsDAO.getClient(currentClient.getId());
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * starts the client account window by setting the stage of the fxmlLoader and initializing the client account page.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        ClientAccountControllerView.setStageOf(fxmlLoader);
        ClientAccountControllerView clientAccountControllerView = fxmlLoader.getController();
        //permet à la vue de communiquer avec le controller de l'application ClientAccount
        clientAccountControllerView.setListener(this);
        //définit la fenêtre courante dans le parentController comme étant la fenêtre gérée par ManagerViewController.
        parentController.setCurrentWindow(ClientAccountControllerView.getAccountWindow());
        //initialise la page du client account (affiche les tickets et les informations du client)
        clientAccountControllerView.initializeClientAccountPage(getClientAccount());

        List<Ticket> tickets = ticketDAO.getTicketsByClient(getClientAccount().getId());
        addTickets(tickets);
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
            System.out.println("ticket: " + ticket.getId());
            try {
                clientAccountControllerView.addTicket(ticket);
            } catch (NullPointerException e) {
                ticketsWithNullSession.add(ticket);
            }
        }

        if (!ticketsWithNullSession.isEmpty()) {
            clientAccountControllerView.showDeletedSessionsAlert();
            for (Ticket ticket : ticketsWithNullSession) {
                ticketDAO.deleteTicket(ticket.getId());
                System.out.println("id du ticket à supprimer: " + ticket.getId());
            }
        }

    }

}
