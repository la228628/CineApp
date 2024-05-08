package be.helha.applicine.controllers;

import be.helha.applicine.dao.SessionDAO;
import be.helha.applicine.dao.TicketDAO;
import be.helha.applicine.dao.impl.SessionDAOImpl;
import be.helha.applicine.dao.impl.TicketDAOImpl;
import be.helha.applicine.models.*;
import be.helha.applicine.views.TicketShoppingViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TicketPageController extends Application implements TicketShoppingViewController.TicketViewListener {

    private final MasterApplication parentController;
    private TicketDAO ticketDAO;
    private int clientID;
    private Visionable movie;
    private SessionDAO sessionDAO;
    private MovieSession selectedSession;

    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(TicketShoppingViewController.class.getResource("TicketShoppingView.fxml"));
        Scene scene;
        try{
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
                controller.showNoSessionsAlert();
                stage.close();
            } else {
                controller.setSessions(sessions);
            }
        }catch (IOException e){
            parentController.popUpAlert("Erreur lors de l'affichage de la fenêtre de réservation de tickets");
        }catch (SQLException e){
            parentController.popUpAlert("Erreur lors de la récupération des séances du film, veuillez relancer l'application. Si le problème persiste contactez un administrateur.");
            parentController.closeAllWindows();
            parentController.toClient();
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

    public void popUpAlert(String message) {
        parentController.popUpAlert(message);
    }

    @Override
    public void onSessionSelected(String sessionId) {
        // Assume sessionId is a valid integer string that represents the ID of the session
        try {
            int id = Integer.parseInt(sessionId);
            selectedSession = sessionDAO.getSessionById(id);
        } catch (NumberFormatException e) {
            parentController.popUpAlert("La session sélectionnée est invalide! Réessayé ultérieurement.");
            parentController.toClient();
        } catch (SQLException e) {
            parentController.popUpAlert("Erreur lors de la récupération de la session sélectionnée! Réessayé ultérieurement.");
            parentController.toClient();
        }
    }

    public void setMovie(Visionable movie) {
        this.movie = movie;
    }

    public void closeWindow() {
        parentController.toClient();
    }

    public List<MovieSession> getSessionsForMovie(Visionable movie) throws SQLException {
        return sessionDAO.getSessionsForMovie(movie);
    }
}

