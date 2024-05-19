package be.helha.applicine.server.dao.impl;

import be.helha.applicine.server.database.DatabaseConnection;
import be.helha.applicine.server.dao.SessionDAO;
import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Room;
import be.helha.applicine.common.models.Viewable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     * @param viewableId
     * @param roomId
     * @param dateTime
     * @param versionMovie
     * @param roomId The id of the room linked to the session
     * @param dateTime The date and time of the session
     * @param versionMovie The version of the movie linked to the session (2D, 3D)
     */
    @Override
    public void addSession(int viewableId, int roomId, String dateTime, String versionMovie) {
        try {
            System.out.println(convertStringToDateTime(dateTime));
            String dateTimeConverted = convertStringToDateTime(dateTime);
            connection.createStatement().executeUpdate("INSERT INTO seances (viewableid, roomid, version,time ) VALUES (" + viewableId + ", " + roomId + ", '" + versionMovie + "', '" + dateTimeConverted + "')");
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
    public void removeSession(int id) throws SQLException {
        connection.createStatement().executeUpdate("DELETE FROM seances WHERE id = " + id);
    }

    /**
     * This method removes all the sessions from the database.
     */

    @Override
    public void removeAllSessions() throws SQLException {
        connection.createStatement().executeUpdate("DELETE FROM seances");
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

    @Override
    public List<MovieSession> getAllSessions() throws SQLException{
        List<MovieSession> movieSessions = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Viewable viewable = new ViewableDAOImpl().getViewableById(rs.getInt("viewableid"));
                Room room = new RoomDAOImpl().getRoomById(rs.getInt("roomid"));
                movieSessions.add(new MovieSession(rs.getInt("id"), viewable, rs.getString("time"), room, rs.getString("version")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieSessions;
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
            connection.createStatement().executeUpdate("UPDATE seances SET viewableid = " + movieId + ", roomid = " + roomId + ", time = '" + convertStringToDateTime(convertedDateTime) + "', version = '" + version + "' WHERE id = " + sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MovieSession> getSessionsForMovie(Viewable viewable) {
        List<MovieSession> sessions = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances WHERE viewableid = ?")) {
            pstmt.setInt(1, viewable.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Room room = new RoomDAOImpl().getRoomById(rs.getInt("roomid"));
                sessions.add(new MovieSession(rs.getInt("id"), viewable, rs.getString("time"), room, rs.getString("version")));
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
                Viewable movie = new MovieDAOImpl().getMovieById(rs.getInt("viewableid"));
                Room room = new RoomDAOImpl().getRoomById(rs.getInt("roomid"));
                return new MovieSession(rs.getInt("id"), movie, rs.getString("time"), room, rs.getString("version"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Integer> checkTimeConflict(int sessionID,int roomId, String dateTime, Integer newSessionMovieDuration) throws SQLException {
        List<Integer> sessionsWithConflict = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances WHERE roomid = ?")) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Les dateTime que l'on utilise n'ont pas de secondes donc on les enlève
                LocalDateTime newSessionBeginTime = LocalDateTime.parse(dateTime, formatter);
                LocalDateTime newSessionEndTime = newSessionBeginTime.plusMinutes(newSessionMovieDuration);
                String currentCheckBeginTimeWithoutSeconds = rs.getString("time").substring(0, rs.getString("time").length() - 3);
                LocalDateTime currentBeginCheckTime = LocalDateTime.parse(currentCheckBeginTimeWithoutSeconds, formatter);
                Viewable movieLinkedToCheckSession = getMovieBySessionId(rs.getInt("id"));
                int currentSessionMovieDuration = movieLinkedToCheckSession.getTotalDuration();
                LocalDateTime currentEndCheckTime = currentBeginCheckTime.plusMinutes(currentSessionMovieDuration);
                if(!(sessionID == rs.getInt("id"))) { // On ne veut pas comparer la séance actuelle avec elle-même (cas de la modification). On ne peut pas avoir un conflit horaire avec la séance que l'on est en train de modifier
                    if (newSessionBeginTime.isAfter(currentBeginCheckTime) && newSessionBeginTime.isBefore(currentEndCheckTime) || //Cas 1: La séance commence pendant une autre séance
                            newSessionEndTime.isAfter(currentBeginCheckTime) && newSessionEndTime.isBefore(currentEndCheckTime) || //Cas 2: La séance se termine pendant une autre séance
                            newSessionBeginTime.isBefore(currentBeginCheckTime) && newSessionEndTime.isAfter(currentEndCheckTime) || //Cas 3: La séance commence avant une autre séance et se termine après
                            newSessionBeginTime.isEqual(currentBeginCheckTime) || newSessionEndTime.isEqual(currentEndCheckTime) || //Cas 4: La séance commence en même temps qu'une autre séance ou se termine en même temps
                            newSessionBeginTime.isEqual(currentEndCheckTime) || currentBeginCheckTime.isEqual(newSessionEndTime)) //Cas 5: La séance commence quand une autre se termine ( il faut quand même un peu de temps pour préparer la salle)
                    {
                        sessionsWithConflict.add(rs.getInt("id"));
                        System.out.println("Conflit horaire");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionsWithConflict;
    }


    public Viewable getMovieBySessionId(int sessionId) {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances WHERE id = ?")) {
            pstmt.setInt(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                //return new MovieDAOImpl().getMovieById(rs.getInt("viewableid"));
                return new ViewableDAOImpl().getViewableById(rs.getInt("viewableid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}