package be.helha.applicine.models;

/**
 * This class represents a session.
 */
public class Session {
    private Movie movie;
    private String date;
    private String time;
    private int room;

    private String movieVersion;

    /**
     * Constructor for the session.
     * @param movie The movie of the session.
     * @param date The date of the session.
     * @param time The time of the session.
     * @param room The room of the session.
     */
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

    public String getMovieTitle() {
        return movie.getTitle();
    }

    public String getMovieVersion() {
        return movieVersion;
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
