package be.helha.applicine.client.controllers;

import be.helha.applicine.client.network.ReadResponseThread;
import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.*;
import be.helha.applicine.common.models.request.*;
import be.helha.applicine.server.dao.SessionDAO;
import be.helha.applicine.client.views.TicketShoppingViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TicketPageController extends Application implements TicketShoppingViewController.TicketViewListener, RequestVisitor, ServerRequestHandler.Listener {

    private final MasterApplication parentController;
    private int clientID;
    private Viewable viewable;
    private SessionDAO sessionDAO;
    private MovieSession selectedSession;

    private ServerRequestHandler serverRequestHandler;
    TicketShoppingViewController controller;

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TicketShoppingViewController.class.getResource("TicketShoppingView.fxml"));
        Scene scene;
        serverRequestHandler = ServerRequestHandler.getInstance();
        serverRequestHandler.addListener(this);
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setTitle("Ticket Shopping");
            stage.setScene(scene);
            stage.show();
            controller = fxmlLoader.getController();
            controller.setListener(this);
            controller.setMovie(viewable);
            System.out.println("Viewable: " + viewable + " ID: " + viewable.getId() + " Title: " + viewable.getTitle());

            // Récupérer les séances du film et les définir dans la vue.
            getSessionsForMovie(viewable);
        } catch (IOException e) {
            AlertViewController.showErrorMessage("Error loading ticket shopping view: " + e.getMessage());
            parentController.toClient();
        }
    }

    public TicketPageController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
        Session currentSession = parentController.getSession();
        Client client = currentSession.getCurrentClient();
        this.clientID = client.getId();
    }

    public void createTickets(int numberOfTickets, String ticketType) {
        try {
            for (int i = 0; i < numberOfTickets; i++) {
                Session currentSession = parentController.getSession();
                Client client = currentSession.getCurrentClient();
                clientID = client.getId();
                Ticket ticket = new Ticket(ticketType, selectedSession, client);
                CreateTicketRequest request = new CreateTicketRequest(ticket);
                serverRequestHandler.sendRequest(request);
            }
        } catch(IOException e){

        }
    }

    @Override
    public void buyTickets(String sessionId, int normalTickets, int seniorTickets, int minorTickets, int studentTickets) {
        onSessionSelected(sessionId); //Session jamais null sinon le prog plante dans la vue déjà ==> supp fonction??
        if (selectedSession == null) {
            System.out.println("No session selected");
            return;
        }
        createTickets(normalTickets, "normal");
        createTickets(seniorTickets, "senior");
        createTickets(minorTickets, "child");
        createTickets(studentTickets, "student");
    }

    @Override
    public void onSessionSelected(String sessionId) {
        // Assume sessionId is a valid integer string that represents the ID of the session
        try {
            int id = Integer.parseInt(sessionId);
            GetSessionByIdRequest request = new GetSessionByIdRequest(id);
            serverRequestHandler.sendRequest(request);
            //selectedSession = serverRequestHandler.sendRequest(request);
        } catch (NumberFormatException |IOException e) {
            System.out.println("Invalid session ID: " + sessionId);
            AlertViewController.showErrorMessage("Session sélectionnée n'existe pas.");
        }
    }

    public void setViewable(Viewable viewable) {
        this.viewable = viewable;
    }

    public void closeWindow() {
        parentController.toClient();
    }

    public void getSessionsForMovie(Viewable movie) {
        try {
            System.out.println("Getting sessions for movie: " + movie.getId());
            GetSessionByMovieId request = new GetSessionByMovieId(movie.getId());
            serverRequestHandler.sendRequest(request);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    @Override
    public void onResponseReceive(ClientEvent response) {
        response.dispatchOn(this);
    }

    @Override
    public void onConnectionLost() {

    }
    @Override
    public void visit(CreateTicketRequest createTicketRequest){
        if (createTicketRequest.getStatus()) {
            System.out.println("Ticket created successfully");
        } else {
            System.out.println("Error creating ticket");
            AlertViewController.showErrorMessage("Erreur de connection à la base de données");
        }
    }

    @Override
    public void visit(GetSessionByIdRequest getSessionByIdRequest){
        selectedSession = getSessionByIdRequest.getSession();
        if(selectedSession == null){
            AlertViewController.showErrorMessage("Session not found");
        }
    }
    @Override
    public void visit(GetSessionByMovieId getSessionByMovieId){
        List<MovieSession> sessions = getSessionByMovieId.getSessions();
        if (sessions.isEmpty()) {
            // Afficher un message à l'utilisateur
            AlertViewController.showInfoMessage("No sessions available for this movie.");
            parentController.toClient();
        } else {
            controller.setSessions(sessions);
        }
    }

}