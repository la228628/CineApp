package com.example.applicine.models;

import com.example.applicine.database.DatabaseConnection;

public class Movie {

    private int id;
    private String title;
    private String genre;
    private String director;
    private int duration;
    private String synopsis;

    private String imagePath;

    public Movie(String title, String genre, String director, int duration, String synopsis, String ImagePath) {
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.imagePath = ImagePath;
        //l'id du film est auto incrémenté donc on ne le précise pas lors de la création d'un film
        this.id = DatabaseConnection.AddMovie(this);
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
