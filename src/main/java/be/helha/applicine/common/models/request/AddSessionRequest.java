package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

public class AddSessionRequest extends ClientEvent implements SessionRequest {
    private MovieSession session;
    private boolean success;
    private String message;

    public AddSessionRequest(MovieSession session) {
        this.session = session;
    }

    public MovieSession getSession() {
        return session;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
