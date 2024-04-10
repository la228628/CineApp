package be.helha.applicine.dao.impl;

import be.helha.applicine.dao.TicketDAO;
import be.helha.applicine.database.DatabaseConnection;

import java.sql.Connection;

public class TicketDAOImpl implements TicketDAO {
    private Connection connection;

    public TicketDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * This method adds a ticket to the database.
     * @param clientId
     * @param sessionId
     * @param ticketType
     * @param seatCode
     * @param price
     * @param verificationCode
     */

    @Override
    public void addTicket(int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode) {
        try {
            connection.createStatement().executeUpdate("INSERT INTO tickets (clientid, seanceid, seatcode, price, clienttype, verificationcode) VALUES (" + clientId + ", " + sessionId + ", '" + seatCode + "', " + price + ", '" + ticketType + "', '" + verificationCode + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * This method removes a ticket from the database.
     * @param ticketId
     */

    @Override
    public void deleteTicket(int ticketId) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM tickets WHERE id = " + ticketId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates a ticket in the database.
     * @param ticketId
     * @param clientId
     * @param sessionId
     * @param ticketType
     * @param seatCode
     * @param price
     * @param verificationCode
     */
    @Override
    public void updateTicket(int ticketId, int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode) {
        try {
            connection.createStatement().executeUpdate("UPDATE tickets SET clientid = " + clientId + ", seanceid = " + sessionId + ", seatcode = '" + seatCode + "', price = " + price + ", clienttype = '" + ticketType + "', verificationcode = '" + verificationCode + "' WHERE id = " + ticketId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns a ticket by its id.
     * @param ticketId
     */
    @Override
    public void getTicket(int ticketId) {
        try {
            connection.createStatement().executeQuery("SELECT * FROM tickets WHERE id = " + ticketId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns all the tickets in the database.
     */
    @Override
    public void getAllTickets() {
        try {
            connection.createStatement().executeQuery("SELECT * FROM tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns all the tickets of a client.
     * @param clientId
     */
    @Override
    public void getTicketsByClient(int clientId) {
        try {
            connection.createStatement().executeQuery("SELECT * FROM tickets WHERE clientid = " + clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns all the tickets of a session.
     * @param sessionId
     */
    @Override
    public void getTicketsBySession(int sessionId) {
        try {
            connection.createStatement().executeQuery("SELECT * FROM tickets WHERE seanceid = " + sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
