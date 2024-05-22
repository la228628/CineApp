package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Client;


public class ClientRegistrationRequest extends ClientEvent{
    private Client client;
    public ClientRegistrationRequest(Client client) {
        this.client = client;
    }
    public Client getClient() {
        return client;
    }
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
