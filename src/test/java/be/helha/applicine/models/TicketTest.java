package be.helha.applicine.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testCreateTicketVerificationCode() {
        Ticket ticket = new Ticket("student");
        String ticketVerificationCode = ticket.getTicketVerificationCode();
        System.out.println("Ticket verification code: " + ticketVerificationCode);
        System.out.println("ticket price: " + ticket.getPrice());
        assertEquals(15, ticketVerificationCode.length());
    }
    @Test
    void testVerifyExpirationDate() {
        Ticket ticket = new Ticket("student");
        assertTrue(ticket.verifyExpirationDate());
    }

}