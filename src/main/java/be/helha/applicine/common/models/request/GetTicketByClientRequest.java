package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Ticket;

import java.util.List;

/**
 * Request to get all the tickets of a client
 */
public class GetTicketByClientRequest extends ClientEvent {
    private final int clientId;
    private List<Ticket> tickets;

    /**
     * Constructor of the request
     * @param clientId the id of the client
     */
    public GetTicketByClientRequest(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Getter of the client id
     * @return the client id
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Dispatch the request to the visitor to handle it.
     * @param visitor the visitor
     */
    @Override
    public void dispatchOn(RequestVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Setter of the tickets
     * @param tickets the tickets
     */
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Getter of the tickets
     * @return the tickets
     */
    public List<Ticket> getTickets() {
        return tickets;
    }
}
