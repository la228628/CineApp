package be.helha.applicine.common.models;

import java.io.Serializable;

/**
 * This class represents a session of a client.
 * It contains a boolean to know if the client is logged in and a Client object.
 */
public class Session implements Serializable {
    /**
     * The boolean to know if the client is logged in.
     */
    private boolean isLogged;

    /**
     * The current client logged in.
     */
    private Client currentClient;

    /**
     * @return The boolean to know if the client is logged in.
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     * set the boolean to know if the client is logged in.
     *
     * @param logged The boolean to know if the client is logged in.
     */
    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    /**
     * set the current client logged in.
     *
     * @param client The current client logged in.
     */
    public void setCurrentClient(Client client) {
        currentClient = client;
    }

    /**
     * @return The current client logged in.
     */
    public Client getCurrentClient() {
        return currentClient;
    }
}
