package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.sql.SQLException;

public class CreateTicketRequest extends ClientEvent {
    private int clientId;
    private int sessionId;
    private String ticketType;
    private double price;

    public CreateTicketRequest(int clientId, int sessionId, String ticketType, double price) {
        this.clientId = clientId;
        this.sessionId = sessionId;
        this.ticketType = ticketType;
        this.price = price;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) throws IOException, SQLException {
        requestVisitor.visit(this);
    }

    public int getClientId() {
        return clientId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public double getPrice() {
        return price;
    }
}
