package be.helha.applicine.common.models.request;

/**
 * Request to delete a viewable
 */
public class DeleteViewableRequest extends ClientEvent {
    private final int viewableId;
    private boolean success;

    private String message;

    /**
     * Constructor of the request
     * @param viewableId the id of the viewable to delete
     */
    public DeleteViewableRequest(int viewableId) {
        this.viewableId = viewableId;
    }

    /**
     * Getter of the id for the viewable
     * @return the id of the viewable
     */
    public int getViewableId() {
        return viewableId;
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
     * Setter of the success of the request
     * @param b the success of the request
     */
    public void setSuccess(boolean b) {
        this.success = b;
    }

    /**
     * Getter of the success of the request
     * @return the success of the request
     */
    public boolean getSuccess() {
        return success;
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
