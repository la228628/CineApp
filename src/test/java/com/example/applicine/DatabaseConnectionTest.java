//package com.example.applicine;
//import com.example.applicine.dao.MovieDAO;
//import com.example.applicine.dao.impl.MovieDAOImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import com.example.applicine.database.DatabaseConnection;
//import com.example.applicine.models.Movie;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//public class DatabaseConnectionTest {
//
//    private MovieDAO movieDAO;
//
//    @BeforeEach
//    public void setUp() {
//        movieDAO = new MovieDAOImpl();
//    }
//
//
//    @Test
//    public void testGetAllMovies() throws SQLException {
//        /*
//        // Pour Louis: En gros là tu crées deux films et tu compares si ils sont dans la liste de films récupérée de la base de données. Mais du coup ça retourne toujours false car les films que tu crées n'existent pas dans la base de données
//        Movie movie1 = new Movie("Test Title 1", "Test Genre", "Test Director", 120, "Test Synopsis", "Test ImagePath");
//        Movie movie2 = new Movie("Test Title 2", "Test Genre", "Test Director", 120, "Test Synopsis", "Test ImagePath");
//        ArrayList<Movie> movies = DatabaseConnection.getAllMovies();
//        boolean isPresent1 = movies.stream().anyMatch(m -> m.getTitle().equals(movie1.getTitle()));
//        boolean isPresent2 = movies.stream().anyMatch(m -> m.getTitle().equals(movie2.getTitle()));
//
//        System.out.println(movies.get(0).getTitle());
//        System.out.println(movies.get(1).getTitle());
//        assertTrue(isPresent1, "The first movie should be present in the database");
//        assertTrue(isPresent2, "The second movie should be present in the database");
//        */
//        // Pour Louis: Là je crée une ArrayList de films, je la remplis avec la fct getAllMovies et je vérifie que tous les attributs de chaque film ne sont pas null (si les films sont bien recup ça sera le cas)
//        List<Movie> moviesList = movieDAO.getAllMovies();
//        boolean movieListAttributesNotNull = moviesList.stream().allMatch(m -> m.getTitle() != null && m.getGenre() != null && m.getDirector() != null && m.getDuration() != 0 && m.getSynopsis() != null && m.getImagePath() != null);
//        assertTrue(movieListAttributesNotNull, "All movies attributes should not be null");
//    }
//
//
//    @Test
//    public void addAndDeleteMovie() throws Exception {
//
//        Movie movieBase = new Movie("TitreTest", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");
//        movieDAO.addMovie(movieBase);
//
//        List<Movie> movies = movieDAO.getAllMovies();
//        System.out.println(movies.size());
//
//        Movie movieSubjet = movieDAO.getMovieById(movies.size());
//        System.out.println(movieBase.getId());
//        boolean isPresent = movies.stream().anyMatch(m -> m.getTitle().equals(movieSubjet.getTitle()));
//        assertTrue(isPresent, "The movie should be present in the database");
//
//
//
//        movieDAO.removeMovie(movieSubjet.getId());
//        List<Movie> moviesArray = movieDAO.getAllMovies();
//        System.out.println(moviesArray.size());
//        System.out.println(movieSubjet.getTitle());
//        boolean isPresent1 = moviesArray.stream().anyMatch(m -> m.getTitle().equals(movieSubjet.getTitle()));
//        assertFalse(isPresent1, "The movie should not be present in the database");
//    }
//
//
//}
