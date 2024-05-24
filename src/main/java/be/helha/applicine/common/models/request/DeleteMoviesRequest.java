package be.helha.applicine.common.models.request;

public class DeleteMoviesRequest extends ClientEvent{
    private final int id;
    private boolean status;

    String message;

    public DeleteMoviesRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void dispatchOn(RequestVisitor visitor) {
        visitor.visit(this);
    }

    public void setStatus(boolean b) {
        this.status = b;
    }

    public boolean getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
