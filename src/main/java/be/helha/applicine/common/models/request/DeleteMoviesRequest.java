package be.helha.applicine.common.models.request;

/**
 * Request to delete a movie
 */
public class DeleteMoviesRequest extends ClientEvent{
    private final int id;
    private boolean status;

    String message;

    /**
     * Constructor of the request
     * @param id the id of the movie to delete
     */
    public DeleteMoviesRequest(int id) {
        this.id = id;
    }

    /**
     * Getter of the id
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Dispatch the request to the visitor to handle it.
     * @param visitor the visitor
     */
    @Override
    public void dispatchOn(RequestVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Setter of the status
     * @param b the status
     */
    public void setStatus(boolean b) {
        this.status = b;
    }

    /**
     * Getter of the status
     * @return the status
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Setter of the message
     * @param message the message
     */
    public void setMessage(String message) {
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
