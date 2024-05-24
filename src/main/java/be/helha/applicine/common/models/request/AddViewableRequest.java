package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Viewable;

/**
 * Request to add a viewable to the database
 */
public class AddViewableRequest extends ClientEvent {
    private final Viewable viewable;
    private boolean success;

    /**
     * Constructor for the request
     * @param viewable the viewable to add
     */
    public AddViewableRequest(Viewable viewable) {
        this.viewable = viewable;
    }

    /**
     * Get the viewable to add
     * @return the viewable to add
     */
    public Viewable getViewable() {
        return viewable;
    }

    /**
     * Dispatch the request to the visitor
     * @param requestVisitor the visitor to dispatch the request to
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    /**
     * Set the success of the request
     * @param b the success of the request
     */
    public void setSuccess(boolean b) {
        this.success = b;
    }

    /**
     * Get the success of the request
     * @return the success of the request
     */
    public boolean getSuccess() {
        return success;
    }
}