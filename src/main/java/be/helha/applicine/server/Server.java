package be.helha.applicine.server;

import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.common.network.ServerConstants;
import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.server.dao.MovieDAO;
import be.helha.applicine.server.dao.RoomDAO;
import be.helha.applicine.server.dao.impl.ClientsDAOImpl;
import be.helha.applicine.server.dao.impl.MovieDAOImpl;
import be.helha.applicine.server.dao.impl.RoomDAOImpl;
import be.helha.applicine.server.database.ApiRequest;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Server class that manages the connection with the clients.
 * It listens to a port put in serverConfig and creates a new ClientHandler for each client that connects.
 */
public class Server {
    //liste qui contient le nombre de clients connectés
    protected final List<ClientHandler> clientsConnected = new ArrayList<>();

    /**
     * Main method that starts the server.
     * @param args the arguments of the application.
     */
    public static void main(String[] args) {
        try {
            initializeAppdata();
            Server server = new Server();
            server.go();
        } catch (IOException e) {
            System.out.println("Error while starting server");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Used to start the server. It listens to a port and creates a new ClientHandler for each client that connects, usable because non-static.
     * @throws IOException if an error occurs while starting the server.
     */
    private void go() throws IOException {
        System.out.println("Starting server...");

        try (ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT)) {
            System.out.println("Server started on port " + ServerConstants.PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection from " + socket.getInetAddress());
                ObjectSocket objectSocket = new ObjectSocket(socket);
                ClientHandler thread = new ClientHandler(objectSocket, this);
                this.clientsConnected.add(thread);
                System.out.println("Number of clients connected: " + clientsConnected.size());
                thread.start();
            }
        }catch (IOException e){
            System.out.println("Error while starting server");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Used to return the list of clients connected to the server.
     * @return the list of clients connected to the server.
     */
    public List<ClientHandler> getClientsConnected() {
        return clientsConnected;
    }

    /**
     * If the Applicine data folder does not exist, it creates it and fills the database with the data from the API.
     */
    private static void initializeAppdata() {
        try {
            FileManager.createDataFolder();
        } catch (IOException e) {
            System.out.println("Error while creating data folder");
            System.out.println(e.getMessage());
        }
        MovieDAO movieDAO = new MovieDAOImpl();

        ClientsDAO clientsDAO = new ClientsDAOImpl();

        try {
            if (movieDAO.isMovieTableEmpty()) {
                ApiRequest apiRequest = new ApiRequest();
                try {
                    apiRequest.fillDatabase();
                } catch (DaoException e) {
                    System.out.println("Error while filling database");
                    System.out.println(e.getMessage());
                }
            }

            RoomDAO roomDAO = new RoomDAOImpl();
            if (roomDAO.isRoomTableEmpty()) {
                roomDAO.fillRoomTable();
            }
        } catch (DaoException e) {
            System.out.println(e.getMessage());
        }
    }
}