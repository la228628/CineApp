package be.helha.applicine.controllers;

import be.helha.applicine.dao.impl.ClientsDAOImpl;
import be.helha.applicine.models.Client;
import be.helha.applicine.models.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ClientAccountApplicationTest {
    MasterApplication parentController;
    ClientsDAOImpl clientsDAO;
    @BeforeEach
    void setUp() {
        parentController = new MasterApplication();
        clientsDAO = new ClientsDAOImpl();
    }
    @Test
    public void getClientAccount() {
        try {
            Session session = parentController.getSession();
            Client currentClient = session.getCurrentClient();
            clientsDAO.getClient(currentClient.getId());
        } catch(SQLException e) {
            System.out.println("Erreur lors de la récupération du client à partir de l'id de la db");
            popUpAlert("Erreur lors de la récupération du client");
            assert (false);
        } catch(NullPointerException e) {
            System.out.println("Problème de null dans la session ou client courant");
            popUpAlert("Erreur lors de la récupération du client");
            assert (false);
        }
    }
    public void popUpAlert(String message) {
        parentController.popUpAlert(message);
    }


}