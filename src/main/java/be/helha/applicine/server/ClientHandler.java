package be.helha.applicine.server;

import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.dao.MovieDAO;
import be.helha.applicine.dao.RoomDAO;
import be.helha.applicine.dao.SessionDAO;
import be.helha.applicine.dao.impl.MovieDAOImpl;
import be.helha.applicine.dao.impl.ClientsDAOImpl;
import be.helha.applicine.dao.impl.RoomDAOImpl;
import be.helha.applicine.dao.impl.SessionDAOImpl;
import be.helha.applicine.models.Visionable;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private MovieDAO movieDAO;
    private ClientsDAO clientsDAO;
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
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }
}