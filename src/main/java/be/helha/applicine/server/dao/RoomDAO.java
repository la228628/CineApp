package be.helha.applicine.server.dao;

import java.sql.SQLException;
import java.util.List;
import be.helha.applicine.common.models.Room;
import be.helha.applicine.common.models.exceptions.DaoException;

public interface RoomDAO {
    List<Room> getAll() throws DaoException;
    Room get(int id) throws DaoException;
    void create(Room room);
    void update(Room room);

    void delete(int id) throws Exception;

    boolean isRoomTableEmpty() throws DaoException;

    void fillRoomTable() throws DaoException;

    void deleteAll();
}
