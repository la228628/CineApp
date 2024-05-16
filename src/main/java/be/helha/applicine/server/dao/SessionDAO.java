package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Viewable;

import java.sql.SQLException;
import java.util.List;

public interface SessionDAO {
    void addSession(int movieId, int roomId, String dateTime, String versionMovie) throws SQLException;

    void removeSession(int id) throws SQLException;
    void removeAllSessions() throws SQLException;

    void updateSession(Integer sessionId, Integer movieId, Integer roomId, String convertedDateTime, String version) throws SQLException;

    List<MovieSession> getSessionsForMovie(Viewable movie) throws SQLException;

    MovieSession getSessionById(int i) throws SQLException;
}
