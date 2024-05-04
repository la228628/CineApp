package be.helha.applicine.DAOTest;

import be.helha.applicine.server.dao.MovieDAO;
import be.helha.applicine.server.dao.impl.MovieDAOImpl;
import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Visionable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieDAOImplTest {

    private MovieDAO movieDAO;

    @BeforeEach
    public void setUp() {
        movieDAO = new MovieDAOImpl();
    }

    @Test
    public void testGetAllMovies() throws SQLException {
        List<Visionable> moviesList = movieDAO.getAllMovies();
        boolean movieListAttributesNotNull = moviesList.stream()
                .allMatch(m -> m.getTitle() != null && m.getGenre() != null && m.getDirector() != null && m.getTotalDuration() != 0 && m.getSynopsis() != null && m.getImagePath() != null);
        assertTrue(movieListAttributesNotNull, "Tous les attributs des films ne doivent pas être nuls");
    }

    @Test
    public void testAddAndDeleteMovie() throws Exception {
        Movie movieBase = new Movie("TitreTest", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");
        movieDAO.addMovie(movieBase);

        List<Visionable> movies = movieDAO.getAllMovies();
        Visionable movieSubject = movies.get(movies.size() - 1);
        boolean isPresent = movies.stream().anyMatch(m -> m.getTitle().equals(movieSubject.getTitle()));
        assertTrue(isPresent, "Le film devrait être présent dans la base de données");

        movieDAO.removeMovie(movieSubject.getId());
        List<Visionable> updatedMovies = movieDAO.getAllMovies();
        boolean isDeleted = updatedMovies.stream().noneMatch(m -> m.getTitle().equals(movieSubject.getTitle()));
        assertTrue(isDeleted, "Le film ne devrait plus etre présent dans la base de données");
    }
}