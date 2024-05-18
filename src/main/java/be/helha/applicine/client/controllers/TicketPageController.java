package be.helha.applicine.client.controllers;

import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.*;
import be.helha.applicine.common.models.request.GetAllSessionRequest;
import be.helha.applicine.common.models.request.GetSessionByIdRequest;
import be.helha.applicine.common.models.request.GetSessionByMovieId;
import be.helha.applicine.server.dao.SessionDAO;
import be.helha.applicine.client.views.TicketShoppingViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TicketPageController extends Application implements TicketShoppingViewController.TicketViewListener {

    private final MasterApplication parentController;
    private int clientID;
    private Viewable movie;
    private SessionDAO sessionDAO;
    private MovieSession selectedSession;

    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(TicketShoppingViewController.class.getResource("TicketShoppingView.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
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
                AlertViewController.showInfoMessage("No sessions available for this movie.");
                stage.close();
            } else {
                controller.setSessions(sessions);
            }
        } catch (IOException e) {
            AlertViewController.showErrorMessage("Error loading ticket shopping view: " + e.getMessage());
        } catch (SQLException e) {
            AlertViewController.showErrorMessage("Error getting sessions for movie: " + e.getMessage());
            parentController.closeAllWindows();
            parentController.toClient();
        }
    }

    public TicketPageController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
        Session currentSession = parentController.getSession();
        Client client = currentSession.getCurrentClient();
        this.clientID = client.getId();
    }

    public void createTickets(int numberOfTickets, String ticketType, int price) {
        for (int i = 0; i < numberOfTickets; i++) {
            Session currentSession = parentController.getSession();
            ServerRequestHandler serverRequestHandler = parentController.getServerRequestHandler();
            try {
                Object response = serverRequestHandler.sendRequest("CREATE_TICKET " + clientID + " " + selectedSession.getId() + " " + ticketType + " " + price);
                if (response.equals("TICKET_CREATED")) {
                    System.out.println("Ticket created successfully");
                } else {
                    System.out.println("Error creating ticket: " + response);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error creating ticket: " + e.getMessage());
            }
        }
    }

    @Override
    public void buyTickets(String sessionId, int normalTickets, int seniorTickets, int minorTickets, int studentTickets) {
        onSessionSelected(sessionId); //Session jamais null sinon le prog plante dans la vue déjà ==> supp fonction??
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
            ServerRequestHandler serverRequestHandler = parentController.getServerRequestHandler();
            GetSessionByIdRequest request = new GetSessionByIdRequest(id);
            selectedSession = (MovieSession) serverRequestHandler.sendRequest(request);
        } catch (NumberFormatException | IOException | ClassNotFoundException e) {
            System.out.println("Invalid session ID: " + sessionId);
        }
    }

    public void setMovie(Viewable movie) {
        this.movie = movie;
    }

    public void closeWindow() {
        parentController.toClient();
    }

    public List<MovieSession> getSessionsForMovie(Viewable movie) throws SQLException {
        ServerRequestHandler serverRequestHandler = parentController.getServerRequestHandler();
        GetSessionByMovieId request = new GetSessionByMovieId(movie.getId());
        try {
            return (List<MovieSession>) serverRequestHandler.sendRequest(request);
        } catch (Exception e) {
            System.out.println("Error getting sessions for movie: " + e.getMessage());
            return null;
        }
    }
}