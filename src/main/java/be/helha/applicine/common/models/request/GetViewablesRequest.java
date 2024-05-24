package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Viewable;

import java.io.IOException;
import java.util.List;

/**
 * Request to get all the viewables
 */
public class GetViewablesRequest extends ClientEvent {
    private List<Viewable> viewables;

    /**
     * Constructor of the request
     * @return the viewables list
     */
    public List<Viewable> getViewables() {
        return viewables;
    }

    /**
     * Setter of the viewables
     * @param viewables the viewables
     */
    public void setViewables(List<Viewable> viewables) {
        System.out.println("Setting viewables in SetViewable : " + viewables);
        this.viewables = viewables;
    }

    /**
     * Dispatch the request to the visitor to handle it.
     * @param requestVisitor the visitor
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
