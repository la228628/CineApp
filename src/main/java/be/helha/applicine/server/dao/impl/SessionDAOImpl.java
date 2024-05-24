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

public class SessionDAOImpl implements SessionDAO {
    private final Connection connection;

    public SessionDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    //constructor pour les tests

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

    // ... other code ...

    /**
     * This method removes a session from the database.
     *
     * @param id
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
     * @param session
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

    @Override
    public void deleteAll() {
        try {
            connection.createStatement().executeUpdate("DELETE FROM seances");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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