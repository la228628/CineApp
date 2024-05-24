package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Ticket;
import be.helha.applicine.common.models.exceptions.DaoException;

import java.util.List;

/**
 * Interface for the TicketDAO, links the DAO to the database for tickets table.
 * @see be.helha.applicine.server.dao.impl.TicketDAOImpl
 */
public interface TicketDAO {
    boolean create(Ticket ticket) throws DaoException;
    List<Ticket> getTicketsByClient(int clientId) throws DaoException;
}
