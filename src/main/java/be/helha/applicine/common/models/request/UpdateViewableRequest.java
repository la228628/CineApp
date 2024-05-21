package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Saga;

import java.io.IOException;

public class UpdateViewableRequest extends ClientEvent {
    private Saga saga;

    public UpdateViewableRequest(Saga saga) {
        this.saga = saga;
    }

    public Saga getSaga() {
        return saga;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
