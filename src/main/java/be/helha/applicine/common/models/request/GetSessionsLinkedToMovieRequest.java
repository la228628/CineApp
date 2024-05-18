package be.helha.applicine.common.models.request;

public class GetSessionsLinkedToMovieRequest extends ClientEvent {
    private int movieId;

    public GetSessionsLinkedToMovieRequest(int movieId) {
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
