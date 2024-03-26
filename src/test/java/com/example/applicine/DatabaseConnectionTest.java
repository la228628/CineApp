package com.example.applicine;
import org.junit.jupiter.api.Test;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
public class DatabaseConnectionTest {
    @Test
    public void testAddMovie() {
        // Créer un objet Movie fictif pour tester
        Movie movie = new Movie("TitreTest", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");

        // Appeler la méthode AddMovie
        int id = DatabaseConnection.AddMovie(movie);

        // Vérifier si l'ID retourné est valide (supérieur à 0)
        assertTrue(id > 0);
    }

    @Test
    public void testGetAllMovies() {
        Movie movie1 = new Movie("Test Title 1254", "Test Genre", "Test Director", 120, "Test Synopsis", "Test ImagePath");
        Movie movie2 = new Movie("Test Title 1121577", "Test Genre", "Test Director", 120, "Test Synopsis", "Test ImagePath");

        ArrayList<Movie> movies = DatabaseConnection.getAllMovies();
        boolean isPresent1 = movies.stream().anyMatch(m -> m.getTitle().equals(movie1.getTitle()));
        boolean isPresent2 = movies.stream().anyMatch(m -> m.getTitle().equals(movie2.getTitle()));

        assertTrue(isPresent1, "The first movie should be present in the database");
        assertTrue(isPresent2, "The second movie should be present in the database");
    }

    @Test
    public void testRemoveMovie() {
        Movie movieToDelete = new Movie("LenaZie", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");

        // Supprimer le film ajouté
        DatabaseConnection.removeMovies(movieToDelete.getID());

        ArrayList<Movie> moviesArray = DatabaseConnection.getAllMovies();
        boolean isPresent1 = moviesArray.stream().anyMatch(m -> m.getTitle().equals(movieToDelete.getTitle()));
        assertFalse(isPresent1, "The movie should not be present in the database");
    }
}
