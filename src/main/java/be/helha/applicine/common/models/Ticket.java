package be.helha.applicine.common.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public class Ticket implements Serializable {
    private Integer id;
    private final String type;
    private final Double price;
    private final String seat;
    private final Client clientLinked;
    private final MovieSession movieSessionLinked;
    private final String ticketVerificationCode;

    /**
     * Get the client linked to the ticket.
     * @return The client linked to the ticket.
     */
    public Client getClientLinked() {
        return clientLinked;
    }

    /**
     * Get the seat of the ticket.
     * @return The seat of the ticket.
     */

    public String getSeat() {
        return seat;
    }

    /**
     * Set the client linked to the ticket.
     * @return The client linked to the ticket.
     */

    public MovieSession getMovieSessionLinked() {
        return movieSessionLinked;
    }


    /**
     * Constructor for the ticket.
     *
     * @param type        The type of the ticket.
     * @param clientLinked The client linked to the ticket.
     */

    public Ticket(String type, MovieSession session, Client clientLinked) {
        this.type = verifyType(type);
        this.price = setPriceByType();
        this.seat = createSeat();
        this.clientLinked = clientLinked;
        this.movieSessionLinked = session;
        this.ticketVerificationCode = createTicketVerificationCode();
    }

    public Ticket(int id, Client client, MovieSession movieSessionLinked, String ticketType, String seatCode, double price, String ticketVerificationCode) {
        this.id = id;
        this.clientLinked = client;
        this.type = ticketType;
        this.price = price;
        this.seat = seatCode;
        this.movieSessionLinked = movieSessionLinked;
        this.ticketVerificationCode = ticketVerificationCode;
    }

    private String verifyType(@NotNull String inputType) {
        return switch (inputType) {
            case "student", "senior", "child", "normal" -> inputType;
            default -> throw new IllegalArgumentException("Invalid ticket type");
        };
    }

    private double setPriceByType() {
        return switch (type) {
            case "student", "senior" -> 6.5;
            case "child" -> 5.5;
            default -> 8.5;
        };
    }

    /**
     * Create a ticket verification code.
     *
     * @return The ticket verification code.
     */
    private String createTicketVerificationCode() {
        StringBuilder ticketVerificationCode = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            ticketVerificationCode.append((int) Math.floor(Math.random() * 10));
        }
        return ticketVerificationCode.toString();
    }

    /**
     * Create a seat.
     *
     * @return The seat.
     */
    private String createSeat() {
        return "A1";
    }

    /**
     * Get the ticket verification code.
     * @return The ticket verification code.
     */

    public String getTicketVerificationCode() {
        return createTicketVerificationCode();
    }

    /**
     * Get the price of the ticket.
     * @return The price of the ticket.
     */

    public double getPrice() {
        return price;
    }

    /**
     * Get the date of the ticket.
     * @return The date of the ticket.
     */

    public String getDate() {
        return LocalDate.now().toString();
    }

    /**
     * Get the room of the ticket.
     * @return The room of the ticket.
     */
    public int getRoom() {
        Room room = movieSessionLinked.getRoom();
        return room.getNumber();
    }

    /**
     * Get the movie of the ticket.
     * @return The movie of the ticket.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the type of the ticket.
     *
     * @return The type of the ticket.
     */

    public String getType() {
        return type;
    }

    /**
     * Get the time of the session.
     * @return The time of the session.
     */

    public String getTime() {
        return movieSessionLinked.getTime();
    }

    /**
     * Get the title of the movie.
     * @return The title of the movie.
     */
    public String getMovieTitle() {
        Viewable movie = movieSessionLinked.getViewable();
        return movie.getTitle();
    }

    /**
     * Get the version of the movie.
     * @return The version of the movie.
     */

    public String getMovieVersion() {
        return movieSessionLinked.getVersion();
    }

    /**
     * Get the viewable.
     * @return The viewable of the ticket.
     */

    public Viewable getMovie() {
        return movieSessionLinked.getViewable();
    }
}
