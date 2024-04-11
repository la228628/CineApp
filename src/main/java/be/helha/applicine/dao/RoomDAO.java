package be.helha.applicine.dao;

import java.sql.SQLException;
import java.util.List;
import be.helha.applicine.models.Room;

public interface RoomDAO {
    List<Room> getAllRooms() throws SQLException;
    Room getRoomById(int id) throws SQLException;
    void addRoom(Room room);
    void updateRoom(Room room);
    void removeRoom(int id) throws Exception;

    boolean isRoomTableEmpty();

    void fillRoomTable();
}
