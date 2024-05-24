package be.helha.applicine.server.dao.impl;

import be.helha.applicine.common.models.exceptions.DaoException;
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

/**
 * This class is the implementation of the SessionDAO interface.
 */
public class SessionDAOImpl implements SessionDAO {
    private final Connection connection;

    /**
     * This constructor initializes the connection to the database.
     */
    public SessionDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * This constructor initializes the connection to the database.
     *
     * @param connection the connection to the database
     */
    public SessionDAOImpl(Connection connection) {
        this.connection = connection;
    }


    /**
     * This method creates a session in the database.
     *
     * @param session the session to create
     */
    @Override
    public void create(MovieSession session) throws DaoException {
        try {
            String sql = "INSERT INTO seances (viewableid, roomid, version,time ) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, session.getViewable().getId());
            pstmt.setInt(2, session.getRoom().getNumber());
            pstmt.setString(3, session.getVersion());
            pstmt.setString(4, convertStringToDateTime(session.getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la création de la séance");
        }
    }

    /**
     * This method removes a session from the database.
     *
     * @param id the id of the session to remove
     */

    @Override
    public void delete(int id) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM seances WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la suppression de la séance");
        }
    }

    /**
     * This method converts a string to a date time format that can be used in the database.
     *
     * @param dateTime the string to convert
     * @return the converted string
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
     * This method retrieves all the sessions from the database.
     *
     * @return a list of all the sessions
     */

    @Override
    public List<MovieSession> getAll() throws DaoException {
        List<MovieSession> movieSessions = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Viewable viewable = new ViewableDAOImpl().getViewableById(rs.getInt("viewableid"));
                Room room = new RoomDAOImpl().get(rs.getInt("roomid"));
                movieSessions.add(new MovieSession(rs.getInt("id"), viewable, rs.getString("time"), room, rs.getString("version")));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération des séances");
        }
        return movieSessions;
    }

    /**
     * This method updates a session in the database.
     *
     * @param session the session to update
     */
    @Override
    public void update(MovieSession session) throws DaoException {
        try {
            String sql = "UPDATE seances SET viewableid = ?, roomid = ?, time = ?, version = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, session.getViewable().getId());
            pstmt.setInt(2, session.getRoom().getNumber());
            pstmt.setString(3, convertStringToDateTime(session.getTime()));
            pstmt.setString(4, session.getVersion());
            pstmt.setInt(5, session.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DaoException("Erreur lors de la mise à jour de la séance");
        }
    }

    /**
     * This method retrieves all the sessions for a movie from the database.
     *
     * @param viewable the movie to get the sessions for
     * @return a list of all the sessions for the movie
     */
    @Override
    public List<MovieSession> getSessionsForMovie(Viewable viewable) throws DaoException {
        List<MovieSession> sessions = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances WHERE viewableid = ?")) {
            pstmt.setInt(1, viewable.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Room room = new RoomDAOImpl().get(rs.getInt("roomid"));
                sessions.add(new MovieSession(rs.getInt("id"), viewable, rs.getString("time"), room, rs.getString("version")));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération des séances");
        }
        return sessions;
    }

    /**
     * This method retrieves a session from the database.
     *
     * @param i the id of the session to retrieve
     * @return the session
     */
    @Override
    public MovieSession get(int i) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances WHERE id = ?")) {
            pstmt.setInt(1, i);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Viewable movie = new MovieDAOImpl().get(rs.getInt("viewableid"));
                Room room = new RoomDAOImpl().get(rs.getInt("roomid"));
                return new MovieSession(rs.getInt("id"), movie, rs.getString("time"), room, rs.getString("version"));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de la séance");
        }
        return null;
    }

    /**
     * This method checks if there is a time conflict between a new session and the existing sessions in a room.
     * @param sessionID the id of the session
     * @param roomId the id of the room
     * @param dateTime the date and time of the session
     * @param newSessionMovieDuration the duration of the movie
     * @return a list of all the sessions for the room
     * @throws DaoException if an error occurs
     */
    @Override
    public List<Integer> checkTimeConflict(int sessionID, int roomId, String dateTime, Integer newSessionMovieDuration) throws DaoException {
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
                if (!(sessionID == rs.getInt("id"))) { // On ne veut pas comparer la séance actuelle avec elle-même (cas de la modification). On ne peut pas avoir un conflit horaire avec la séance que l'on est en train de modifier
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
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération des séances pour vérifier les conflits horaires");
        }
        return sessionsWithConflict;
    }

    /**
     * This method deletes all the sessions from the database.
     */
    @Override
    public void deleteAll() {
        try {
            connection.createStatement().executeUpdate("DELETE FROM seances");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de toutes les séances");
        }
    }

    /**
     * This method retrieves the movie linked to a session.
     *
     * @param sessionId the id of the session
     * @return the movie linked to the session
     */
    public Viewable getMovieBySessionId(int sessionId) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM seances WHERE id = ?")) {
            pstmt.setInt(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                //return new MovieDAOImpl().getMovieById(rs.getInt("viewableid"));
                return new ViewableDAOImpl().getViewableById(rs.getInt("viewableid"));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération du film lié à la séance");
        }
        return null;
    }

}