package be.helha.applicine.common.models.request;

/**
 * Request to ping the server and test the connection
 */
public class PingServer extends ClientEvent{
    /**
     * Dispatch the request to the visitor to handle it.
     * @param requestVisitor the visitor
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
