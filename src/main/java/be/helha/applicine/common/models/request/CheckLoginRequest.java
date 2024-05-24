package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Client;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Request to check if the login is correct
 */
public class CheckLoginRequest extends ClientEvent{

    private String username;
    private String password;
    private Client client;

    /**
     * Constructor of the request
     * @param username the username of the client
     * @param password the password of the client
     */
    public CheckLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Getter of the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter of the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Dispatch the request to the visitor to handle it.
     * @param requestVisitor the visitor
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    /**
     * Setter of the client
     * @param client the client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Getter of the client
     * @return the client
     */
    public Client getClient() {
        return client;
    }

}
