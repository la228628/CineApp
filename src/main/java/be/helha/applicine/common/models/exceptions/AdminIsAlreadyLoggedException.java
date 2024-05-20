package be.helha.applicine.common.models.exceptions;
public class AdminIsAlreadyLoggedException extends Exception{
    /**
     * Constructor for the exception.
     * AdminIsAlreadyLoggedException is thrown when the admin is already logged.
     * @param message
     */
    public AdminIsAlreadyLoggedException(String message) {
        super(message);
    }
}
