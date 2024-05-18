package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

public class AddSessionRequest extends ClientEvent {
    private MovieSession session;

    public AddSessionRequest(MovieSession session) {
        this.session = session;
    }

    public MovieSession getSession() {
        return session;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
