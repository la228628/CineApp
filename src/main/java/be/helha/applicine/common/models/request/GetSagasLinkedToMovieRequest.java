package be.helha.applicine.common.models.request;

public class GetSagasLinkedToMovieRequest extends ClientEvent {
    private final int movieId;

    public GetSagasLinkedToMovieRequest(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
