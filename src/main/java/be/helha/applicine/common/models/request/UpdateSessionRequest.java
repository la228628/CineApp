package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

public class UpdateSessionRequest extends ClientEvent{
    private MovieSession session;

    public MovieSession getSession() {
        return session;
    }

    public UpdateSessionRequest(MovieSession session) {
        this.session = session;
    }


    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
