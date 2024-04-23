package be.helha.applicine.dao;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Viewable;

import java.util.ArrayList;

public interface ViewableDAO {
    void addViewable(String name, String type , ArrayList<Integer> movieIDs);
    void removeViewable(Integer id);

    void updateViewable(Integer id, String name, String type);

    void addMovieToViewable(Integer viewableID, Integer movieID);

    void removeViewableMovieCorrespondance(Integer viewableID);


    void addViewableWithOneMovie(String title, String singleMovie, int id);

    ArrayList<Movie> getMoviesFromViewable(Integer viewableID);

    public ArrayList<Viewable> getAllViewables();
}
