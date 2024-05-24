package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.exceptions.DaoException;

import java.util.ArrayList;

/**
 * Interface for the ViewableDAO, links the DAO to the database for viewables table.
 * @see be.helha.applicine.server.dao.impl.ViewableDAOImpl
 */
public interface ViewableDAO {
    void addViewable(String name, String type , ArrayList<Integer> movieIDs) throws DaoException;
    boolean removeViewable(Integer id) throws DaoException;

    void updateViewable(Integer id, String name, String type, ArrayList<Integer> movieIDs) throws DaoException;

    void addMovieToViewable(Integer viewableID, Integer movieID) throws DaoException;

    void removeViewableMovieCorrespondance(Integer viewableID) throws DaoException;
    void addViewableWithOneMovie(String title, String singleMovie, int id) throws DaoException;

    ArrayList<Movie> getMoviesFromViewable(Integer viewableID) throws DaoException;

    ArrayList<Viewable> getAllViewables() throws DaoException;

    Viewable getViewableById(int id) throws DaoException;

    ArrayList<Integer> getSeancesLinkedToViewable(int id) throws DaoException;

    int sagasLinkedToMovie(int movieId) throws DaoException;

    void removeViewableFromMovie(int movieId) throws DaoException;

    int getViewableIdByMovieId(int id) throws DaoException;
}
