package be.helha.applicine.dao;

public interface TicketDAO {
    void addTicket(int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode);
    void deleteTicket(int ticketId);
    void updateTicket(int ticketId, int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode);
    void getTicket(int ticketId);
    void getAllTickets();
    void getTicketsByClient(int clientId);
    void getTicketsBySession(int sessionId);
}
