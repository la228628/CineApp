package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.exceptions.DaoException;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ClientsDAO {
    Client create(Client client) throws DaoException;
    Client get(int clientId) throws DaoException;
    Client getClientByUsername(String username) throws DaoException;
    Client getClientByEmail(String email) throws DaoException;

    void deleteAll();
}
