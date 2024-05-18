package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Movie;

public class UpdateMovieRequest extends ClientEvent{
    private Movie movie;

    public UpdateMovieRequest(Movie movie){
        this.movie = movie;
    }

    public Movie getMovie(){
        return movie;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor){
        requestVisitor.visit(this);
    }
}
