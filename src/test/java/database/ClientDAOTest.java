package database;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.server.dao.impl.ClientsDAOImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientDAOTest {
    private static ClientsDAO clientsDAO;

    @BeforeEach
    public void setup() {
        Connection connection = DatabaseConnectionTest.getConnection();
        clientsDAO = new ClientsDAOImpl(connection);
    }

    @AfterAll
    public static void tearDown() {
        clientsDAO.deleteAll();
    }

    @Test
    public void testCreateClient() {
        try {
            Client client = new Client("Test Name1", "test2@email.com", "testUsername2", "testPassword");
            Client createdClient = clientsDAO.create(client);
            assertNotNull(createdClient);
            assertEquals(client.getName(), createdClient.getName());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetClient() throws SQLException {
        try {
            Client client = new Client("Test Name", "test@email.com","testUsername", "testPassword");
            clientsDAO.create(client);
            Client clientReceived = clientsDAO.get(1); // Assuming 1 is the id of the client
            assertNotNull(clientReceived);
            assertEquals("Test Name", clientReceived.getName());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

}


