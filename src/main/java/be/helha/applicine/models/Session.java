package be.helha.applicine.models;

public class Session {
    private boolean isLogged;
    private Client currentClient;

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public void setCurrentClient(Client client) {
        currentClient = client;
    }

    public Client getCurrentClient() {
        return currentClient;
    }
}
