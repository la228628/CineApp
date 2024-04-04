package be.helha.applicine.models;

import java.time.LocalDate;

/**
 * This class represents a session.
 */
public class Session {
    private Movie movie;
    /**
     * Modèle de données : année/mois/jour
     */
    private LocalDate date;
    private String time;

    /**
     * Constructor for the session.
     * @param movie The movie of the session.
     * @param date The date of the session.
     * @param time The time of the session.
     */
    public Session(Movie movie, LocalDate date, String time) {
        this.movie = movie;
        this.date = date;
        this.time = time;
    }
    public Session(LocalDate date) {
        this(null, date, null);
    }

    public LocalDate getDate() {
        return date;
    }
}
