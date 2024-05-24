package be.helha.applicine.models;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testSession {
    @Test
    public void testSession() {
        Session session = new Session();
        assertFalse(session.isLogged());

        session.setLogged(true);
        assertTrue(session.isLogged());

        Client client = new Client("name", "email@example.com", "username", "password");
        session.setCurrentClient(client);
        assertEquals(client, session.getCurrentClient());
    }
}
