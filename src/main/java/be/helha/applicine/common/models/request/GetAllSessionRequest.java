package be.helha.applicine.common.models.request;

public class GetAllSessionRequest extends ClientEvent{
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
