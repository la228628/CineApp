package be.helha.applicine.dao;

public interface SessionDAO {
    void addSession(int movieId, int roomId, String dateTime, String versionMovie);

    void removeSession(int id);
    void removeAllSessions();

    void updateSession(Integer sessionId, Integer movieId, Integer roomId, String convertedDateTime, String version);
}
