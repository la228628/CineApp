package be.helha.applicine.common.models.request;

public class GetViewablesRequest extends ClientEvent {
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
