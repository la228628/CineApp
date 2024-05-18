package be.helha.applicine.common.models.request;

public class GetTicketByClientRequest extends ClientEvent {
    private int clientId;

    public GetTicketByClientRequest(int clientId) {
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public void dispatchOn(RequestVisitor visitor) {
        visitor.visit(this);
    }
}
