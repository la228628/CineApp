package be.helha.applicine.models;

import javafx.scene.layout.Pane;

import java.util.Date;

public class Ticket {
    private String type;
    private double price;
    private String ticketVerificationCode;
    private Date date;
    private String place;
    private Movie movieLinked;
    private Client clientLinked;

    public Ticket(String type, double price, String ticketVerificationCode, Date date, String place, Movie movieLinked, Client clientLinked) {
        this.type = type;
        this.price = price;
        this.ticketVerificationCode = ticketVerificationCode;
        this.date = date;
        this.place = place;
        this.movieLinked = movieLinked;
        this.clientLinked = clientLinked;
    }

}
