package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerAppTest {

        @Test
        void testValidateFieldsWithValidInputs() {
            SessionManagerApp manager = new SessionManagerApp();
            Integer movieId = 1;
            Integer roomId = 1;
            String date = "2021-06-01";
            String time = "12:00";
            try {
                manager.validateFields(movieId, roomId, date, time);
                // Si aucune exception n'est levée, le test réussit
            } catch (InvalideFieldsExceptions e) {
                fail("Expected no exception, but caught InvalideFieldsExceptions: " + e.getMessage());
            }

        }

        @Test
        void testValidateFieldsWithEmptyInputs() {
            SessionManagerApp manager = new SessionManagerApp();
            try {
                manager.validateFields(null,null, "", "");
                fail("Expected InvalideFieldsExceptions, but no exception was thrown");
            } catch (NullPointerException | InvalideFieldsExceptions e) {
                // Si une InvalideFieldsExceptions est levée, le test réussit
            }
        }

        @Test
        void testValidateFieldsWithInvalidDuration() {
            SessionManagerApp manager = new SessionManagerApp();
            try {
                manager.validateFields(1, 1, "2021-06-01", "abc");
                fail("Expected InvalideFieldsExceptions, but no exception was thrown");
            } catch (InvalideFieldsExceptions e) {
                // Si une InvalideFieldsExceptions est levée, le test réussit
            }
        }


}