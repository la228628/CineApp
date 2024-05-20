package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.Movie;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface represents the Data Access Object for the movies.
 */
public interface MovieDAO {
    List<Movie> getAll() throws SQLException;
    Movie get(int id);
    void create(Viewable movie);
    void update(Viewable movie);
    void delete(int id) throws Exception;

    boolean isMovieTableEmpty();

    int getSessionLinkedToMovie(int movieId);

    void deleteRattachedSessions(int id);

}
