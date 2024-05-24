package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Client;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ClientsDAO {
    Client create(Client client);
    void delete(int clientId);
    void update(Client client);
    Client get(int clientId) throws SQLException;
    ArrayList<Client> getAll();
    Client getClientByUsername(String username);
    Client getClientByEmail(String email);

    boolean isClientTableEmpty();

    void deleteAll();
}
