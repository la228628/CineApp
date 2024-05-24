package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.util.List;

/**
 * Request to add a session to the database
 */
public class AddSessionRequest extends ClientEvent implements SessionRequest {
    private MovieSession session;
    private boolean success;
    private String message;
    private List<Integer> conflictedSessions;

    /**
     * Constructor for the request
     * @param session the session to add
     */
    public AddSessionRequest(MovieSession session) {
        this.session = session;
    }

    /**
     * Get the session to add
     * @return the session to add
     */
    public MovieSession getSession() {
        return session;
    }

    /**
     * Set the success of the request
     * @param success the session to add
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Get the success of the request
     * @return the success of the request
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Set the message of the request
     * @param message the message of the request
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the message of the request
     * @return the message of the request
     */
    public String getMessage() {
        return message;
    }

    /**
     * Dispatch the request to the visitor
     * @param requestVisitor the visitor to dispatch the request to
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    public List<Integer> getConflictedSessions() {
        return conflictedSessions;
    }

    public void setConflictedSessions(List<Integer> conflictedSessions) {
        this.conflictedSessions = conflictedSessions;
    }
}
