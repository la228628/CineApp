package be.helha.applicine.dao.impl;

import be.helha.applicine.dao.MovieDAO;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Viewable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieDAOImplTest {
    private MovieDAO movieDAO;

    @BeforeEach
    public void setUp() {
        movieDAO = new MovieDAOImpl();
    }

    @Test
    public void testGetAllMovies() throws SQLException {
        List<Viewable> moviesList = movieDAO.getAllMovies();
        boolean movieListAttributesNotNull = moviesList.stream()
                .allMatch(m -> m.getTitle() != null && m.getGenre() != null && m.getDirector() != null && m.getTotalDuration() != 0 && m.getSynopsis() != null && m.getImagePath() != null);
        assertTrue(movieListAttributesNotNull, "Tous les attributs des films ne doivent pas être nuls");
    }

    @Test
    public void testAddAndDeleteMovie() throws Exception {
        Movie movieBase = new Movie("TitreTest", "GenreTest", "RéalisateurTest", 120, "SynopsisTest", "CheminTest");
        movieDAO.addMovie(movieBase);

        List<Viewable> movies = movieDAO.getAllMovies();
        Viewable movieSubject = movies.get(movies.size() - 1);
        boolean isPresent = movies.stream().anyMatch(m -> m.getTitle().equals(movieSubject.getTitle()));
        assertTrue(isPresent, "Le film devrait être présent dans la base de données");

        movieDAO.removeMovie(movieSubject.getId());
        List<Viewable> updatedMovies = movieDAO.getAllMovies();
        boolean isDeleted = updatedMovies.stream().noneMatch(m -> m.getTitle().equals(movieSubject.getTitle()));
        assertTrue(isDeleted, "Le film ne devrait plus etre présent dans la base de données");
    }

}