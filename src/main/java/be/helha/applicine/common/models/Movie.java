package be.helha.applicine.common.models;

import java.io.Serializable;


/**
 * Class representing a movie.

 */
public class Movie extends Viewable implements Serializable {

    /**
     * Constructor for the movie.
     * It is the constructor that is used when the movie is created.
     * @param title The title of the movie.
     * @param genre The genre of the movie.
     * @param director The director of the movie.
     * @param duration The duration of the movie.
     * @param synopsis The synopsis of the movie.
     */
    public Movie(String title, String genre, String director, int duration, String synopsis, byte[] image, String imagePath) {
        super(title, genre, director, duration, synopsis, image, imagePath);
    }
    /**
     * Constructor for the movie.
     * It is the constructor that is used when the movie is modified.
     * @param id The id of the movie.
     * @param title The title of the movie.
     * @param genre The genre of the movie.
     * @param director The director of the movie.
     * @param duration The duration of the movie.
     * @param synopsis The synopsis of the movie.
     * @param imagePath The path to the image of the movie.
     */
    public Movie(int id, String title, String genre, String director, int duration, String synopsis, byte[] image, String imagePath) {
        super(id, title, genre, director, duration, synopsis, image, imagePath);
    }

    /**
     * Get the synopsis of the movie.
     * @return The synopsis of the movie.
     */
    @Override
    public String getDescription() {
        return getSynopsis();
    }

    /**
     * Get the duration of the movie.
     * @return The duration of the movie.
     */
    @Override
    public int getTotalDuration() {
        return getDuration();
    }
}
