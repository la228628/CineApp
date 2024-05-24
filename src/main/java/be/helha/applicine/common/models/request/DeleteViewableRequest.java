package be.helha.applicine.common.models.request;

/**
 * Request to delete a viewable
 */
public class DeleteViewableRequest extends ClientEvent {
    private final int viewableId;
    private boolean success;

    private String message;

    public DeleteViewableRequest(int viewableId) {
        this.viewableId = viewableId;
    }

    public int getViewableId() {
        return viewableId;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    public void setSuccess(boolean b) {
        this.success = b;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
