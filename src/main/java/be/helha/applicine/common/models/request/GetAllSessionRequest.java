package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetAllSessionRequest extends ClientEvent{
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) throws IOException, SQLException {
        requestVisitor.visit(this);
    }
}
