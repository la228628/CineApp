package be.helha.applicine.server;

import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is running on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected");
            new ClientHandler(clientSocket).start();
        }
    }
}