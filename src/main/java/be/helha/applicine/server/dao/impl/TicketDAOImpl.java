package be.helha.applicine.server.dao.impl;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.server.dao.TicketDAO;
import be.helha.applicine.server.database.DatabaseConnection;
import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the implementation of the TicketDAO interface.
 */
public class TicketDAOImpl implements TicketDAO {
    private final Connection connection;
    private final SessionDAOImpl sessionDAO;

    /**
     * The constructor of the TicketDAOImpl class.
     */
    public TicketDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
        this.sessionDAO = new SessionDAOImpl();
    }

    /**
     * This method creates a ticket in the database.
     * @param ticket The ticket to create.
     * @return True if the ticket has been created, false otherwise.
     * @throws DaoException If the creation of the ticket has failed.
     */
    @Override
    public boolean create(Ticket ticket) throws DaoException {
        int clientId = ticket.getClientLinked().getId();
        int sessionId = ticket.getMovieSessionLinked().getId();
        String ticketType = ticket.getType();
        String seatCode = ticket.getSeat();
        double price = ticket.getPrice();
        String verificationCode = ticket.getTicketVerificationCode();
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
                    return true;
                } else {
                    throw new DaoException("La création du ticket a échoué.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("La création du ticket a échoué.");
        }
    }

    /**
     * This method gets all tickets for a specific client from the database.
     * @param clientId The id of the client.
     * @return List of tickets
     * @throws DaoException If the retrieval of the tickets has failed.
     */
    @Override
    public List<Ticket> getTicketsByClient(int clientId) throws DaoException {
        List<Ticket> tickets = new ArrayList<>();
        ClientsDAOImpl clientsDAO = new ClientsDAOImpl();
        Client client;
        client = clientsDAO.get(clientId);
        String query = "SELECT * FROM tickets WHERE clientid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                MovieSession movieSession = sessionDAO.get(resultSet.getInt("seanceid"));
                String seatCode = resultSet.getString("seatcode");
                double price = resultSet.getDouble("price");
                String ticketType = resultSet.getString("clienttype");
                String verificationCode = resultSet.getString("verificationcode");
                Ticket ticket = new Ticket(id, client, movieSession, ticketType, seatCode, price, verificationCode);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            throw new DaoException("La récupération des tickets a échoué.");
        }
        return tickets;
    }
}
