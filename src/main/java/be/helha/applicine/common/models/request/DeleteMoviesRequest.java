package be.helha.applicine.common.models.request;

public class DeleteMoviesRequest extends ClientEvent{
    private int id;

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
}
