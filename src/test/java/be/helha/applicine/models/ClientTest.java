package be.helha.applicine.models;

import be.helha.applicine.common.models.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    public void testClientCreation() {
        Client client = new Client("name", "email@example.com", "username", "password");
        assertEquals("name", client.getName());
        assertEquals("email@example.com", client.getEmail());
        assertEquals("username", client.getUsername());
        assertEquals("password", client.getPassword());
    }

    @Test
    public void testEmailValidation() {
        assertTrue(Client.isValidEmail("email@example.com"));
        assertFalse(Client.isValidEmail("djofjioqf ezd785"));
    }
}
