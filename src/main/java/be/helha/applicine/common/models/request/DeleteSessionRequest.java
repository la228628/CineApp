package be.helha.applicine.common.models.request;

/**
 * Request to delete a session
 */
public class DeleteSessionRequest extends ClientEvent {
    private final int sessionId;
    private boolean status;

    /**
     * Constructor of the request
     * @param sessionId the id of the session to delete
     */
    public DeleteSessionRequest(int sessionId) {
        this.sessionId = sessionId;
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
     * Getter of the session id
     * @return the session id
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * Setter of the status
     * @param b the status
     */
    public void setSuccess(boolean b) {
        this.status = b;
    }

    /**
     * Getter of the status
     * @return the status
     */
    public boolean getStatus() {
        return status;
    }
}
