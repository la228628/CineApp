package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Movie;

/**
 * Request to create a movie
 */
public class CreateMovieRequest extends ClientEvent {
    private final Movie movie;
    private boolean status;

    /**
     * Constructor of the request
     * @param movie the movie to create
     */
    public CreateMovieRequest(Movie movie) {
        this.movie = movie;
    }

    /**
     * Getter of the movie
     * @return the movie
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Dispatch the request to the visitor to handle it.
     * @param requestVisitor the visitor
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    /**
     * Setter of the status
     * @param b the status
     */
    public void setStatus(boolean b) {
        this.status = b;
    }

    /**
     * Getter of the status
     * @return the status
     */
    public boolean getStatus() {
        return status;
    }
}
