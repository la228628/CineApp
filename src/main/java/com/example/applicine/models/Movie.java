package com.example.applicine.models;

import com.example.applicine.database.DatabaseConnection;

import java.sql.SQLException;

/**
 * This class represents a movie.
 */
public class Movie {

    private int id;
    private String title;
    private String genre;
    private String director;
    private int duration;
    private String synopsis;

    private String imagePath;

    /**
     * Constructor for the movie.
     * @param title The title of the movie.
     * @param genre The genre of the movie.
     * @param director The director of the movie.
     * @param duration The duration of the movie.
     * @param synopsis The synopsis of the movie.
     * @param ImagePath The path to the image of the movie.
     */
    public Movie(String title, String genre, String director, int duration, String synopsis, String ImagePath) throws SQLException {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.imagePath = ImagePath;
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
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public int getDuration() {
        return duration;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
