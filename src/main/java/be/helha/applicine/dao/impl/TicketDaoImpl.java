package be.helha.applicine.dao.impl;

import be.helha.applicine.dao.TicketDAO;
import be.helha.applicine.database.DatabaseConnection;

import java.sql.Connection;

public class TicketDaoImpl implements TicketDAO {
    private Connection connection;

    public TicketDaoImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void addTicket(int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode) {
        try {
            connection.createStatement().executeUpdate("INSERT INTO tickets (clientid, seanceid, seatcode, price, clienttype, verificationcode) VALUES (" + clientId + ", " + sessionId + ", '" + seatCode + "', " + price + ", '" + ticketType + "', '" + verificationCode + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTicket(int ticketId) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM tickets WHERE id = " + ticketId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTicket(int ticketId, int clientId, int sessionId, String ticketType, String seatCode, double price, String verificationCode) {
        try {
            connection.createStatement().executeUpdate("UPDATE tickets SET clientid = " + clientId + ", seanceid = " + sessionId + ", seatcode = '" + seatCode + "', price = " + price + ", clienttype = '" + ticketType + "', verificationcode = '" + verificationCode + "' WHERE id = " + ticketId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTicket(int ticketId) {
        try {
            connection.createStatement().executeQuery("SELECT * FROM tickets WHERE id = " + ticketId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllTickets() {
        try {
            connection.createStatement().executeQuery("SELECT * FROM tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTicketsByClient(int clientId) {
        try {
            connection.createStatement().executeQuery("SELECT * FROM tickets WHERE clientid = " + clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTicketsBySession(int sessionId) {
        try {
            connection.createStatement().executeQuery("SELECT * FROM tickets WHERE seanceid = " + sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
