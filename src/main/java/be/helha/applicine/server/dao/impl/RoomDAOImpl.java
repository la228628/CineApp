package be.helha.applicine.server.dao.impl;


import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.server.dao.RoomDAO;
import be.helha.applicine.server.database.DatabaseConnection;
import be.helha.applicine.common.models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {


    private Connection connection;

    private static final String SELECT_ALL_ROOMS = "SELECT * FROM rooms";
    private static final String SELECT_ROOM_BY_ID = "SELECT * FROM rooms WHERE id = ?";

    private static final String ADD_ROOM = "INSERT INTO rooms (seatsnumber, id) VALUES (?, ?)";

    private static final String UPDATE_ROOM = "UPDATE rooms SET seatsnumber = ? WHERE id = ?";

    private static final String DELETE_ROOM = "DELETE FROM rooms WHERE id = ?";

    public RoomDAOImpl(){
        this.connection = DatabaseConnection.getConnection();
    }

    //constructor pour les tests
    public RoomDAOImpl(Connection connection) {
        this.connection = connection;
    }


    /**
     * return a list with all rooms
     * @return
     * @throws SQLException
     */

    @Override
    public List<Room> getAll() throws DaoException {

        List<Room> rooms = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_ROOMS);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rooms.add(new Room(rs.getInt("id"), rs.getInt("seatsnumber")));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération des salles");
        }
        return rooms;
    }

    /**
     * return a room by id
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Room get(int id) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ROOM_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Room(rs.getInt("id"), rs.getInt("seatsnumber"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de la salle");
        }
        return null;
    }

    public boolean isRoomTableEmpty() throws DaoException {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM rooms");
            ResultSet rs = pstmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la vérification de la table des salles");
        }
    }

    public void fillRoomTable() throws DaoException {
        try {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO rooms (seatsnumber) VALUES\n" +
                    "(100),\n" +
                    "(80),\n" +
                    "(120),\n" +
                    "(150),\n" +
                    "(70),\n" +
                    "(90),\n" +
                    "(110),\n" +
                    "(85),\n" +
                    "(130),\n" +
                    "(60);");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors du remplissage de la table des salles");
        }
    }
}
