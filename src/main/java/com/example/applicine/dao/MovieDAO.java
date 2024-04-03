package com.example.applicine.dao;

import com.example.applicine.models.Movie;

import java.util.List;

/**
 * This interface represents the Data Access Object for the movies.
 */
public interface MovieDAO {
    List<Movie> getAllMovies();
    Movie getMovieById(int id);
    void addMovie(Movie movie);
    void updateMovie(Movie movie);
    void removeMovie(int id) throws Exception;

    void removeAllMovies();

    void adaptAllImagePathInDataBase();
}
