package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Request to get a session by its id
 */
public class GetSessionByIdRequest extends ClientEvent{
    private int sessionId;
    MovieSession session;

    /**
     * Constructor of the request
     * @param id the id of the session
     */
    public GetSessionByIdRequest(int id) {
        this.sessionId = id;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSession(MovieSession session) {
        this.session = session;
    }

    public MovieSession getSession(){
        return this.session;
    }
}
