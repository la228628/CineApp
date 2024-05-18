package be.helha.applicine.common.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing a saga.
 */
public class Saga extends Viewable implements Serializable {
    /**
     * The list of movies in the saga.
     */
    private final ArrayList<Movie> movies;

    /**
     * Constructor for the saga.
     *
     * @param title     The title of the saga.
     * @param genre     The genre of the saga.
     * @param director  The director of the saga.
     * @param duration  The duration of the saga.
     * @param synopsis  The synopsis of the saga.
     * @param image The path to the image of the saga.
     */
    public Saga(String title, String genre, String director, int duration, String synopsis, byte[] image,String imagePath) {
        super(title, genre, director, duration, synopsis, image,imagePath);
        this.movies = new ArrayList<>();
    }

    /**
     * Constructor for the saga.
     *
     * @param id        The id of the saga.
     * @param title     The title of the saga.
     * @param genre     The genre of the saga.
     * @param director  The director of the saga.
     * @param duration  The duration of the saga.
     * @param synopsis  The synopsis of the saga.
     * @param image The path to the image of the saga.
     */
    public Saga(int id, String title, String genre, String director, int duration, String synopsis, byte[] image,String imagePath) {
        super(id, title, genre, director, duration, synopsis, image,imagePath);
        this.movies = new ArrayList<>();
    }

    //Je vais tester ce constructeur (on rajoute un ArrayList de Movie directement dans le constructeur)
    public Saga(int id, String title, String genre, String director, int duration, String synopsis,byte[] image, String imagePath, ArrayList<Movie> movies) {
        super(id,title, genre, director, duration, synopsis, image,imagePath);
        this.movies = movies;
    }

    /**
     * Add a movie into the saga.
     *
     * @param movie The movie to add.
     */
    public void addMovieIntoSaga(Movie movie) {
        movies.add(movie);
    }

    /**
     * Get the description of the saga.
     *
     * @return the description of the saga.
     */
    @Override
    public String getDescription() {
        StringBuilder descriptions = new StringBuilder();
        for (Movie movie : movies) {
            descriptions.append(movie.getSynopsis()).append("\n");
        }
        //saut de ligne entre chaque synopsis /n
        return descriptions.toString();
    }

    /**
     * Get the total duration of the saga.
     *
     * @return The total duration of the saga.
     */
    @Override
    public int getTotalDuration() {
        int totalDuration = 0;
        for (Movie movie : movies) {
            totalDuration += movie.getDuration();
        }
        return totalDuration;
    }

    /**
     * Get the list of movies in the saga.
     *
     * @return The list of movies in the saga.
     */
    public ArrayList<Movie> getMovies() {
        return movies;
    }


}
