package be.helha.applicine.dao;

import be.helha.applicine.models.Client;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ClientsDAO {
    void createClient(String name, String email, String username, String password);
    void deleteClient(int clientId);
    void updateClient(int clientId, String name, String email, String username, String password);
    Client getClient(int clientId) throws SQLException;
    ArrayList<Client> getAllClients();
    Client getClientByUsername(String username);
    Client getClientByEmail(String email);
}
