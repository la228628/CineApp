package be.helha.applicine.server;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.models.HashedPassword;
import be.helha.applicine.server.dao.*;
import be.helha.applicine.server.dao.impl.*;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private MovieDAO movieDAO;
    private ClientsDAO clientsDAO;
    private TicketDAO ticketDAO;
    private RoomDAO roomDAO;
    private SessionDAO sessionDAO;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket) throws IOException {
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
                if (request.equals("GET_MOVIES")) {
                    out.writeObject(movieDAO.getAllMovies());
                }
                else if(request.toString().startsWith("GET_TICKETS_BY_CLIENT")) {
                    int clientId = Integer.parseInt(request.toString().split(" ")[1]);
                    out.writeObject(ticketDAO.getTicketsByClient(clientId));
                }
                else if (request.toString().startsWith("CHECK_LOGIN")) {
                    String[] credentials = request.toString().split(" ");
                    String username = credentials[1];
                    String password = credentials[2];
                    Client client = clientsDAO.getClientByUsername(username);
                    if (client != null && HashedPassword.checkPassword(password, client.getPassword())) {
                        out.writeObject(client);
                    } else {
                        out.writeObject(null);
                    }
                }
                else if (request instanceof Client) {
                    Client client = (Client) request;
                    String hashedPassword = HashedPassword.getHashedPassword(client.getPassword());
                    Client registeredClient = clientsDAO.createClient(client.getName(), client.getEmail(), client.getUsername(), hashedPassword);
                    if (registeredClient != null) {
                        out.writeObject("Registration successful");
                    } else {
                        out.writeObject("Registration failed");
                    }
                }
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }
}