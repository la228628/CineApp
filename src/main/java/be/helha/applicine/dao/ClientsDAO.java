package be.helha.applicine.dao;

import be.helha.applicine.models.Client;

public interface ClientsDAO {
    void createClient(String name, String email, String username, String password);
    void deleteClient(int clientId);
    void updateClient(int clientId, String name, String email, String username, String password);
    Client getClient(int clientId);
    void getAllClients();
    void getClientByUsername(String username);
    void getClientByEmail(String email);
}
