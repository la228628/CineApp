package be.helha.applicine.common.models.request;

import be.helha.applicine.server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;

public class GetAllSessionRequest extends ClientEvent{
    @Override
    public void dispatchOn(ClientHandler clientHandler) throws IOException, SQLException {
        clientHandler.handleGetAllSessions();
    }
}
