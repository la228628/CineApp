package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.exceptions.DaoException;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for the SessionDAO, links the DAO to the database for sessions table.
 * @see be.helha.applicine.server.dao.impl.SessionDAOImpl
 */
public interface SessionDAO {
    void create(MovieSession session) throws DaoException;

    void delete(int id) throws DaoException;

    void update(MovieSession session) throws DaoException;

    List<MovieSession> getSessionsForMovie(Viewable movie) throws DaoException;

    MovieSession get(int i) throws DaoException;

    List<MovieSession> getAll() throws DaoException;

    List<Integer> checkTimeConflict(int sessionID,int roomId, String dateTime, Integer newSessionMovieDuration) throws DaoException;

    void deleteAll();
}
