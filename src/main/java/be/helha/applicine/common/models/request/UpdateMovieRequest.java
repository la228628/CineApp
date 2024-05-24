package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Movie;

/**
 * Request to update a movie
 */
public class UpdateMovieRequest extends ClientEvent{
    private final Movie movie;
    private boolean status;

    /**
     * Constructor of the request
     * @param movie the movie to update
     */
    public UpdateMovieRequest(Movie movie){
        this.movie = movie;
    }

    /**
     * Getter of the movie
     * @return the movie
     */
    public Movie getMovie(){
        return movie;
    }

    /**
     * Setter of the status
     * @param status the status
     */
    public void setStatus(boolean status){
        this.status = status;
    }

    /**
     * Getter of the status
     * @return the status
     */
    public boolean getStatus(){
        return status;
    }

    /**
     * Dispatch the request to the visitor to handle it.
     * @param requestVisitor the visitor
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor){
        requestVisitor.visit(this);
    }
}
