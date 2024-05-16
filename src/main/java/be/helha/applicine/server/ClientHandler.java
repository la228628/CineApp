package be.helha.applicine.server;

import be.helha.applicine.common.models.*;
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
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void run() {
        try {
            Object request;
            while ((request = in.readObject()) != null) {
                handleRequest(request);
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleRequest(Object request) throws IOException, SQLException {
        if (request.equals("GET_MOVIES")) {
            handleGetMovies();
        } else if (request.toString().startsWith("GET_TICKETS_BY_CLIENT")) {
            handleGetTicketsByClient(request.toString());
        } else if (request.toString().startsWith("CHECK_LOGIN")) {
            handleCheckLogin(request.toString());
        } else if (request instanceof Client) {
            handleClientRegistration((Client) request);
        } else if (request.toString().startsWith("GET_SESSIONS_BY_MOVIE")) {
            handleGetSessionsByMovie(request.toString());
        } else if (request.toString().startsWith("GET_SESSION")) {
            handleGetSession(request.toString());
        } else if (request.toString().startsWith("CREATE_TICKET")) {
            handleCreateTicket(request.toString());
        }
    }

    private void handleGetMovies() throws IOException, SQLException {
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