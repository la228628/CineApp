package be.helha.applicine.common.models.exceptions;
public class AdminIsAlreadyLoggedException extends Exception{
    /**
     * Constructor for the exception.
     * AdminIsAlreadyLoggedException is thrown when the admin is already logged.
     * @param message the message to display.
     */
    public AdminIsAlreadyLoggedException(String message) {
        super(message);
    }
}
