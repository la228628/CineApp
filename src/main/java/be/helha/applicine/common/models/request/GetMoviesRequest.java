package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.sql.SQLException;

public class GetMoviesRequest extends ClientEvent{

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
