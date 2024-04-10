package be.helha.applicine.controllers;

import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.dao.MovieDAO;
import be.helha.applicine.dao.impl.ClientsDAOImpl;
import be.helha.applicine.dao.impl.MovieDAOImpl;
import be.helha.applicine.models.Client;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Session;
import be.helha.applicine.models.Ticket;
import be.helha.applicine.views.ClientAccountControllerView;
import be.helha.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class ClientAccountApplication extends Application implements ClientAccountControllerView.ClientAccountListener {

    private ClientsDAO clientsDAO = new ClientsDAOImpl();
    //renvoie le fichier FXML de la vue ClientAccount
    private final FXMLLoader fxmlLoader = new FXMLLoader(ClientAccountControllerView.getFXMLResource());

    //permet de communiquer avec le parentController (MasterApplication) pour changer de fenêtre et de contrôleur de vue.
    private final MasterApplication parentController = new MasterApplication();



    //permet de fermer la fenêtre du client account et de retourner à la fenêtre du client. Je parle au parentController (masterApplication) pour changer de fenêtre.
    @Override
    public void toClientSide() throws Exception {
        parentController.toClient();
    }

    @Override
    public void toClientAccount() throws Exception {
        parentController.toClientAccount();
    }

    @Override
    public Client getClientAccount() throws SQLException {
        try{
            return clientsDAO.getClient(1);
        }catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        ClientAccountControllerView.setStageOf(fxmlLoader);
        ClientAccountControllerView clientAccountControllerView = fxmlLoader.getController();
        //permet à la vue de communiquer avec le controller de l'application ClientAccount
        clientAccountControllerView.setListener(this);
        //définit la fenêtre courante dans le parentController comme étant la fenêtre gérée par ManagerViewController.
        parentController.setCurrentWindow(ClientAccountControllerView.getAccountWindow());
        //initialise la page du client account (affiche les tickets et les informations du client)
        clientAccountControllerView.initializeClientAccountPage();
    }

    public void addTickets(List<Ticket> tickets) {
        ClientAccountControllerView clientAccountControllerView = fxmlLoader.getController();
        for (Ticket ticket : tickets) {
            clientAccountControllerView.addTicket(ticket);
        }
    }

}
