package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Movie;

public class CreateMovieRequest extends ClientEvent {
    private Movie movie;

    public CreateMovieRequest(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
