package com.example.applicine.models;

public class Session {
    private Movie movie;
    private String date;
    private String time;
    private int room;

    public Session(Movie movie, String date, String time, int room) {
        this.movie = movie;
        this.date = date;
        this.time = time;
        this.room = room;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getRoom() {
        return room;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRoom(int room) {
        this.room = room;
    }
}
