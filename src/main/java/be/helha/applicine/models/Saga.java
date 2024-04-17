package be.helha.applicine.models;

import java.util.ArrayList;

public class Saga extends Visionable{
    private ArrayList<Movie> movies;

    public Saga(String title, String genre, String director, int duration, String synopsis, String imagePath) {
        super(title, genre, director, duration, synopsis, imagePath);
        this.movies = new ArrayList<>();

    }

    public Saga(int id, String title, String genre, String director, int duration, String synopsis, String imagePath) {
        super(id, title, genre, director, duration, synopsis, imagePath);
        this.movies = new ArrayList<>();
    }

    private void addMovieIntoSaga(Movie movie) {
        movies.add(movie);
    }

    @Override
    public ArrayList<String> getDescription() {
        ArrayList<String> descriptions = new ArrayList<>();
        for(Movie movie : movies) {
            descriptions.add(movie.getSynopsis());
        }
        return descriptions;
    }

    @Override
    public int getTotalDuration() {
        int totalDuration = 0;
        for (Movie movie : movies) {
            totalDuration += movie.getDuration();
        }
        return totalDuration;
    }

}
