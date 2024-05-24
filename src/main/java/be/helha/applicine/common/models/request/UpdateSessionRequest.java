package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.util.List;

/**
 * Request to update a session
 */
public class UpdateSessionRequest extends ClientEvent implements SessionRequest {
    private final MovieSession session;
    private boolean success;

    private String message;

    private List<Integer> conflictedSessions;

    /**
     * Getter of the session
     * @return the session
     */
    public MovieSession getSession() {
        return session;
    }

    /**
     * Constructor of the request
     * @param session the session
     */
    public UpdateSessionRequest(MovieSession session) {
        this.session = session;
    }

    /**
     * Setter of the success
     * @param success the success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter of the success
     * @return the success
     */
    public boolean getSuccess() {
        return success;
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
     * Setter of the message
     * @param message the message
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter of the message
     * @return the message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Setter of the conflicted sessions
     * @param conflictedSessions the conflicted sessions
     */
    @Override
    public void setConflictedSessions(List<Integer> conflictedSessions) {
        this.conflictedSessions = conflictedSessions;

    }

    /**
     * Getter of the conflicted sessions
     * @return the conflicted sessions
     */
    public List<Integer> getConflictedSessions() {
        return conflictedSessions;
    }
}
