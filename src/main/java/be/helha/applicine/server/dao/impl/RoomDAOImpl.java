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

/**
 * Class that implements the RoomDAO interface
 * @see be.helha.applicine.server.dao.RoomDAO for more information
 */
public class RoomDAOImpl implements RoomDAO {


    private final Connection connection;

    private static final String SELECT_ALL_ROOMS = "SELECT * FROM rooms";
    private static final String SELECT_ROOM_BY_ID = "SELECT * FROM rooms WHERE id = ?";

    private static final String ADD_ROOM = "INSERT INTO rooms (seatsnumber, id) VALUES (?, ?)";

    private static final String UPDATE_ROOM = "UPDATE rooms SET seatsnumber = ? WHERE id = ?";

    private static final String DELETE_ROOM = "DELETE FROM rooms WHERE id = ?";

    /**
     * Constructor that initializes the connection to the database
     */
    public RoomDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Constructor for tests
     * @param connection to the database
     */
    public RoomDAOImpl(Connection connection) {
        this.connection = connection;
    }


    /**
     * Return a list with all rooms
     * @return a list with all rooms
     * @throws DaoException if there is an error
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
     * Return a room by its id
     * @param id of the room
     * @return the room
     * @throws DaoException if there is an error
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

    /**
     * Add a room
     * @param room to add
     */
    @Override
    public void create(Room room) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(ADD_ROOM);
            pstmt.setInt(1, room.getCapacity());
            pstmt.setInt(2, room.getNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la salle : " + e.getMessage());
            if (e.getMessage().contains("missing database")) {
                System.out.println("La base de données n'existe pas");
            }
        }
    }

    /**
     * Update a room
     * @param room to update
     */
    @Override
    public void update(Room room) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_ROOM);
            pstmt.setInt(1, room.getCapacity());
            pstmt.setInt(2, room.getNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la salle : " + e.getMessage());
            if (e.getMessage().contains("missing database")) {
                System.out.println("La base de données n'existe pas");
            }
        }
    }

    /**
     * Remove a room
     * @param id of the room
     * @throws Exception if the room doesn't exist
     */
    @Override
    public void delete(int id) throws Exception {
        try {
            PreparedStatement pstmt = connection.prepareStatement(DELETE_ROOM);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la salle : " + e.getMessage());
            if (e.getMessage().contains("missing database")) {
                System.out.println("La base de données n'existe pas");
            }
        }

    }

    /**
     * Check if the table is empty
     * @return true if the table is empty
     * @throws DaoException if there is an error
     */
    public boolean isRoomTableEmpty() throws DaoException {
        try {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM rooms");
            ResultSet rs = pstmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la vérification de la table des salles");
        }
    }

    /**
     * Fill the room table with some data
     * @throws DaoException if there is an error
     */
    public void fillRoomTable() throws DaoException {
        try {
            PreparedStatement pstmt = connection.prepareStatement("""
                    INSERT INTO rooms (seatsnumber) VALUES
                    (100),
                    (80),
                    (120),
                    (150),
                    (70),
                    (90),
                    (110),
                    (85),
                    (130),
                    (60);""");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors du remplissage de la table des salles");
        }
    }

    /**
     * Delete all rooms
     */
    @Override
    public void deleteAll() {
        try {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM rooms");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de toutes les salles : " + e.getMessage());
            if (e.getMessage().contains("missing database")) {
                System.out.println("La base de données n'existe pas");
            }
        }
    }
}
