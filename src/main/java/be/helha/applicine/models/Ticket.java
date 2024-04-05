package be.helha.applicine.models;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class Ticket {
    private String type;
    private double price;
    private String seat;

    private Session sessionLinked; //May be removed later on

    private Client clientLinked;

    public Ticket(String type, Movie movieLinked, Client clientLinked) {
        this.type = verifyType(type);
        this.price = setPrice();
        this.seat = createSeat();
        this.clientLinked = clientLinked;
    }
    public Ticket(String type) {
        this(type, null, null);
    }
    private String verifyType(@NotNull String inputType){
        return switch (inputType) {
            case "student", "senior", "child", "normal" -> inputType;
            default -> throw new IllegalArgumentException("Invalid ticket type");
        };
    }
    @NotNull
    private LocalDate createDate(){
        LocalDate myObj = LocalDate.now(); // Create a date object
        System.out.println(myObj); // Display the current date
        return myObj;
    }
    private double setPrice(){
        return switch (type) {
            case "student", "senior" -> 6.5;
            case "child" -> 5.5;
            default -> 8.5;
        };
    }
    private String createTicketVerificationCode(){
        StringBuilder ticketVerificationCode = new StringBuilder();
        for(int i = 0; i < 15; i++){
            ticketVerificationCode.append((int) Math.floor(Math.random() * 10));
            System.out.println("index " + i + " : " + ticketVerificationCode);
        }
        System.out.println(ticketVerificationCode);
        return ticketVerificationCode.toString();
    }
    private String createSeat(){
        return "A1";
    }
    public boolean verifyExpirationDate(){
        return LocalDate.now().isBefore(sessionLinked.getDate());
    }
    public String getTicketVerificationCode() {
        return createTicketVerificationCode();
    }
    public double getPrice() {
        return price;
    }
}
