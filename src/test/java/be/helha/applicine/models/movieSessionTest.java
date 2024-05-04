package be.helha.applicine.models;

import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class movieSessionTest {

    private MovieSession movieSession;
    private Movie movie;
    private Room room;
    @BeforeEach
    public void setUp() throws SQLException {
        LocalDate date = LocalDate.now();
        movie = new Movie("Test", "Test", "Test", 150, "Test", "C:\\Users\\vdzlu\\AppData\\Roaming\\Applicine\\images\\uVAk2YliqInQfH4B4vzZ75rwcNB.jpg");
        room = new Room(1, 150);
        movieSession = new MovieSession(10, movie, date.toString(), room,"2D");
    }

    @Test
    public void testConstructor() throws SQLException {
        assertNotNull(movieSession.getDate());
        assertFalse(movieSession.getDate().isAfter(LocalDate.now()));
    }
    @Test
    public void testMovieInSession() throws SQLException {
        System.out.println(movieSession.getMovie());
        assertEquals(movie, movieSession.getMovie());
    }
}