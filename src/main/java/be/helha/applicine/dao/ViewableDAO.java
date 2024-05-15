package be.helha.applicine.dao;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Viewable;

import java.util.ArrayList;
import java.util.Collection;

public interface ViewableDAO {
    void addViewable(String name, String type , ArrayList<Integer> movieIDs);
    void removeViewable(Integer id);

    void updateViewable(Integer id, String name, String type, ArrayList<Integer> movieIDs);

    void addMovieToViewable(Integer viewableID, Integer movieID);

    void removeViewableMovieCorrespondance(Integer viewableID);


    void addViewableWithOneMovie(String title, String singleMovie, int id);

    ArrayList<Movie> getMoviesFromViewable(Integer viewableID);

    public ArrayList<Viewable> getAllViewables();

    Viewable getViewableById(int id);

    ArrayList<Integer> getSeancesLinkedToViewable(int id);
}
