package be.helha.applicine.models;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents a film session.
 */
public class Session {

    public int id;
    private Movie movie;
    private String time;
    private Room room;

    private String version;

    /**
     * Constructor for the session.
     *
     * @param movie The movie of the session.
     * @param time  The date and the time of the session.
     * @param room  The room of the session.
     */
    public Session(int id, Movie movie, String time, Room room, String version) {

        this.id = id;
        this.movie = movie;
        this.time = time;
        this.room = room;
        this.version = version;
    }

    public Movie getMovie() {
        return movie;
    }


    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public Room getRoom() {
        return room;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getSession() {
        return version;
    }

    public LocalDate getDate() {
        String strDate = time.split(" ")[0];
        LocalDate date = LocalDate.parse(strDate);
        return date;
    }

    public String getHourFromTime() {
        return time.split(" ")[1].split(":")[0];
    }

    public String getMinuteFromTime() {
        return time.split(" ")[1].split(":")[1];
    }
}
