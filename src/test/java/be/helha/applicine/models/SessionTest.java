package be.helha.applicine.models;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    private Session session;
    private Movie movie;
    private Room room;
    @BeforeEach
    public void setUp() throws SQLException {
        LocalDate date = LocalDate.now();
        movie = new Movie("Test", "Test", "Test", 150, "Test", "C:\\Users\\vdzlu\\AppData\\Roaming\\Applicine\\images\\uVAk2YliqInQfH4B4vzZ75rwcNB.jpg");
        room = new Room(1, 150);
        session = new Session(10, movie, date.toString(), room,"2D");
    }

    @Test
    public void testConstructor() throws SQLException {
        assertNotNull(session.getDate());
        assertTrue(session.getDate().isAfter(LocalDate.now()));
    }
    @Test
    public void testMovieInSession() throws SQLException {
        LocalDate date = LocalDate.now();
        System.out.println(session.getMovie());
        assertEquals(movie, session.getMovie());
    }
}