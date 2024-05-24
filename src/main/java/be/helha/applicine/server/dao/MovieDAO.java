package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.exceptions.DaoException;

import java.util.List;

/**
 * Interface for the MovieDAO, links the DAO to the database for movies table.
 * @see be.helha.applicine.server.dao.impl.MovieDAOImpl
 */
public interface MovieDAO {
    List<Movie> getAll() throws DaoException;
    Movie get(int id) throws DaoException;
    void create(Viewable movie) throws DaoException;
    void update(Viewable movie) throws DaoException;
    void delete(int id) throws DaoException;

    boolean isMovieTableEmpty() throws DaoException;

    int getSessionLinkedToMovie(int movieId) throws DaoException;

    void deleteAll();
}
