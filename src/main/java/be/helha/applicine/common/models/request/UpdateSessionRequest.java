package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateSessionRequest extends ClientEvent implements SessionRequest {
    private MovieSession session;
    private boolean success;

    private String message;

    public MovieSession getSession() {
        return session;
    }

    public UpdateSessionRequest(MovieSession session) {
        this.session = session;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
