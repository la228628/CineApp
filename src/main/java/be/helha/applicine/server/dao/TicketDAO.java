package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Ticket;

import java.util.List;

public interface TicketDAO {
    boolean create(Ticket ticket);
    List<Ticket> getTicketsByClient(int clientId);

    void delete(Integer ticketId);
}
