package be.helha.applicine.common.models;

import java.io.Serializable;

public class Session implements Serializable {
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
