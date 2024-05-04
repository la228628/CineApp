package be.helha.applicine.client.controllers;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Session;
import be.helha.applicine.common.models.Visionable;
import be.helha.applicine.server.dao.SessionDAO;
import be.helha.applicine.server.dao.TicketDAO;
import be.helha.applicine.server.dao.impl.SessionDAOImpl;
import be.helha.applicine.server.dao.impl.TicketDAOImpl;
import be.helha.applicine.client.views.TicketShoppingViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class TicketPageController extends Application implements TicketShoppingViewController.TicketViewListener {

    private final MasterApplication parentController;
    private TicketDAO ticketDAO;
    private int clientID;
    private Visionable movie;
    private SessionDAO sessionDAO;
    private MovieSession selectedSession;

    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TicketShoppingViewController.class.getResource("TicketShoppingView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ticket Shopping");
        stage.setScene(scene);
        stage.show();
        TicketShoppingViewController controller = fxmlLoader.getController();
        controller.setListener(this);
        controller.setMovie(movie);

        // Récupérer les séances du film et les définir dans la vue.
        List<MovieSession> sessions = getSessionsForMovie(movie);
        if (sessions.isEmpty()) {
            // Afficher un message à l'utilisateur
            controller.showNoSessionsAlert();
            stage.close();
        } else {
            controller.setSessions(sessions);
        }
    }

    public TicketPageController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
        this.ticketDAO = new TicketDAOImpl();
        this.sessionDAO = new SessionDAOImpl();
        Session currentSession = parentController.getSession();
        Client client = currentSession.getCurrentClient();
        this.clientID = client.getId();
    }

    public void createTickets(int numberOfTickets, String ticketType, int price) {
        for (int i = 0; i < numberOfTickets; i++) {
            ticketDAO.addTicket(clientID, selectedSession.getId(), ticketType, "A" + i, price, "1234");
            System.out.println("Ticket created");
        }
    }

    @Override
    public void buyTickets(String sessionId, int normalTickets, int seniorTickets, int minorTickets, int studentTickets) {
        onSessionSelected(sessionId);
        if (selectedSession == null) {
            System.out.println("No session selected");
            return;
        }

        createTickets(normalTickets, "normal", 8);
        createTickets(seniorTickets, "senior", 6);
        createTickets(minorTickets, "minor", 5);
        createTickets(studentTickets, "student", 4);
    }

    @Override
    public void onSessionSelected(String sessionId) {
        // Assume sessionId is a valid integer string that represents the ID of the session
        try {
            int id = Integer.parseInt(sessionId);
            selectedSession = sessionDAO.getSessionById(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid session ID: " + sessionId);
        }
    }

    public void setMovie(Visionable movie) {
        this.movie = movie;
    }

    public List<MovieSession> getSessionsForMovie(Visionable movie) {
        return sessionDAO.getSessionsForMovie(movie);
    }
}

