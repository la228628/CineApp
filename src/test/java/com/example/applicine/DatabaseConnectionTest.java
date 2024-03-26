package com.example.applicine;
import org.junit.jupiter.api.Test;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;

import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
public class DatabaseConnectionTest {
    @Test
    public void testAddMovie() throws SQLException {
        Movie movie = new Movie("TitreTest", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");
        DatabaseConnection.AddMovie(movie);
    }

    @Test
    public void testGetAllMovies() throws SQLException {
        Movie movie1 = new Movie("Test Title 1", "Test Genre", "Test Director", 120, "Test Synopsis", "Test ImagePath");
        Movie movie2 = new Movie("Test Title 2", "Test Genre", "Test Director", 120, "Test Synopsis", "Test ImagePath");

        ArrayList<Movie> movies = DatabaseConnection.getAllMovies();
        boolean isPresent1 = movies.stream().anyMatch(m -> m.getTitle().equals(movie1.getTitle()));
        boolean isPresent2 = movies.stream().anyMatch(m -> m.getTitle().equals(movie2.getTitle()));

        assertTrue(isPresent1, "The first movie should be present in the database");
        assertTrue(isPresent2, "The second movie should be present in the database");
    }

    @Test
    public void testRemoveMovie() throws SQLException {
        Movie movieToDelete = new Movie("LenaZie", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");

        DatabaseConnection.removeMovies(movieToDelete.getID());

        ArrayList<Movie> moviesArray = DatabaseConnection.getAllMovies();
        boolean isPresent1 = moviesArray.stream().anyMatch(m -> m.getTitle().equals(movieToDelete.getTitle()));
        assertFalse(isPresent1, "The movie should not be present in the database");
    }
}
