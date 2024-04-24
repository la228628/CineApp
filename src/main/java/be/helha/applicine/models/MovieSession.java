package be.helha.applicine.models;

import java.time.LocalDate;

/**
 * This class represents a film session.
 */
public class MovieSession {

    public int id;
    private Viewable viewable;
    private String time;
    private Room room;

    private String version;

    /**
     * Constructor for the session.
     *
     * @param viewable The movie of the session.
     * @param time  The date and the time of the session.
     * @param room  The room of the session.
     */
    public MovieSession(int id, Viewable viewable, String time, Room room, String version) {
        this.id = id;
        this.viewable = viewable;
        this.time = time;
        this.room = room;
        this.version = version;
    }

    public Viewable getViewable() {
        return viewable;
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

    public void setViewable(Movie viewable) {
        this.viewable = viewable;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getVersion() {
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
