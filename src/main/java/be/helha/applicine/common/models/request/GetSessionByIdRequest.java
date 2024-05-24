package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

/**
 * Request to get a session by its id
 */
public class GetSessionByIdRequest extends ClientEvent{
    private final int sessionId;
    MovieSession session;

    /**
     * Constructor of the request
     * @param id the id of the session
     */
    public GetSessionByIdRequest(int id) {
        this.sessionId = id;
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
     * Getter of the id
     * @return the id
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * Setter of the session
     * @param session the session
     */
    public void setSession(MovieSession session) {
        this.session = session;
    }

    /**
     * Getter of the session
     * @return the session
     */
    public MovieSession getSession(){
        return this.session;
    }
}
