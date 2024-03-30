package com.example.applicine;
import com.example.applicine.dao.MovieDAO;
import com.example.applicine.dao.impl.MovieDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class DatabaseConnectionTest {
    private MovieDAO movieDAO;

    @BeforeEach
    public void setUp() {
        movieDAO = new MovieDAOImpl(DatabaseConnection.getConnection());
    }

    @Test
    public void testAddMovie() throws SQLException {
        Movie movie = new Movie("TitreTest", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");
        movieDAO.addMovie(movie);
    }

    @Test
    public void testGetAllMovies() throws SQLException {
        Movie movie1 = new Movie("Test Title 1", "Test Genre", "Test Director", 120, "Test Synopsis", "Test ImagePath");
        Movie movie2 = new Movie("Test Title 2", "Test Genre", "Test Director", 120, "Test Synopsis", "Test ImagePath");
        List<Movie> movies = movieDAO.getAllMovies();
        boolean isPresent1 = movies.stream().anyMatch(m -> m.getTitle().equals(movie1.getTitle()));
        boolean isPresent2 = movies.stream().anyMatch(m -> m.getTitle().equals(movie2.getTitle()));

        assertTrue(isPresent1, "The first movie should be present in the database");
        assertTrue(isPresent2, "The second movie should be present in the database");
    }

    @Test
    public void testRemoveMovie() throws SQLException {
        Movie movieToDelete = new Movie("LenaZie", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");

        // Supprimer le film ajouté
        movieDAO.removeMovie(movieToDelete.getId());

        List<Movie> moviesArray = movieDAO.getAllMovies();
        boolean isPresent1 = moviesArray.stream().anyMatch(m -> m.getTitle().equals(movieToDelete.getTitle()));
        assertFalse(isPresent1, "The movie should not be present in the database");
    }
}
