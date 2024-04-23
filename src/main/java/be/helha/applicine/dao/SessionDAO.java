package be.helha.applicine.dao;

import be.helha.applicine.models.MovieSession;
import be.helha.applicine.models.Viewable;

import java.util.List;

public interface SessionDAO {
    void addSession(int movieId, int roomId, String dateTime, String versionMovie);

    void removeSession(int id);
    void removeAllSessions();

    void updateSession(Integer sessionId, Integer movieId, Integer roomId, String convertedDateTime, String version);

    List<MovieSession> getSessionsForMovie(Viewable movie);

    MovieSession getSessionById(int i);
}
