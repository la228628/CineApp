package be.helha.applicine.models;

import be.helha.applicine.common.models.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

class TicketTest {
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket("student", null, null);
    }

    @Test
    public void testCreateTicketVerificationCode() {
        String ticketVerificationCode = ticket.getTicketVerificationCode();
        System.out.println("Ticket verification code: " + ticketVerificationCode);
        System.out.println("ticket price: " + ticket.getPrice());
        assertEquals(15, ticketVerificationCode.length());
    }
    /*
    @Test
    void testVerifyExpirationDate() {
        Ticket ticket = new Ticket("student");
        assertTrue(ticket.verifyExpirationDate());
    }
    */

}