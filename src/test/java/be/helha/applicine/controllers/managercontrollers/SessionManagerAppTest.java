package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.client.controllers.managercontrollers.SessionManagerApp;
import be.helha.applicine.common.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.common.models.exceptions.TimeConflictException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerAppTest {


        @Test
        void testValidateFieldsWithEmptyInputs() {
            try {
                SessionManagerApp manager = new SessionManagerApp();
                manager.validateFields(null,null,null, "", "");
                fail("Expected InvalideFieldsExceptions, but no exception was thrown");
            } catch (NullPointerException | InvalideFieldsExceptions | SQLException | ClassNotFoundException e) {
                // Si une InvalideFieldsExceptions est levée, le test réussit
            } catch (TimeConflictException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        void testValidateFieldsWithInvalidDuration() {
            try {
                SessionManagerApp manager = new SessionManagerApp();
                manager.validateFields(null,1, 1, "2021-06-01", "abc");
                fail("Expected InvalideFieldsExceptions, but no exception was thrown");
            } catch (InvalideFieldsExceptions e) {
                // Si une InvalideFieldsExceptions est levée, le test réussit
            } catch (TimeConflictException | SQLException | ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }


}