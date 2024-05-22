package be.helha.applicine.common.models.request;

public class GetSessionByIdRequest extends ClientEvent{
    private int sessionId;

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
}
