package be.helha.applicine.models.exceptions;

import be.helha.applicine.common.models.exceptions.AdminIsAlreadyLoggedException;
import be.helha.applicine.common.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.common.models.exceptions.TimeConflictException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testTimeConflictException() {
        String message = "Time conflict";
        List<Integer> conflictingSessionsIds = Arrays.asList(1, 2, 3);
        TimeConflictException exception = new TimeConflictException(message, conflictingSessionsIds);
        assertEquals(message, exception.getMessage());
        assertEquals(conflictingSessionsIds, exception.getConflictingSessionsIds());
    }
}