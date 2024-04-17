package be.helha.applicine.dao;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Visionable;

import java.util.List;

/**
 * This interface represents the Data Access Object for the movies.
 */
public interface MovieDAO {
    List<Visionable> getAllMovies();
    Visionable getMovieById(int id);
    void addMovie(Visionable movie);
    void updateMovie(Visionable movie);
    void removeMovie(int id) throws Exception;

    void removeAllMovies();

    void adaptAllImagePathInDataBase();

    boolean isMovieTableEmpty();

    int sessionLinkedToMovie(int movieId);

    void deleteRattachedSessions(int id);

}
