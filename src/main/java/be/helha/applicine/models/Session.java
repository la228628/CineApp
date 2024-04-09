package be.helha.applicine.models;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class represents a film session.
 */
public class Session {
    private final Movie movie;
    private final LocalDate date; //Model year, month, day
    private int seatsLeft = 0;
    private final ArrayList<Ticket> ticketsArray = new ArrayList<>();

    /**
     * Constructor for the session.
     *
     * @param movie The movie of the session.
     * @param date  The date of the session.
     * @param time  The time of the session.
     */
    public Session(Movie movie, LocalDate date, String time) {
        this.movie = movie;
        this.date = date;
    }

    public void buyingTicket() {
        if (seatsLeft > 0) {
            seatsLeft--;
            Ticket ticket = new Ticket("normal", movie, null);
            ticketsArray.add(ticket);
        }
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    public Session(LocalDate date) {
        this(null, date, null);
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDate getDate() {
        return date;
    }
}
