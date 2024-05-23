package be.helha.applicine.server;

import be.helha.applicine.common.models.Client;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.common.network.ServerConstants;
import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.server.dao.MovieDAO;
import be.helha.applicine.server.dao.RoomDAO;
import be.helha.applicine.server.dao.impl.ClientsDAOImpl;
import be.helha.applicine.server.dao.impl.MovieDAOImpl;
import be.helha.applicine.server.dao.impl.RoomDAOImpl;
import be.helha.applicine.server.database.ApiRequest;
import okhttp3.internal.ws.WebSocketReader;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    //liste qui contient le nombre de clients connectés
    protected List<ClientHandler> clientsConnected = new ArrayList<>();


    public static void main(String[] args) {
        try {
            initializeAppdata();
            Server server = new Server();
            server.go();
        } catch (IOException e) {
            System.out.println("Error while starting server");
            e.printStackTrace();
        }
    }

    private void go() throws IOException {
        System.out.println("Starting server...");

        ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);
        System.out.println("Server started on port " + ServerConstants.PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New connection from " + socket.getInetAddress());
            ObjectSocket objectSocket = new ObjectSocket(socket);
            ClientHandler thread = new ClientHandler(objectSocket);
            this.clientsConnected.add(thread);
            thread.start();
        }
    }

    public void broadcast(Object message) {
        for (ClientHandler clientHandler : clientsConnected) {
            clientHandler.writeToClient(message);
        }
    }

    private static void initializeAppdata() {
        try {
            FileManager.createDataFolder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            clientsDAO.create(new Client("John Doe", "john.doe@example.com", "johndoe", "motdepasse"));
        }

        RoomDAO roomDAO = new RoomDAOImpl();
        if (roomDAO.isRoomTableEmpty()) {
            roomDAO.fillRoomTable();
        }
    }
}