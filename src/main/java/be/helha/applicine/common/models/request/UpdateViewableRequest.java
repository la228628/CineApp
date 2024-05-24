package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Saga;

import java.io.IOException;

/**
 * Request to update a viewable
 */
public class UpdateViewableRequest extends ClientEvent {
    private final Saga saga;
    private boolean success;

    /**
     * Constructor of the request
     * @param saga the viewable to update
     */
    public UpdateViewableRequest(Saga saga) {
        this.saga = saga;
    }

    /**
     * Getter of the viewable
     * @return the viewable
     */
    public Saga getSaga() {
        return saga;
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
     * Setter of the success
     * @param b the success
     */
    public void setSuccess(boolean b) {
        this.success = b;
    }

    /**
     * Getter of the success
     * @return the success
     */
    public boolean getSuccess() {
        return success;
    }
}
