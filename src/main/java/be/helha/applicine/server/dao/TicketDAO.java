package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Ticket;

import java.util.List;

public interface TicketDAO {
    void addTicket(int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode);
    List<Ticket> getTicketsByClient(int clientId);

    void deleteTicket(Integer ticketId);
}
