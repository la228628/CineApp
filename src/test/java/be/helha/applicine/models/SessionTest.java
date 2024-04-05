package be.helha.applicine.models;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {
    @Test
    public void testConstructor(){
        LocalDate date = LocalDate.now();
        System.out.println(date);
        date = date.plusMonths(1);
        System.out.println(date);
        Session session = new Session(date);
        System.out.println(session.getDate());
        assertNotNull(session.getDate());
        assertTrue(session.getDate().isAfter(LocalDate.now()));
    }
    @Test
    public void testBuyingTicket(){
        LocalDate date = LocalDate.now();
        Session session = new Session(date);
        session.buyingTicket();
        assertTrue(session.getSeatsLeft() > 0);
    }
}