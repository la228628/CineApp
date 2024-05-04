package be.helha.applicine.server;

import be.helha.applicine.common.network.ServerConstants;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(ServerConstants.PORT);
        System.out.println("Server is running on port " + ServerConstants.PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected");
            try {
                new ClientHandler(clientSocket).start();
            } catch (IOException e) {
                System.out.println("Error creating client handler: " + e.getMessage());
            }
        }
    }
}