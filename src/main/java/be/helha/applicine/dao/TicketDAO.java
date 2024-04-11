package be.helha.applicine.dao;

import be.helha.applicine.models.Ticket;

import java.util.List;

public interface TicketDAO {
    void addTicket(int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode);
    List<Ticket> getTicketsByClient(int clientId);
}
