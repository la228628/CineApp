package be.helha.applicine.client.controllers;

import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.*;
import be.helha.applicine.common.models.request.*;
import be.helha.applicine.server.dao.SessionDAO;
import be.helha.applicine.client.views.TicketShoppingViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the ticket shopping ticket window.
 */
public class TicketPageController extends Application implements TicketShoppingViewController.TicketViewListener, RequestVisitor, ServerRequestHandler.Listener {

    private final MasterApplication parentController;
    private int clientID;
    private Viewable viewable;
    private SessionDAO sessionDAO;
    private MovieSession selectedSession;

    List<MovieSession> sessions;
    private ServerRequestHandler serverRequestHandler;
    TicketShoppingViewController controller;

    /**
     * Starts the ticket shopping window.
     *
     * @param stage
     * @throws IOException
     */
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

    /**
     * Constructor for the TicketPageController.
     * set the parentController to the masterApplication.
     * get the current client and session.
     * @param masterApplication
     */
    public TicketPageController(MasterApplication masterApplication) {
        this.parentController = masterApplication;
        Session currentSession = parentController.getSession();
        Client client = currentSession.getCurrentClient();
        this.clientID = client.getId();
    }

    /**
     * Create tickets for the selected session.
     * Send a request to the server to create the tickets.
     *
     * @param numberOfTickets
     * @param ticketType
     */

    public void createTickets(int numberOfTickets, String ticketType) {
        try {
            for (int i = 0; i < numberOfTickets; i++) {
                Session currentSession = parentController.getSession();
                Client client = currentSession.getCurrentClient();
                clientID = client.getId();
                Ticket ticket = new Ticket(ticketType, selectedSession, client);
                CreateTicketRequest request = new CreateTicketRequest(ticket);
                serverRequestHandler.sendRequest(request);
                System.out.println("testtttt");
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Buy tickets for the selected session.
     *
     * @param sessionId
     * @param normalTickets
     * @param seniorTickets
     * @param minorTickets
     * @param studentTickets
     */
    @Override
    public void buyTickets(String sessionId, int normalTickets, int seniorTickets, int minorTickets, int studentTickets) {
        onSessionSelected(sessionId);
        if (selectedSession.getId() == 0) {
            System.out.println("No session selected");
            return;
        }
        System.out.println("Buying tickets for session: " + selectedSession.getId());
        createTickets(normalTickets, "normal");
        createTickets(seniorTickets, "senior");
        createTickets(minorTickets, "child");
        createTickets(studentTickets, "student");
    }

    /**
     * Select a session.
     * Create a MovieSession object from the session ID and set it as the selected session.
     *
     * @param sessionId
     */

    @Override
    public void onSessionSelected(String sessionId) {
        try {
            MovieSession session = sessions.stream().filter(s -> s.getId() == Integer.parseInt(sessionId)).findFirst().orElse(null);
            if (session != null) {
                selectedSession = session;
                System.out.println("Selected session: " + selectedSession.getId());
            } else {
                System.out.println("Session not found: " + sessionId);
                AlertViewController.showErrorMessage("Session sélectionnée n'existe pas.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid session ID: " + sessionId);
            AlertViewController.showErrorMessage("Session sélectionnée n'existe pas.");
        }
    }

    /**
     * Set the viewable object.
     *
     * @param viewable
     */
    public void setViewable(Viewable viewable) {
        this.viewable = viewable;
    }

    /**
     * Close the ticket shopping window.
     */
    public void closeWindow() {
        parentController.toClient();
    }

    /**
     * Get the sessions for the selected movie.
     * Send a request to the server to get the sessions.
     *
     * @param movie
     */

    public void getSessionsForMovie(Viewable movie) {
        try {
            System.out.println("Getting sessions for movie: " + movie.getId());
            GetSessionByMovieId request = new GetSessionByMovieId(movie.getId());
            serverRequestHandler.sendRequest(request);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    /**
     * Handle the response from the server.
     * And dispatch it to the appropriate method.
     */

    @Override
    public void onResponseReceive(ClientEvent response) {
        response.dispatchOn(this);
    }

    /**
     * Handle the create ticket request.
     * If the ticket was created successfully, show a success message.
     * If the ticket was not created successfully, show an error message.
     * @param createTicketRequest
     */
    @Override
    public void visit(CreateTicketRequest createTicketRequest){
        if (createTicketRequest.getStatus()) {
            System.out.println("Ticket created successfully");
        } else {
            System.out.println("Error creating ticket");
            AlertViewController.showErrorMessage("Erreur de connection à la base de données");
        }
    }

    /**
     * Handle the get session by ID request.
     * Set the selected session.
     * @param getSessionByIdRequest
     */
    @Override
    public void visit(GetSessionByIdRequest getSessionByIdRequest){
        selectedSession = getSessionByIdRequest.getSession();
        if(selectedSession == null){
            AlertViewController.showErrorMessage("Session not found");
        }
    }

    /**
     * Handle the get session by movie ID request.
     * Set the sessions.
     * If there are no sessions available, show an info message.
     * @param getSessionByMovieId
     */
    @Override
    public void visit(GetSessionByMovieId getSessionByMovieId){
        Platform.runLater(() -> {
            List<MovieSession> sessions = getSessionByMovieId.getSessions();
            if (sessions.isEmpty()) {
                AlertViewController.showInfoMessage("No sessions available for this movie.");
                parentController.toClient();
            } else {
                this.sessions = sessions;
                controller.setSessions(sessions);
            }
        });
    }
    @Override
    public void visit(ErrorMessage errorMessage) {
        Platform.runLater(() -> {
            AlertViewController.showErrorMessage(errorMessage.getMessage());
        });
    }
}