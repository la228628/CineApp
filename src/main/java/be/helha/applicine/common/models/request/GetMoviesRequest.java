package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Request to get all the movies
 */
public class GetMoviesRequest extends ClientEvent {
    private List<Movie> movieList;

    /**
     * Constructor of the request
     */
    public GetMoviesRequest() {
        this.movieList = new ArrayList<>();
    }

    /**
     * Getter of the movie list
     * @return the movie list
     */
    public List<Movie> getMovieList() {
        return movieList;
    }

    /**
     * Setter of the movie list
     * @param movieList the movie list
     */
    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
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
     * Setter of the movies
     * @param movies the movies
     */
    public void setMovies(List<Movie> movies) {
        this.movieList = movies;
    }

    /**
     * Getter of the movies
     * @return the movies
     */
    public List<Movie> getMovies() {
        return movieList;
    }
}
