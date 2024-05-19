package be.helha.applicine.server.dao;

import java.sql.SQLException;
import java.util.List;
import be.helha.applicine.common.models.Room;

public interface RoomDAO {
    List<Room> getAll() throws SQLException;
    Room get(int id) throws SQLException;
    void create(Room room);
    void update(Room room);
    void delete(int id) throws Exception;

    boolean isRoomTableEmpty();

    void fillRoomTable();
}
