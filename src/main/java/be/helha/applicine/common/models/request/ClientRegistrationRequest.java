package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;

public class ClientRegistrationRequest extends ClientEvent{
    private Client client;
    public ClientRegistrationRequest(Client client) {
        this.client = client;
    }
    @Override
    public void dispatchOn(ClientHandler clientHandler) throws IOException, SQLException {
        clientHandler.handleClientRegistration(this.client);
    }
}
