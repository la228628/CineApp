package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Ticket;
import be.helha.applicine.common.models.exceptions.DaoException;

import java.util.List;

public interface TicketDAO {
    boolean create(Ticket ticket) throws DaoException;
    List<Ticket> getTicketsByClient(int clientId) throws DaoException;
}
