package be.helha.applicine.common.models.request;

/**
 * Request to send an error message
 */
public class ErrorMessage {
    private final String message;

    /**
     * Constructor of the request
     * @param message the message
     */
    public ErrorMessage(String message) {
        this.message = message;
    }

    /**
     * Getter of the message
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
