package be.helha.applicine.controllers;

import be.helha.applicine.controllers.ManagerApplication;
import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import org.junit.Test;
import static org.junit.Assert.*;

public class ManagerApplicationTest {

    @Test
    public void testValidateFieldsWithValidInputs() {
        ManagerApplication manager = new ManagerApplication();
        String title = "Test Movie";
        String genre = "Action";
        String director = "John Doe";
        String duration = "120";
        String synopsis = "Test synopsis";
        String imagePath = "path/to/image.png";

        try {
            manager.validateFields(title, genre, director, duration, synopsis, imagePath);
            // Si aucune exception n'est levée, le test réussit
        } catch (InvalideFieldsExceptions e) {
            fail("Expected no exception, but caught InvalideFieldsExceptions: " + e.getMessage());
        }
    }

    @Test(expected = InvalideFieldsExceptions.class)
    public void testValidateFieldsWithEmptyInputs() throws InvalideFieldsExceptions {
        ManagerApplication manager = new ManagerApplication();
        manager.validateFields("", "", "", "", "", "");
        // Si une InvalideFieldsExceptions est levée, le test réussit
    }

    @Test(expected = InvalideFieldsExceptions.class)
    public void testValidateFieldsWithInvalidDuration() throws InvalideFieldsExceptions {
        ManagerApplication manager = new ManagerApplication();
        manager.validateFields("Test Movie", "Action", "John Doe", "abc", "Test synopsis", "path/to/image.png");
    }



    @Test
    public void testCreateValidPath() {
        ManagerApplication manager = new ManagerApplication();
        String fileName = "image.png";
        String validPath = manager.createValidPath(fileName);
        assertTrue(validPath.startsWith("file:src"));
    }


}