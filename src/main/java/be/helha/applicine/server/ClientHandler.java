package be.helha.applicine.server;

import be.helha.applicine.common.models.*;
import be.helha.applicine.common.models.request.ClientEvent;
import be.helha.applicine.common.models.request.DeleteMoviesRequest;
import be.helha.applicine.server.dao.*;
import be.helha.applicine.server.dao.impl.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private MovieDAO movieDAO;
    private ClientsDAO clientsDAO;
    private TicketDAO ticketDAO;
    private RoomDAO roomDAO;

    private ViewableDAO viewableDAO;
    private SessionDAO sessionDAO;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket) throws IOException, SQLException {
        this.clientSocket = socket;
        this.movieDAO = new MovieDAOImpl();
        this.clientsDAO = new ClientsDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.sessionDAO = new SessionDAOImpl();
        this.ticketDAO = new TicketDAOImpl();
        this.viewableDAO = new ViewableDAOImpl();
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void run() {
        try {
            ClientEvent event;
            while ((event = (ClientEvent) in.readObject()) != null) {
                handleRequest(event);
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleRequest(ClientEvent event) throws IOException, SQLException {
        //le this sera le clientHandler
        event.dispatchOn(this);
        //        if (request.equals("GET_MOVIES")) {
//            handleGetMovies();
//        } else if (request.toString().startsWith("DELETE_MOVIE")) {
//            handleDeleteMovie(request.toString());
//        } else if (request.toString().startsWith("GET_TICKETS_BY_CLIENT")) {
//            handleGetTicketsByClient(request.toString());
//        } else if (request.toString().startsWith("CHECK_LOGIN")) {
//            handleCheckLogin(request.toString());
//        } else if (request instanceof Client) {
//            handleClientRegistration((Client) request);
//        } else if (request.toString().startsWith("GET_SESSIONS_BY_MOVIE")) {
//            handleGetSessionsByMovie(request.toString());
//        } else if (request.toString().startsWith("GET_SESSIONS")) {
//            handleGetSessions();
//        } else if (request.toString().startsWith("GET_SESSION")) {
//            handleGetSession(request.toString());
//        } else if (request.toString().startsWith("CREATE_TICKET")) {
//            handleCreateTicket(request.toString());
//        } else if (request instanceof Movie) {
//            handleAddMovie((Movie) request);
//        } else if (request.toString().startsWith("GET_MOVIE_BY_ID")) {
//            handleGetMovieById(request.toString());
//        } else if (request.toString().startsWith("SESSIONS_LINKED_TO_MOVIE")) {
//            handleGetSessionsLinkedToMovie(request.toString());
//        } else if (request.toString().startsWith("SAGAS_LINKED_TO_MOVIE")) {
//            handleGetSagasLinkedToMovie(request.toString());
//        } else if (request instanceof Saga) {
//            Saga saga = (Saga) request;
//            if (saga != null && saga.getId() > 0) {
//                handleUpdateViewable(saga);
//            } else {
//                handleAddViewable(saga);
//            }
//        } else if (request.toString().startsWith("GET_VIEWABLES")) {
//            handleGetViewables();
//        } else if (request.toString().startsWith("DELETE_VIEWABLE")) {
//            handleDeleteViewable(request.toString());
//        } else if (request.toString().startsWith("GET_ROOMS")) {
//            handleGetRooms();
//        } else if (request instanceof MovieSession) {
//            MovieSession session = (MovieSession) request;
//            if (session.getId() > 0) {
//                // Update the existing Session
//                handleUpdateSession(session);
//            } else {
//                // Add a new Session
//                handleAddSession(session);
//            }
//        } else if (request.toString().startsWith("DELETE_SESSION")) {
//            handleDeleteSession(request.toString());
//        } else if (request.toString().startsWith("GET_ROOM_BY_ID")) {
//            handleGetRoomById(request.toString());
//        }
    }

    private void handleGetRoomById(String string) {
        int id = Integer.parseInt(string.split(" ")[1]);
        try {
            Room room = roomDAO.getRoomById(id);
            out.writeObject(room);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleDeleteMovie(Integer id) {
        try {
            movieDAO.removeMovie(id);
            out.writeObject("MOVIE_DELETED");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteSession(String string) {
        int id = Integer.parseInt(string.split(" ")[1]);
        try {
            sessionDAO.removeSession(id);
            out.writeObject("SESSION_DELETED");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddSession(MovieSession session) {
        try {
            sessionDAO.addSession(session.getViewable().getId(), session.getRoom().getNumber(), session.getTime(), session.getVersion());
            out.writeObject("SESSION_ADDED");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdateSession(MovieSession session) throws SQLException {
        sessionDAO.updateSession(session.getId(), session.getViewable().getId(), session.getRoom().getNumber(), session.getTime(), session.getVersion());
        try {
            out.writeObject("SESSION_UPDATED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetRooms() {
        try {
            out.writeObject(roomDAO.getAllRooms());
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetSessions() {
        try {
            out.writeObject(sessionDAO.getAllSessions());
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteViewable(String string) {
        int id = Integer.parseInt(string.split(" ")[1]);
        if (viewableDAO.removeViewable(id)) {
            try {
                out.writeObject("VIEWABLE_DELETED");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                out.writeObject("VIEWABLE_NOT_DELETED");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleGetViewables() {
        try {
            out.writeObject(viewableDAO.getAllViewables());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddViewable(Saga saga) {
        ArrayList<Movie> viewables = saga.getMovies();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Movie viewable : viewables) {
            ids.add(viewable.getId());
        }
        viewableDAO.addViewable(saga.getTitle(), "Saga", ids);
        try {
            out.writeObject("VIEWABLE_ADDED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleUpdateViewable(Saga saga) {
        viewableDAO.updateViewable(saga.getId(), saga.getTitle(), "Saga", new ArrayList<>());
        try {
            out.writeObject("VIEWABLE_UPDATED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetSagasLinkedToMovie(String string) {
        int movieId = Integer.parseInt(string.split(" ")[1]);
        int sagas = viewableDAO.sagasLinkedToMovie(movieId);
        try {
            out.writeObject(sagas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetSessionsLinkedToMovie(String string) {
        int movieId = Integer.parseInt(string.split(" ")[1]);
        int amountSessions = movieDAO.sessionLinkedToMovie(movieId);
        try {
            out.writeObject(amountSessions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetMovieById(String string) {
        int id = Integer.parseInt(string.split(" ")[1]);
        try {
            Movie movie = movieDAO.getMovieById(id);
            movie.setImage(getImageAsBytes(movie.getImagePath()));
            out.writeObject(movie);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddMovie(Movie request) throws IOException {
        if (request.getId() == 0) {
            movieDAO.addMovie(request);
            out.writeObject("MOVIE_ADDED");
        } else if (request.getId() > 0) {
            movieDAO.updateMovie(request);
            out.writeObject("MOVIE_UPDATED");
        } else {
            out.writeObject("MOVIE_NOT_ADDED");
        }
    }

    public void handleGetMovies() throws IOException, SQLException {
        List<Movie> movies = movieDAO.getAllMovies();
        for (Viewable movie : movies) {
            movie.setImage(getImageAsBytes(movie.getImagePath()));
        }
        out.writeObject(movies);
    }

    public byte[] getImageAsBytes(String imagePath) throws IOException {
        if (imagePath.startsWith("file:")) {
            imagePath = imagePath.substring(5); // Remove the "file:" scheme
        }
        return Files.readAllBytes(Paths.get(imagePath));
    }

    private void handleGetTicketsByClient(String request) throws IOException {
        int clientId = Integer.parseInt(request.split(" ")[1]);
        out.writeObject(ticketDAO.getTicketsByClient(clientId));
    }

    private void handleCheckLogin(String request) throws IOException {
        String[] credentials = request.split(" ");
        String username = credentials[1];
        String password = credentials[2];
        Client client = clientsDAO.getClientByUsername(username);
        if (client != null && HashedPassword.checkPassword(password, client.getPassword())) {
            out.writeObject(client);
        } else {
            out.writeObject(null);
        }
    }

    private void handleClientRegistration(Client client) throws IOException {
        String hashedPassword = HashedPassword.getHashedPassword(client.getPassword());
        Client registeredClient = clientsDAO.createClient(client.getName(), client.getEmail(), client.getUsername(), hashedPassword);
        if (registeredClient != null) {
            out.writeObject("Registration successful");
        } else {
            out.writeObject("Registration failed");
        }
    }

    private void handleGetSessionsByMovie(String request) throws IOException, SQLException {
        int movieId = Integer.parseInt(request.split(" ")[1]);
        System.out.println("Movie ID: " + movieId);
        out.writeObject(sessionDAO.getSessionsForMovie(movieDAO.getMovieById(movieId)));
    }

    private void handleGetSession(String request) throws IOException, SQLException {
        int sessionId = Integer.parseInt(request.split(" ")[1]);
        out.writeObject(sessionDAO.getSessionById(sessionId));
    }

    private void handleCreateTicket(String request) throws IOException {
        String[] parts = request.split(" ");
        int clientId = Integer.parseInt(parts[1]);
        int sessionId = Integer.parseInt(parts[2]);
        String ticketType = parts[3];
        double price = Double.parseDouble(parts[4]);
        String seatCode = "A1"; // This can be changed to a dynamic value if needed
        String verificationCode = "123456789"; // This can be changed to a dynamic value if needed
        ticketDAO.addTicket(clientId, sessionId, ticketType, seatCode, price, verificationCode);
        out.writeObject("TICKET_CREATED");
    }
}