package be.helha.applicine.common.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Saga extends Viewable implements Serializable {
    private ArrayList<Movie> movies;

    public Saga(String title, String genre, String director, int duration, String synopsis, String imagePath) {
        super(title, genre, director, duration, synopsis, null, imagePath);
        this.movies = new ArrayList<>();
    }

    public Saga(int id, String title, String genre, String director, int duration, String synopsis, String imagePath) {
        super(id,title, genre, director,duration, synopsis, null, imagePath);
        this.movies = new ArrayList<>();
    }

    //Je vais tester ce constructeur (on rajoute un ArrayList de Movie directement dans le constructeur)
    public Saga(int id, String title, String genre, String director, int duration, String synopsis, String imagePath, ArrayList<Movie> movies, byte[] image) {
        super( title,  genre,  director, duration, synopsis,image, imagePath);
        this.movies = movies;
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

    public ArrayList<Movie> getMovies() {
        return movies;
    }



}
