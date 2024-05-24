package be.helha.applicine.server;

import be.helha.applicine.common.models.*;
import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.common.models.request.*;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.server.dao.*;
import be.helha.applicine.server.dao.impl.*;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling the requests from the client.
 * It implements the RequestVisitor interface to handle the different types of requests from the client which implement the ClientEvent (dispatchOn) interface.
 * It extends the Thread class to handle the requests in a separate thread.
 */
public class ClientHandler extends Thread implements RequestVisitor {
    private final ObjectSocket objectSocket;
    private final MovieDAO movieDAO;
    private final ClientsDAO clientsDAO;
    private final TicketDAO ticketDAO;
    private final RoomDAO roomDAO;
    private final ViewableDAO viewableDAO;
    private final SessionDAO sessionDAO;
    private final Server server;

    /**
     * Constructor of the ClientHandler.
     * @param socket the socket of the client.
     * @param server the server.
     */
    public ClientHandler(ObjectSocket socket, Server server) {
        this.server = server;
        this.objectSocket = socket;
        this.movieDAO = new MovieDAOImpl();
        this.clientsDAO = new ClientsDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.sessionDAO = new SessionDAOImpl();
        this.ticketDAO = new TicketDAOImpl();
        this.viewableDAO = new ViewableDAOImpl();
    }

    /**
     * Method to handle the requests from the client.
     */
    public void run() {
        try {
            ClientEvent event;
            while ((event = objectSocket.read()) != null) {
                event.dispatchOn(this);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
            System.out.println("Client disconnected");
            server.getClientsConnected().remove(this);
        }
    }

    /**
     * Method to write an object to the client.
     * @param object the object to write to the client.
     */
    public void writeToClient(Object object) {
        try {
            objectSocket.write(object);
        } catch (IOException e) {
            System.out.println("Error writing to client: " + e.getMessage());
        }
    }

    /**
     * Method to broadcast an object to all clients connected to the server.
     * @param object the object to broadcast.
     */
    public void broadcast(Object object) {
        for (ClientHandler client : server.getClientsConnected()) {
            client.writeToClient(object);
        }
    }

    private void sendErrorMessage(String message) {
        writeToClient(new ErrorMessage(message));
    }

    /**
     * Method to send the list of viewables to all clients connected to the server.
     */
    public void sendViewableListToAllClients() {
        GetViewablesRequest request = new GetViewablesRequest();
        try {
            request.setViewables(viewableDAO.getAllViewables());
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        } finally {
            broadcast(request);
        }
    }

    /**
     * Visit implementation for the AddSessionRequest which adds a session to the database.
     * @param addSessionRequest the request to add a session.
     */
    @Override
    public void visit(AddSessionRequest addSessionRequest) {
        processSessionRequest(addSessionRequest, false);
    }

    /**
     * Visit implementation for the UpdateSessionRequest which updates a session in the database.
     * @param updateSessionRequest the request to update a session.
     */
    @Override
    public void visit(UpdateSessionRequest updateSessionRequest) {
        processSessionRequest(updateSessionRequest, true);
    }

    /**
     * Method to process a session request.
     * @param sessionRequest the session request to process.
     * @param isUpdate a boolean to indicate if the request is an update or not.
     */
    private void processSessionRequest(SessionRequest sessionRequest, boolean isUpdate) {
        MovieSession session = sessionRequest.getSession();
        List<Integer> sessionsWithConflict = new ArrayList<>();
        try {
            sessionsWithConflict = sessionDAO.checkTimeConflict(session.getId(), session.getRoom().getNumber(), session.getTime(), session.getViewable().getDuration());
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
            return;
        }

        if (!sessionsWithConflict.isEmpty()) {
            sessionRequest.setSuccess(false);
            sessionRequest.setConflictedSessions(sessionsWithConflict);
            sessionRequest.setMessage("Conflit de temps avec des séances existantes.");
            writeToClient(sessionRequest);
            return;
        }

        try {
            if (isUpdate) {
                sessionDAO.update(session);
            } else {
                sessionDAO.create(session);
            }
            sessionRequest.setSuccess(true);
        } catch (DaoException e) {
            sessionRequest.setSuccess(false);
            sessionRequest.setMessage(e.getMessage());
        }
        writeToClient(sessionRequest);
    }

    /**
     * Visit implementation for the GetRoomsRequest which gets all the rooms from the database.
     * @param getRoomsRequest the request to get all the rooms.
     */
    @Override
    public void visit(GetRoomsRequest getRoomsRequest) {
        try {
            getRoomsRequest.setRooms(roomDAO.getAll());
            System.out.println("Rooms: " + getRoomsRequest.getRooms());
            writeToClient(getRoomsRequest);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the DeleteViewableRequest which deletes a room from the database.
     * @param deleteViewableRequest the request to delete a room.
     */
    @Override
    public void visit(DeleteViewableRequest deleteViewableRequest) {
        try {
            int viewableId = deleteViewableRequest.getViewableId();

            ArrayList<Integer> sessionsLinkedToViewable = viewableDAO.getSeancesLinkedToViewable(viewableId);

            if (sessionsLinkedToViewable.size() > 0) {
                deleteViewableRequest.setSuccess(false);
                deleteViewableRequest.setMessage("Vous ne pouvez pas supprimer une saga si des séances lui sont attribuées.");
                writeToClient(deleteViewableRequest);
                return;
            }

            if (viewableDAO.removeViewable(viewableId)) {
                deleteViewableRequest.setSuccess(true);
                writeToClient(deleteViewableRequest);
            } else {
                deleteViewableRequest.setSuccess(false);
                deleteViewableRequest.setMessage("Erreur lors de la suppression de la saga.");
                writeToClient(deleteViewableRequest);
            }
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
        sendViewableListToAllClients();
    }

    /**
     * Visit implementation for the GetViewablesRequest which gets all the viewables from the database.
     * @param getViewablesRequest the request to get all the viewables.
     */
    @Override
    public void visit(GetViewablesRequest getViewablesRequest) {
        try {
            getViewablesRequest.setViewables(viewableDAO.getAllViewables());
            writeToClient(getViewablesRequest);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the AddViewableRequest which adds a movie to the database.
     * @param addViewableRequest the request to add a movie.
     */
    @Override
    public void visit(AddViewableRequest addViewableRequest) {
        try {
            Saga saga = (Saga) addViewableRequest.getViewable();
            ArrayList<Movie> viewables = saga.getMovies();
            ArrayList<Integer> ids = new ArrayList<>();
            for (Movie viewable : viewables) {
                ids.add(viewable.getId());
            }
            viewableDAO.addViewable(saga.getTitle(), "Saga", ids);
            addViewableRequest.setSuccess(true);
            writeToClient(addViewableRequest);
            sendViewableListToAllClients();
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the UpdateViewableRequest which updates a viewable in the database.
     * @param updateViewableRequest the request to update a viewable.
     */
    @Override
    public void visit(UpdateViewableRequest updateViewableRequest) {
        try {
            Saga saga = updateViewableRequest.getSaga();
            ArrayList<Movie> sagaMovies = saga.getMovies();
            ArrayList<Integer> ids = new ArrayList<>();
            for (Movie movie : sagaMovies) {
                ids.add(movie.getId());
            }
            viewableDAO.updateViewable(saga.getId(), saga.getTitle(), "Saga", ids);
            updateViewableRequest.setSuccess(true);
            writeToClient(updateViewableRequest);
            sendViewableListToAllClients();
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the CreateMovieRequest which gets a viewable by its id.
     * @param createMovieRequest the request to get a viewable by its id.
     */
    @Override
    public void visit(CreateMovieRequest createMovieRequest) {
        try {
            Movie movie = createMovieRequest.getMovie();
            movie.setImagePath(FileManager.createPath(removeSpecialCharacters(movie.getTitle()) + ".jpg"));
            FileManager.createImageFromBytes(movie.getImage(), movie.getImagePath());
            movieDAO.create(movie);
            createMovieRequest.setStatus(true);
            writeToClient(createMovieRequest);
            sendViewableListToAllClients();
        } catch (DaoException e) {
           sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Method to remove special characters from a string.
     * @param str the string to remove special characters from.
     * @return the string without special characters.
     */
    public static String removeSpecialCharacters(String str) {
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    /**
     * Visit implementation for the ClientRegistrationRequest which registers a client.
     * @param clientRegistrationRequest the request to register a client.
     */
    @Override
    public void visit(ClientRegistrationRequest clientRegistrationRequest) {
        try {
            Client client = clientRegistrationRequest.getClient();
            try {
                String hashedPassword = HashedPassword.getHashedPassword(client.getPassword());
                Client registeredClient = clientsDAO.create(new Client(client.getName(), client.getEmail(), client.getUsername(), hashedPassword));
                if (registeredClient != null) {
                    clientRegistrationRequest.setSuccess(true);
                } else {
                    clientRegistrationRequest.setSuccess(false);
                }
                writeToClient(clientRegistrationRequest);
            } catch (IOException e) {
                writeToClient("Error during registration: " + e.getMessage());
            }
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the DeleteMoviesRequest which deletes a movie from the database.
     * @param deleteMoviesRequest the request to delete a movie.
     */
    @Override
    public void visit(DeleteMoviesRequest deleteMoviesRequest) {
        try {
            if (movieDAO.getSessionLinkedToMovie(deleteMoviesRequest.getId()) > 0 || viewableDAO.sagasLinkedToMovie(deleteMoviesRequest.getId()) > 0) {
                deleteMoviesRequest.setStatus(false);
                deleteMoviesRequest.setMessage("Vous ne pouvez pas supprimer un film si des séances ou de sagas lui sont attribués.");
                writeToClient(deleteMoviesRequest);
                return;
            }
            try {
                movieDAO.delete(deleteMoviesRequest.getId());
                deleteMoviesRequest.setStatus(true);
                writeToClient(deleteMoviesRequest);
            } catch (Exception e) {
                deleteMoviesRequest.setStatus(false);
                deleteMoviesRequest.setMessage("Erreur lors de la suppression du film.");
                writeToClient(deleteMoviesRequest);
            }
            sendViewableListToAllClients();
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the GetAllSessionRequest which gets all the sessions from the database.
     * @param getAllSessionRequest the request to get all the sessions.
     */
    @Override
    public void visit(GetAllSessionRequest getAllSessionRequest) {
        try {
            List<MovieSession> sessions = sessionDAO.getAll();
            getAllSessionRequest.setSessions(sessions);
            writeToClient(getAllSessionRequest);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the checkLoginRequest which gets a viewable by its id.
     * @param checkLoginRequest the request to get a viewable by its id.
     */
    @Override
    public void visit(CheckLoginRequest checkLoginRequest) {
        try {
            String username = checkLoginRequest.getUsername();
            String password = checkLoginRequest.getPassword();
            Client client = clientsDAO.getClientByUsername(username);
            if (client != null && HashedPassword.checkPassword(password, client.getPassword())) {
                checkLoginRequest.setClient(client);
                writeToClient(checkLoginRequest);
            } else {
                checkLoginRequest.setClient(null);
                writeToClient(checkLoginRequest);
            }
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the GetMoviesRequest which gets all the movies from the database.
     * @param getMoviesRequest the request to get all the movies.
     */
    @Override
    public void visit(GetMoviesRequest getMoviesRequest) {
        try {
            List<Movie> movies = movieDAO.getAll();
            for (Viewable movie : movies) {
                movie.setImage(FileManager.getImageAsBytes(movie.getImagePath()));
            }
            getMoviesRequest.setMovies(movies);
            writeToClient(getMoviesRequest);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the GetSessionsByMovieId which gets all the sessions by room from the database.
     * @param getSessionByMovieId the request to get all the sessions by room.
     */
    @Override
    public void visit(GetSessionByMovieId getSessionByMovieId) {
        int movieId = getSessionByMovieId.getMovieId();
        try {
            List<MovieSession> sessions = sessionDAO.getSessionsForMovie(viewableDAO.getViewableById(movieId));
            getSessionByMovieId.setSessions(sessions);
            writeToClient(getSessionByMovieId);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the GetTicketByClientRequest which gets all the tickets by session from the database.
     * @param getTicketByClientRequest the request to get all the tickets by session.
     */
    @Override
    public void visit(GetTicketByClientRequest getTicketByClientRequest) {
        try {
            int clientId = getTicketByClientRequest.getClientId();
            List<Ticket> tickets = ticketDAO.getTicketsByClient(clientId);
            getTicketByClientRequest.setTickets(tickets);
            writeToClient(getTicketByClientRequest);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the GetSessionByIdRequest which gets a session by its id from the database.
     * @param getSessionByIdRequest the request to get a session by its id.
     */
    @Override
    public void visit(GetSessionByIdRequest getSessionByIdRequest) {
        int sessionId = getSessionByIdRequest.getSessionId();
        try {
            MovieSession session = sessionDAO.get(sessionId);
            getSessionByIdRequest.setSession(session);
            writeToClient(getSessionByIdRequest);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the CreateTicketRequest which creates a ticket in the database.
     * @param createTicketRequest the request to create a ticket.
     */
    @Override
    public void visit(CreateTicketRequest createTicketRequest) {
        try {
            Ticket ticket = createTicketRequest.getTicket();
            ticketDAO.create(ticket);
            createTicketRequest.setStatus(true);
            writeToClient(createTicketRequest);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the DeleteSessionRequest which deletes a ticket from the database.
     * @param deleteSessionRequest the request to delete a ticket.
     */
    @Override
    public void visit(DeleteSessionRequest deleteSessionRequest) {
        try {
            int sessionId = deleteSessionRequest.getSessionId();
            sessionDAO.delete(sessionId);
            deleteSessionRequest.setSuccess(true);
            writeToClient(deleteSessionRequest);
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Visit implementation for the PingServer which sends a ping to all clients when received. (Verify connection).
     * @param pingServer le ping envoyé par le client
     */
    @Override
    public void visit(PingServer pingServer) {
        System.out.println("Ping received from client");
        writeToClient(pingServer);
        System.out.println("Clients connected: " + server.getClientsConnected());
        for (ClientHandler client : server.getClientsConnected()) {
            if (client != this) {
                client.writeToClient(pingServer);
            }
        }
    }

    /**
     * Visit implementation for the UpdateMovieRequest which updates a movie in the database.
     * @param updateMovieRequest the request to update a movie.
     */
    @Override
    public void visit(UpdateMovieRequest updateMovieRequest) {
        Movie movie = updateMovieRequest.getMovie();
        try {
            if (movie.getImage() != null) {
                movie.setImagePath(FileManager.createPath(removeSpecialCharacters(movie.getTitle()) + ".jpg"));
                FileManager.createImageFromBytes(movie.getImage(), movie.getImagePath());
            }
            movieDAO.update(movie);
            updateMovieRequest.setStatus(true);
            writeToClient(updateMovieRequest);
            sendViewableListToAllClients();
        } catch (DaoException e) {
            sendErrorMessage(e.getMessage());
        }
    }
}