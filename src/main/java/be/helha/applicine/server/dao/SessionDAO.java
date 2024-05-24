package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Viewable;

import java.sql.SQLException;
import java.util.List;

public interface SessionDAO {
    void create(MovieSession session) throws SQLException;

    void delete(int id) throws SQLException;

    void update(MovieSession session) throws SQLException;

    List<MovieSession> getSessionsForMovie(Viewable movie) throws SQLException;

    MovieSession get(int i) throws SQLException;

    List<MovieSession> getAll() throws SQLException;

    List<Integer> checkTimeConflict(int sessionID,int roomId, String dateTime, Integer newSessionMovieDuration) throws SQLException;
}
