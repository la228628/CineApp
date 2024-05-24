package be.helha.applicine.common.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public class Ticket implements Serializable {
    private Integer id;
    private final String type;
    private final Double price;
    private final String seat;
    private Client clientLinked;
    private MovieSession movieSessionLinked;
    private final String ticketVerificationCode;

    public Client getClientLinked() {
        return clientLinked;
    }

    public String getSeat() {
        return seat;
    }

    public void setClientLinked(Client clientLinked) {
        this.clientLinked = clientLinked;
    }

    public MovieSession getMovieSessionLinked() {
        return movieSessionLinked;
    }

    public void setMovieSessionLinked(MovieSession movieSessionLinked) {
        this.movieSessionLinked = movieSessionLinked;
    }

    /**
     * Constructor for the ticket.
     *
     * @param type
     * @param clientLinked
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
     * @return
     */
    private String createTicketVerificationCode() {
        StringBuilder ticketVerificationCode = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            ticketVerificationCode.append((int) Math.floor(Math.random() * 10));
            System.out.println("index " + i + " : " + ticketVerificationCode);
        }
        System.out.println(ticketVerificationCode);
        return ticketVerificationCode.toString();
    }

    /**
     * Create a seat.
     *
     * @return
     */
    private String createSeat() {
        return "A1";
    }

    public String getTicketVerificationCode() {
        return createTicketVerificationCode();
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return LocalDate.now().toString();
    }

    public int getRoom() {
        Room room = movieSessionLinked.getRoom();
        return room.getNumber();
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return movieSessionLinked.getTime();
    }

    public String getMovieTitle() {
        Viewable movie = movieSessionLinked.getViewable();
        return movie.getTitle();
    }

    public String getMovieVersion() {
        return movieSessionLinked.getVersion();
    }

    public Viewable getMovie() {
        return movieSessionLinked.getViewable();
    }
}
