package be.helha.applicine.dao.impl;

import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.common.models.Client;
import be.helha.applicine.server.dao.impl.ClientsDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientsDAOImplTest {

    private ClientsDAO clientDB;

    @BeforeEach
    void setUp() {
        clientDB = new ClientsDAOImpl();
    }

    @Test
    void createClient() {
        String name = "test";
        String email = "test";
        String username = "test";
        String password = "testPasswd";

        clientDB.create(new Client(name, email, username, password));

        Client createdClient = clientDB.getClientByUsername(username);
        assertNotNull(createdClient);
        assertEquals(name, createdClient.getName());
        assertEquals(email, createdClient.getEmail());
        assertEquals(username, createdClient.getUsername());
        assertEquals(password, createdClient.getPassword());
    }

    @Test
    void deleteClient() {
        String name = "test2";
        String email = "test2";
        String username = "test2";
        String password = "testPasswd2";

        clientDB.create(new Client(name, email, username, password));
        clientDB.delete(clientDB.getClientByUsername(username).getId());
        assertNull(clientDB.getClientByUsername(username));
    }

    @Test
    void updateClient() {
        String name = "test3";
        String email = "test3";
        String username = "test3";
        String password = "testPasswd3";
        String newName = "test4";
        String newEmail = "test4";
        String newUsername = "test4";
        String newPassword = "testPasswd4";

        clientDB.create(new Client(name, email, username, password));
        Client createdClient = clientDB.getClientByUsername(username);
        clientDB.update(new Client(createdClient.getId(), newName, newEmail, newUsername, newPassword));
        Client updatedClient = clientDB.getClientByUsername(newUsername);
        assertNotNull(updatedClient);
        assertEquals(newName, updatedClient.getName());
        assertEquals(newEmail, updatedClient.getEmail());
        assertEquals(newUsername, updatedClient.getUsername());
        assertEquals(newPassword, updatedClient.getPassword());
    }

    @Test
    void getClient() throws SQLException {
        String name = "test5";
        String email = "test5";
        String username = "test5";
        String password = "testPasswd5";

        clientDB.create(new Client(name, email, username, password));
        Client fetchedClient = clientDB.get(clientDB.getClientByUsername(username).getId());
        assertNotNull(fetchedClient);
    }

    @Test
    void getAllClients() {
        ArrayList<Client> clients = clientDB.getAll();
        assertNotNull(clients);
        assertTrue(clients.isEmpty(), "The clients list should be empty");
    }

    @Test
    void getClientByUsername() {
        String name = "test5";
        String email = "test5";
        String username = "test5";
        String password = "testPasswd5";

        clientDB.delete(clientDB.getClientByUsername(username).getId());

        clientDB.create(new Client(name, email, username, password));
        Client createdClient = clientDB.getClientByUsername(username);
        assertNotNull(createdClient);
        assertEquals(name, createdClient.getName());
        assertEquals(email, createdClient.getEmail());
        assertEquals(username, createdClient.getUsername());
        assertEquals(password, createdClient.getPassword());

        clientDB.delete(createdClient.getId());
    }

    @Test
    void getClientByEmail() {
        String name = "test6";
        String email = "test6";
        String username = "test6";
        String password = "testPasswd6";

        clientDB.create(new Client(name, email, username, password));
        Client createdClient = clientDB.getClientByEmail(email);
        assertNotNull(createdClient);
        assertEquals(name, createdClient.getName());
        assertEquals(email, createdClient.getEmail());
        assertEquals(username, createdClient.getUsername());
        assertEquals(password, createdClient.getPassword());
    }
}