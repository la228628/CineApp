package be.helha.applicine.controllers;

import be.helha.applicine.models.Ticket;
import be.helha.applicine.views.TicketShoppingViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class TicketPageController extends Application {

    private static double totalPrice = 0;
    private static TicketShoppingViewController controller;

    public static void main(String[] args) {
        launch();
    }

    public static void buyTickets(int normalTickets, int seniorTickets, int minorTickets, int studentTickets) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        for(int i = 0; i < normalTickets; i++){
            tickets.add(new Ticket("normal"));
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/helha/applicine/views/TicketShoppingView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        controller = loader.getController();
    }

    public static void addTicket(TextField currentTextField){
        System.out.println("add ticket");
        int currentTicketNumber = Integer.parseInt(Objects.requireNonNull(currentTextField).getText());
        currentTextField.setText(String.valueOf(++currentTicketNumber));
        addTicketPrice(currentTextField);
    }

    public static void removeTicket(TextField currentTextField){
        System.out.println("remove ticket");
        int currentTicketNumber = Integer.parseInt(Objects.requireNonNull(currentTextField).getText());
        if(currentTicketNumber > 0){
            currentTextField.setText(String.valueOf(--currentTicketNumber));
            removeTicketPrice(currentTextField);
        }
    }

    private static void removeTicketPrice(TextField currentTextField) {
        switch (Objects.requireNonNull(currentTextField).getId()){
            case "normalPlaceNumber":
                totalPrice -= 8.5;
                break;
            case "seniorPlaceNumber", "minorPlaceNumber":
                totalPrice -= 6.5;
                break;
            case "studentPlaceNumber":
                totalPrice -= 5.5;
                break;
        }
        controller.updatePrice(totalPrice);
    }

    private static void addTicketPrice(TextField currentTextField) {
        switch (Objects.requireNonNull(currentTextField).getId()){
            case "normalPlaceNumber":
                totalPrice += 8.5;
                break;
            case "seniorPlaceNumber", "minorPlaceNumber":
                totalPrice += 6.5;
                break;
            case "studentPlaceNumber":
                totalPrice += 5.5;
                break;
        }
        controller.updatePrice(totalPrice);
    }
}

