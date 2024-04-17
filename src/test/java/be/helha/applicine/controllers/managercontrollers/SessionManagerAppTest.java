package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.models.exceptions.TimeConflictException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerAppTest {


        @Test
        void testValidateFieldsWithEmptyInputs() {
            SessionManagerApp manager = new SessionManagerApp();
            try {
                manager.validateFields(null,null,null, "", "");
                fail("Expected InvalideFieldsExceptions, but no exception was thrown");
            } catch (NullPointerException | InvalideFieldsExceptions e) {
                // Si une InvalideFieldsExceptions est levée, le test réussit
            } catch (TimeConflictException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        void testValidateFieldsWithInvalidDuration() {
            SessionManagerApp manager = new SessionManagerApp();
            try {
                manager.validateFields(null,1, 1, "2021-06-01", "abc");
                fail("Expected InvalideFieldsExceptions, but no exception was thrown");
            } catch (InvalideFieldsExceptions e) {
                // Si une InvalideFieldsExceptions est levée, le test réussit
            } catch (TimeConflictException e) {
                throw new RuntimeException(e);
            }
        }


}