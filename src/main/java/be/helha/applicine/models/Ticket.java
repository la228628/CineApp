package be.helha.applicine.models;

import java.util.Date;

public class Ticket {
    private String type;
    private double price;
    private String ticketVerificationCode;
    private String place;
    private Session sessionLinked;
    private Client clientLinked;

    public Ticket(String type, double price, String ticketVerificationCode, Date date, String place, Session sessionLinked, Client clientLinked) {
        this.type = type;
        this.price = price;
        this.ticketVerificationCode = ticketVerificationCode;
        this.place = place;
        this.sessionLinked = sessionLinked;
        this.clientLinked = clientLinked;
    }

    public String getTime() {
        return sessionLinked.getTime();
    }

    public String getDate() {
        return sessionLinked.getDate();
    }

    public String getMovieTitle() {
        return sessionLinked.getMovieTitle();
    }

    public String getPlace() {
        return place;
    }

    public String getTicketVerificationCode() {
        return ticketVerificationCode;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public Session getSessionLinked() {
        return sessionLinked;
    }

    public int getRoom() {
        return sessionLinked.getRoom();
    }

    public Client getClientLinked() {
        return clientLinked;
    }

    public String getMovieVersion() {
        return sessionLinked.getMovieVersion();
    }
}
