package be.helha.applicine.common.models.request;

import be.helha.applicine.server.ClientHandler;

public class DeleteMoviesRequest extends ClientEvent{
    private int id;

    public DeleteMoviesRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void dispatchOn(ClientHandler clientHandler) {
        clientHandler.handleDeleteMovie(this.id);
    }
}
