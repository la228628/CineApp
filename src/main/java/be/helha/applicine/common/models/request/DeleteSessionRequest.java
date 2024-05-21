package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteSessionRequest extends ClientEvent {
    private int sessionId;

    public DeleteSessionRequest(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    public int getSessionId() {
        return sessionId;
    }
}
