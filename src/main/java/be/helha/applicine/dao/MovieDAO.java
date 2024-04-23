package be.helha.applicine.dao;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Viewable;

import java.util.List;

/**
 * This interface represents the Data Access Object for the movies.
 */
public interface MovieDAO {
    List<Movie> getAllMovies();
    Movie getMovieById(int id);
    void addMovie(Viewable movie);
    void updateMovie(Viewable movie);
    void removeMovie(int id) throws Exception;

    void removeAllMovies();

    void adaptAllImagePathInDataBase();

    boolean isMovieTableEmpty();

    int sessionLinkedToMovie(int movieId);

    void deleteRattachedSessions(int id);

}
