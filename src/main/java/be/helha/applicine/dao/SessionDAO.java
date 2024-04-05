package be.helha.applicine.dao;

public interface SessionDAO {
    void addSession(int movieId, int roomId, String date, String time, String versionMovie);
    void removeSession(int id);
    void removeAllSessions();
}
