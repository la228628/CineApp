package be.helha.applicine.models;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class represents a film session.
 */
public class Session {

    private Movie movie;
    private LocalDate date; //Model year, month, day
    private int seatsLeft = 0;
    private ArrayList<Ticket> ticketsArray = new ArrayList<>();

    /**
     * Constructor for the session.
     *
     * @param movie The movie of the session.
     * @param date  The date of the session.
     */
    public Session(Movie movie, LocalDate date) {
        this.movie = movie;
        this.date = date;
    }

    public void buyingTicket() {
        if (seatsLeft > 0) {
            seatsLeft--;
            Ticket ticket = new Ticket("normal", this,  null);
            ticketsArray.add(ticket);
        }
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    public Session(LocalDate date) {
        this(null, date);
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDate getDate() {
        return date;
    }
}
