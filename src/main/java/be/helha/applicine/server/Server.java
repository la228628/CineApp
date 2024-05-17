package be.helha.applicine.server;

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
import java.sql.SQLException;

public class Server {

    public static void main(String[] args) throws IOException {
        initializeAppdata();
        ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);
        System.out.println("Server is running on port " + ServerConstants.PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected");
            try {
                new ClientHandler(clientSocket).start();
            } catch (IOException e) {
                System.out.println("Error creating client handler: " + e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void initializeAppdata() {
        FileManager.createDataFolder();

        MovieDAO movieDAO = new MovieDAOImpl();

        ClientsDAO clientsDAO = new ClientsDAOImpl();

        if (movieDAO.isMovieTableEmpty()) {
            ApiRequest apiRequest = new ApiRequest();
            try {
                apiRequest.fillDatabase();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (clientsDAO.isClientTableEmpty()) {
            clientsDAO.createClient("John Doe", "john.doe@example.com", "johndoe", "motdepasse");
        }

        RoomDAO roomDAO = new RoomDAOImpl();
        if (roomDAO.isRoomTableEmpty()) {
            roomDAO.fillRoomTable();
        }
    }
}