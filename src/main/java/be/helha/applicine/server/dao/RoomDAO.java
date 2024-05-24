package be.helha.applicine.server.dao;

import java.sql.SQLException;
import java.util.List;
import be.helha.applicine.common.models.Room;
import be.helha.applicine.common.models.exceptions.DaoException;

public interface RoomDAO {
    List<Room> getAll() throws DaoException;
    Room get(int id) throws DaoException;

    boolean isRoomTableEmpty() throws DaoException;

    void fillRoomTable() throws DaoException;
}
