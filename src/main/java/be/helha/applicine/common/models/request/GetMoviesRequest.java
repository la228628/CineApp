package be.helha.applicine.common.models.request;

import be.helha.applicine.server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;

public class GetMoviesRequest extends ClientEvent{

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) throws IOException, SQLException {
        requestVisitor.visit(this);
    }
}
