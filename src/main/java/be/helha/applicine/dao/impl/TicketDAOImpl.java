package be.helha.applicine.dao.impl;

import be.helha.applicine.dao.TicketDAO;
import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.models.MovieSession;
import be.helha.applicine.models.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOImpl implements TicketDAO {
    private Connection connection;
    private SessionDAOImpl sessionDAO;

    public TicketDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
        this.sessionDAO = new SessionDAOImpl();
    }

    /**
     * This method adds a ticket to the database.
     *
     * @param clientId The id of the client who bought the ticket.
     * @param sessionId The id of the session the ticket is for.
     * @param ticketType The type of the ticket (student, senior, normal).
     * @param seatCode The code of the seat the ticket is for.
     * @param price The price of the ticket.
     * @param verificationCode The verification code of the ticket.
     */

    @Override
    public void addTicket(int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode) {
        try {
            String query = "INSERT INTO tickets (clientid, seanceid, seatcode, price, clienttype, verificationcode) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, clientId);
            preparedStatement.setInt(2, sessionId);
            preparedStatement.setString(3, seatCode);
            preparedStatement.setDouble(4, price);
            preparedStatement.setString(5, ticketType);
            preparedStatement.setString(6, verificationCode);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.println("Inserted ticket ID: " + id);
                } else {
                    throw new SQLException("Creating ticket failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            System.out.println("erreur laissée, changera avec serveur");
        }
    }

    @Override
    public List<Ticket> getTicketsByClient(int clientId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets WHERE clientid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                MovieSession movieSession = sessionDAO.getSessionById(resultSet.getInt("seanceid"));
                String seatCode = resultSet.getString("seatcode");
                double price = resultSet.getDouble("price");
                String ticketType = resultSet.getString("clienttype");
                String verificationCode = resultSet.getString("verificationcode");
                Ticket ticket = new Ticket(id, clientId, movieSession, ticketType, seatCode, price, verificationCode);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println("erreur laissée, changera avec serveur");
        }
        return tickets;
    }

    @Override
    public void deleteTicket(Integer ticketId) {
        String query = "DELETE FROM tickets WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ticketId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("erreur laissée, changera avec serveur");
        }
    }
}
