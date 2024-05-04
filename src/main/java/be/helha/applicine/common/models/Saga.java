package be.helha.applicine.common.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Saga extends Visionable implements Serializable {
    private ArrayList<Movie> movies;

    public Saga(String title, String genre, String director, int duration, String synopsis, String imagePath) {
        super(title, genre, director, duration, synopsis, imagePath);
        this.movies = new ArrayList<>();

    }

    public Saga(int id, String title, String genre, String director, int duration, String synopsis, String imagePath) {
        super(id, title, genre, director, duration, synopsis, imagePath);
        this.movies = new ArrayList<>();
    }

    public void addMovieIntoSaga(Movie movie) {
        movies.add(movie);
    }

    @Override
    public String getDescription() {
        StringBuilder descriptions = new StringBuilder();
        for(Movie movie : movies) {
            descriptions.append(movie.getSynopsis()).append("\n");
        }
        //saut de ligne entre chaque synopsis /n
        return descriptions.toString();
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
