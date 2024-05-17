package be.helha.applicine.server.dao;

import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Viewable;

import java.util.ArrayList;
import java.util.Collection;

public interface ViewableDAO {
    void addViewable(String name, String type , ArrayList<Integer> movieIDs);
    boolean removeViewable(Integer id);

    void updateViewable(Integer id, String name, String type, ArrayList<Integer> movieIDs);

    void addMovieToViewable(Integer viewableID, Integer movieID);

    void removeViewableMovieCorrespondance(Integer viewableID);
    void addViewableWithOneMovie(String title, String singleMovie, int id);

    ArrayList<Movie> getMoviesFromViewable(Integer viewableID);

    public ArrayList<Viewable> getAllViewables();

    Viewable getViewableById(int id);

    ArrayList<Integer> getSeancesLinkedToViewable(int id);

    int sagasLinkedToMovie(int movieId);

    void removeViewableFromMovie(int movieId);

    int getViewableIdByMovieId(int id);
}