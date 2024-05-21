package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.io.IOException;
import java.sql.SQLException;

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
