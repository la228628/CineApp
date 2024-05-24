package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Client;

/**
 * Request to register a client
 */
public class ClientRegistrationRequest extends ClientEvent{
    private final Client client;
    private boolean status;

    /**
     * Constructor of the request
     * @param client the client to register
     */
    public ClientRegistrationRequest(Client client) {
        this.client = client;
    }

    /**
     * Getter of the client
     * @return the client
     */
    public Client getClient() {
        return client;
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
     * Setter of the success
     * @param b the success
     */
    public void setSuccess(boolean b) {
        this.status = b;
    }

    /**
     * Getter of the success
     * @return the success
     */
    public boolean getStatus() {
        return status;
    }
}
