package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.exceptions.DaoException;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface represents the Data Access Object for the movies.
 */
public interface MovieDAO {
    List<Movie> getAll() throws DaoException;
    Movie get(int id) throws DaoException;
    void create(Viewable movie) throws DaoException;
    void update(Viewable movie) throws DaoException;
    void delete(int id) throws DaoException;

    boolean isMovieTableEmpty() throws DaoException;

    int getSessionLinkedToMovie(int movieId) throws DaoException;
}
