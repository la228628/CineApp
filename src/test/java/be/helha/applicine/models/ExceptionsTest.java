package be.helha.applicine.models;

import be.helha.applicine.common.models.exceptions.AdminIsAlreadyLoggedException;
import be.helha.applicine.common.models.exceptions.InvalideFieldsExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionsTest {

    @Test
    public void testAdminIsAlreadyLoggedException() {
        String message = "Admin is already logged in";
        AdminIsAlreadyLoggedException exception = new AdminIsAlreadyLoggedException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testInvalideFieldsExceptions() {
        String message = "Invalid fields";
        InvalideFieldsExceptions exception = new InvalideFieldsExceptions(message);
        assertEquals(message, exception.getMessage());
    }

}