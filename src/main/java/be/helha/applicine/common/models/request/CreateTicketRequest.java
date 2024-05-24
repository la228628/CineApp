package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Ticket;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Request to create a ticket
 */
public class CreateTicketRequest extends ClientEvent {
    private final Ticket ticket;
    private boolean status;

    /**
     * Constructor of the request
     * @param ticket the ticket to create
     */
    public CreateTicketRequest(Ticket ticket) {
        this.ticket = ticket;
    }

    /**
     * Getter of the ticket
     * @return the ticket
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Dispatch the request to the visitor to handle it.
     * @param requestVisitor the visitor
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    /**
     * Setter of the status
     * @param b the status
     */
    public void setStatus(boolean b) {
        this.status = b;
    }

    /**
     * Getter of the status
     * @return the status
     */
    public boolean getStatus(){
        return this.status;
    }
}
