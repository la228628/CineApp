package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Client;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ClientsDAO {
    Client createClient(String name, String email, String username, String password);
    void deleteClient(int clientId);
    void updateClient(int clientId, String name, String email, String username, String password);
    Client getClient(int clientId) throws SQLException;
    ArrayList<Client> getAllClients();
    Client getClientByUsername(String username);
    Client getClientByEmail(String email);

    boolean isClientTableEmpty();
}
