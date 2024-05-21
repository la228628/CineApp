package be.helha.applicine.common.models.request;

public class GetMovieByIdRequest extends ClientEvent{
    private int id;
    public GetMovieByIdRequest(int id) {
        this.id = id;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        //je vais passer un objet de type Movie à la méthode handleGetMovieById
        requestVisitor.visit(this);
    }

    public int getMovieId() {
        return id;
    }
}
