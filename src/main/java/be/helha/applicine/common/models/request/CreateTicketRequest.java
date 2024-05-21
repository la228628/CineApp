package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Ticket;


public class CreateTicketRequest extends ClientEvent {
    private Ticket ticket;

    public CreateTicketRequest(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
