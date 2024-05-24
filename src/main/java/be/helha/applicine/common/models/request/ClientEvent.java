package be.helha.applicine.common.models.request;

import java.io.Serializable;

/**
 * Request to check if the login is correct
 */
public abstract class ClientEvent implements Serializable {
    private String message;

    /**
     * Constructor of the request
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter of the message
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Dispatch the request to the visitor to handle it.
     * @param requestVisitor the visitor
     */
    public abstract void dispatchOn(RequestVisitor requestVisitor);
}
