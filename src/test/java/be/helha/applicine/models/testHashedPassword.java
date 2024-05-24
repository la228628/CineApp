package be.helha.applicine.models;

import be.helha.applicine.common.models.HashedPassword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testHashedPassword {

    @Test
    public void testPasswordHashing() throws Exception {
        String password = "password";
        String hashedPassword = HashedPassword.getHashedPassword(password);

        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
    public void testPasswordChecking() throws Exception {
        String password = "password";
        String hashedPassword = HashedPassword.getHashedPassword(password);

        assertTrue(HashedPassword.checkPassword(password, hashedPassword));
        assertFalse(HashedPassword.checkPassword("wrongpassword", hashedPassword));
    }
}
