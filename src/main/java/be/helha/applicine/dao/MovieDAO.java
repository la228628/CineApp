package be.helha.applicine.dao;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Viewable;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface represents the Data Access Object for the movies.
 */
public interface MovieDAO {
    List<Movie> getAllMovies() throws SQLException;
    Movie getMovieById(int id);
    void addMovie(Viewable movie);
    void updateMovie(Viewable movie);
    void removeMovie(int id) throws Exception;

    void removeAllMovies();

    void adaptAllImagePathInDataBase() throws SQLException;

    boolean isMovieTableEmpty();

    int sessionLinkedToMovie(int movieId);

    void deleteRattachedSessions(int id);

}
