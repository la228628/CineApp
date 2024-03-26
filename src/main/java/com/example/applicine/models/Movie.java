package com.example.applicine.models;

import com.example.applicine.database.DatabaseConnection;

import java.sql.SQLException;

public class Movie {

    private int id;
    private String title;
    private String genre;
    private String director;
    private int duration;
    private String synopsis;

    private String imagePath;

    public Movie(String title, String genre, String director, int duration, String synopsis, String ImagePath) throws SQLException {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.imagePath = ImagePath;
        this.id = DatabaseConnection.getNewMovieId();
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

    public Integer getID() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }
}
