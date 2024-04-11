package be.helha.applicine.dao.impl;

import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.dao.SessionDAO;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.MovieSession;
import be.helha.applicine.models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SessionDAOImpl implements SessionDAO {
    private Connection connection;

    public SessionDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * This method adds a session to the database.
     *
     * @param movieId
     * @param roomId
     * @param dateTime
     * @param versionMovie
     */
    @Override
    public void addSession(int movieId, int roomId, String dateTime, String versionMovie) {
        try {
            System.out.println(convertStringToDateTime(dateTime));
            String dateTimeConverted = convertStringToDateTime(dateTime);
            connection.createStatement().executeUpdate("INSERT INTO seances (movieid, roomid, version,time ) VALUES (" + movieId + ", " + roomId + ", '" + versionMovie + "', '" + dateTimeConverted + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method removes a session from the database.
     *
     * @param id
     */

    @Override
    public void removeSession(int id) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM seances WHERE id = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method removes all the sessions from the database.
     */

    @Override
    public void removeAllSessions() {
        try {
            connection.createStatement().executeUpdate("DELETE FROM seances");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method converts a string to a date time format that can be used in the database.
     *
     * @param dateTime
     * @return
     */

    private String convertStringToDateTime(String dateTime) {
        String date = dateTime.split(" ")[0];
        System.out.println(date);
        String time = dateTime.split(" ")[1];
        String[] timeParts = time.split(":");
        System.out.println(date + " " + timeParts[0] + ":" + timeParts[1] + ":00");
        return date + " " + timeParts[0] + ":" + timeParts[1] + ":00";
    }

    /**
     * returns a list with all the sessions
     *
     * @return
     */

    public List<MovieSession> getAllSessions() {
        List<MovieSession> MovieSessions = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Movie movie = new MovieDAOImpl().getMovieById(rs.getInt("movieid"));
                Room room = new RoomDAOImpl().getRoomById(rs.getInt("roomid"));
                MovieSessions.add(new MovieSession(rs.getInt("id"), movie, rs.getString("time"), room, rs.getString("version")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MovieSessions;
    }

    /**
     * update a session from the given parameters
     *
     * @param sessionId
     * @param movieId
     * @param roomId
     * @param convertedDateTime
     * @param version
     */
    @Override
    public void updateSession(Integer sessionId, Integer movieId, Integer roomId, String convertedDateTime, String version) {
        try {
            connection.createStatement().executeUpdate("UPDATE seances SET movieid = " + movieId + ", roomid = " + roomId + ", time = '" + convertStringToDateTime(convertedDateTime) + "', version = '" + version + "' WHERE id = " + sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MovieSession> getSessionsForMovie(Movie movie) {
        List<MovieSession> sessions = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances WHERE movieid = ?")) {
            pstmt.setInt(1, movie.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Room room = new RoomDAOImpl().getRoomById(rs.getInt("roomid"));
                sessions.add(new MovieSession(rs.getInt("id"), movie, rs.getString("time"), room, rs.getString("version")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessions;
    }

    @Override
    public MovieSession getSessionById(int i) {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances WHERE id = ?")) {
            pstmt.setInt(1, i);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Movie movie = new MovieDAOImpl().getMovieById(rs.getInt("movieid"));
                Room room = new RoomDAOImpl().getRoomById(rs.getInt("roomid"));
                return new MovieSession(rs.getInt("id"), movie, rs.getString("time"), room, rs.getString("version"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
