package be.helha.applicine.dao;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Visionable;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface represents the Data Access Object for the movies.
 */
public interface MovieDAO {
    List<Visionable> getAllMovies() throws SQLException;
    Visionable getMovieById(int id);
    void addMovie(Visionable movie) throws SQLException;
    void updateMovie(Visionable movie) throws SQLException;
    void removeMovie(int id) throws Exception;

    void removeAllMovies();

    void adaptAllImagePathInDataBase() throws SQLException;

    boolean isMovieTableEmpty();

    int sessionLinkedToMovie(int movieId);

    void deleteRattachedSessions(int id);

}
