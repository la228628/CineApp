package be.helha.applicine.common.models.request;

import be.helha.applicine.server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;

public class GetSessionByIdRequest extends ClientEvent{
    private int sessionId;

    public GetSessionByIdRequest(int id) {
        this.sessionId = id;
    }

    @Override
    public void dispatchOn(ClientHandler clientHandler) throws SQLException, IOException {
        clientHandler.handleGetSessionById(this.sessionId);
    }
}
