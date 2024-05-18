package be.helha.applicine.common.models.request;

public class DeleteViewableRequest extends ClientEvent {
    private int viewableId;

    public DeleteViewableRequest(int viewableId) {
        this.viewableId = viewableId;
    }

    public int getViewableId() {
        return viewableId;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
