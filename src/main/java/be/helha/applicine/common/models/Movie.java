package be.helha.applicine.common.models;

import java.io.Serializable;


/**
 * Class representing a movie.

 */
public class Movie extends Viewable implements Serializable {

    /**
     * Constructor for the movie.
     * @param title The title of the movie.
     * @param genre The genre of the movie.
     * @param director The director of the movie.
     * @param duration The duration of the movie.
     * @param synopsis The synopsis of the movie.
     * @param ImagePath The path to the image of the movie.
     */
    public Movie(String title, String genre, String director, int duration, String synopsis, String ImagePath) {
        super(title, genre, director, duration, synopsis, null, ImagePath);
    }
    /**
     * Constructor for the movie.
     * @param id The id of the movie.
     * @param title The title of the movie.
     * @param genre The genre of the movie.
     * @param director The director of the movie.
     * @param duration The duration of the movie.
     * @param synopsis The synopsis of the movie.
     * @param imagePath The path to the image of the movie.
     */
    public Movie(int id, String title, String genre, String director, int duration, String synopsis, String imagePath) {
        super(id, title, genre, director, duration, synopsis, null, imagePath);
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
