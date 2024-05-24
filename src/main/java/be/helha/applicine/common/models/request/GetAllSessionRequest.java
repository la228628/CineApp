package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.util.List;

/**
 * Request to get all the sessions
 */
public class GetAllSessionRequest extends ClientEvent{
    private List<MovieSession> sessions;

    /**
     * Constructor of the request
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    /**
     * Setter of the sessions
     * @param sessions the sessions
     */
    public void setSessions(List<MovieSession> sessions) {
        this.sessions = sessions;
    }

    /**
     * Getter of the sessions
     * @return the sessions
     */
    public List<MovieSession> getSessions() {
        return sessions;
    }
}
