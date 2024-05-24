package database;

import be.helha.applicine.common.models.Room;
import be.helha.applicine.server.dao.RoomDAO;
import be.helha.applicine.server.dao.impl.RoomDAOImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RoomDAOTest {
    private static RoomDAO roomDAO;

    @BeforeEach
    public void setup() {
        Connection connection = DatabaseConnectionTest.getConnection();
        roomDAO = new RoomDAOImpl(connection);
    }

    @AfterAll
    public static void tearDown() {
        roomDAO.deleteAll();
    }

    @Test
    public void testCreateRoom() throws SQLException {
        Room room = new Room(1, 100); // Assuming 1 is the room number and 100 is the capacity
        roomDAO.create(room);
        Room createdRoom = roomDAO.get(1);
        assertNotNull(createdRoom);
        assertEquals(room.getNumber(), createdRoom.getNumber());
    }

    @Test
    public void testGetRoom() throws SQLException {
        Room room = new Room(1, 100);
        roomDAO.create(room);
        Room roomReceived = roomDAO.get(1); // Assuming 1 is the id of the room
        assertNotNull(roomReceived);
        assertEquals(1, roomReceived.getNumber());
    }

    @Test
    public void testDeleteRoom() throws Exception {
        Room room = new Room(1, 100);
        roomDAO.create(room);
        roomDAO.delete(1);
        Room deletedRoom = roomDAO.get(1);
        assertNull(deletedRoom);
    }

    @Test
    public void testIsRoomTableEmpty() {
        boolean isEmpty = roomDAO.isRoomTableEmpty();
        assertFalse(isEmpty); // Assuming that the table is not empty
    }

    @Test
    public void testFillRoomTable() {
        roomDAO.fillRoomTable();
        boolean isEmpty = roomDAO.isRoomTableEmpty();
        assertFalse(isEmpty);
    }
}

