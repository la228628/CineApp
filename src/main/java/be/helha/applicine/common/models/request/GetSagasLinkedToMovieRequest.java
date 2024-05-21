package be.helha.applicine.common.models.request;

import be.helha.applicine.client.views.AlertViewController;

import java.io.IOException;

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
