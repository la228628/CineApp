package com.example.applicine.models;

public class Movie {

    private int id;
    private String title;
    private String genre;
    private String director;
    private int duration;
    private String synopsis;

    private String ImagePath;

    public Movie(int id, String title, String genre, String director, int duration, String synopsis, String ImagePath) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.ImagePath = ImagePath;
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
}
