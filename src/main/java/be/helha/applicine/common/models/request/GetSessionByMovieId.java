package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.util.List;

/**
 * Request to get a session by its movie id
 */
public class GetSessionByMovieId extends ClientEvent{
    private final int movieID;
    private List<MovieSession> sessions;

    /**
     * Constructor of the request
     * @param movieID the id of the movie
     */
    public GetSessionByMovieId(int movieID) {
        this.movieID = movieID;
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
     * Getter of the movie id
     * @return the movie id
     */
    public int getMovieId() {
        return movieID;
    }

    /**
     * Setter of the sessions
     * @param sessions the sessions
     */
    public void setSessions(List<MovieSession> sessions) {
        this.sessions = sessions;
    }

    /**
     * Getter of the sessions
     * @return the sessions
     */
    public List<MovieSession> getSessions(){
        return this.sessions;
    }
}
