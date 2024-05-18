package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Viewable;

public class AddViewableRequest extends ClientEvent {
    private Viewable viewable;

    public AddViewableRequest(Viewable viewable) {
        this.viewable = viewable;
    }

    public Viewable getViewable() {
        return viewable;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
