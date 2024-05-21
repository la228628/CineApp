package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.sql.SQLException;

public class GetAllSessionRequest extends ClientEvent{
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
