package database;

import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Room;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.server.dao.SessionDAO;
import be.helha.applicine.server.dao.impl.SessionDAOImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionDAOTest {
    private static SessionDAO sessionDAO;

    @BeforeEach
    public void setup() {
        Connection connection = DatabaseConnectionTest.getConnection();
        sessionDAO = new SessionDAOImpl(connection);
    }

    @AfterAll
    public static void tearDown() {
        sessionDAO.deleteAll();
    }

    @Test
    public void testCreateSession() throws SQLException, DaoException {
        Room room = new Room(1, 100);
        byte[] image = new byte[10];
        Movie newMovie = new Movie("Title", "Genre", "Director", 120, "Synopsis", image, "imagePath");
        MovieSession session = new MovieSession(1, newMovie, "2022-12-12 12:00", room, "2D");
        sessionDAO.create(session);
        MovieSession createdSession = sessionDAO.get(1);
        assertNotNull(createdSession);
        assertEquals(session.getId(), createdSession.getId());
    }

    @Test
    public void testGetSession() throws SQLException, DaoException {
        Room room = new Room(1, 100);
        byte[] image = new byte[10];
        Movie newMovie = new Movie("Title", "Genre", "Director", 120, "Synopsis", image, "imagePath");
        MovieSession session = new MovieSession(1, newMovie, "2022-12-12 12:00", room, "2D");
        sessionDAO.create(session);
        MovieSession sessionReceived = sessionDAO.get(1); // Assuming 1 is the id of the session
        assertNotNull(sessionReceived);
        assertEquals(1, sessionReceived.getId());
    }

    @Test
    public void testUpdateSession() throws SQLException, DaoException {
        Room room = new Room(1, 100);
        byte[] image = new byte[10];
        Movie newMovie = new Movie("Title", "Genre", "Director", 120, "Synopsis", image, "imagePath");
        MovieSession session = new MovieSession(1, newMovie, "2022-12-12 12:00", room, "2D");
        sessionDAO.create(session);
        session.setTime("2022-12-13 12:00");
        sessionDAO.update(session);
        MovieSession updatedSession = sessionDAO.get(1);
        assertEquals("2022-12-13 12:00", updatedSession.getTime());
    }

    @Test
    public void testDeleteSession() throws SQLException, DaoException {
        Room room = new Room(1, 100);
        byte[] image = new byte[10];
        Movie newMovie = new Movie("Title", "Genre", "Director", 120, "Synopsis", image, "imagePath");
        MovieSession session = new MovieSession(1, newMovie, "2022-12-12 12:00", room, "2D");
        sessionDAO.create(session);
        sessionDAO.delete(1);
        MovieSession deletedSession = sessionDAO.get(1);
        assertNull(deletedSession);
    }

    @Test
    public void testGetAllSessions() throws SQLException, DaoException {
        List<MovieSession> sessions = sessionDAO.getAll();
        assertNotNull(sessions);
        assertFalse(sessions.isEmpty());
    }

    @Test
    public void testGetSessionsForMovie() throws SQLException, DaoException {
        byte[] image = new byte[10];
        Movie newMovie = new Movie("Title", "Genre", "Director", 120, "Synopsis", image, "imagePath");
        List<MovieSession> sessions = sessionDAO.getSessionsForMovie(newMovie);
        assertNotNull(sessions);
        assertFalse(sessions.isEmpty());
    }
}

