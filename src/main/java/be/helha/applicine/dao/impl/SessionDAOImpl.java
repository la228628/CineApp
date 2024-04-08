package be.helha.applicine.dao.impl;

import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.dao.SessionDAO;

import java.sql.Connection;

public class SessionDAOImpl implements SessionDAO {
    private Connection connection;

    public SessionDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void addSession(int movieId, int roomId, String date, String time, String versionMovie) {
        try {
            connection.createStatement().executeUpdate("INSERT INTO Session (movieId, roomId, date, time, versionMovie) VALUES (" + movieId + ", " + roomId + ", '" + date + "', '" + time + "', " + versionMovie + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSession(int id) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM Session WHERE id = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeAllSessions() {
        try {
            connection.createStatement().executeUpdate("DELETE FROM Session");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}