package be.helha.applicine.models.exceptions;

public class InvalideFieldsExceptions extends Exception {
    /**
     * Constructor for the exception.
     * InvalidFieldsExceptions is thrown when the fields are invalid ( empty or bad format ).
     * @param message
     */
    public InvalideFieldsExceptions(String message) {
        super(message);
    }

}
