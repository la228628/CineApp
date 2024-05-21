package be.helha.applicine.common.models.request;

import java.io.IOException;

public class GetViewablesRequest extends ClientEvent {
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
