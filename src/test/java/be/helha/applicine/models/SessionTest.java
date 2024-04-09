package be.helha.applicine.models;

import org.junit.Test;

import java.sql.SQLException;
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
    public void testMovieInSession() throws SQLException {
        LocalDate date = LocalDate.now();
        Movie movie = new Movie("Test", "Test", "Test", 150, "Test", "C:\\Users\\vdzlu\\AppData\\Roaming\\Applicine\\images\\uVAk2YliqInQfH4B4vzZ75rwcNB.jpg");
        Session session = new Session(movie, date, "Test");
        System.out.println(session.getMovie());
        assertEquals(movie, session.getMovie());
    }
    @Test
    public void testBuyingTicket(){
        LocalDate date = LocalDate.now();
        Session session = new Session(date);
        session.buyingTicket();
        assertTrue(session.getSeatsLeft() > 0);
    }
}