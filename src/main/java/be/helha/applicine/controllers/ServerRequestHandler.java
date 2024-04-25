package be.helha.applicine.controllers;

import be.helha.applicine.models.Visionable;

import java.io.*;
import java.net.*;
import java.util.List;

public class ServerRequestHandler {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServerRequestHandler(String serverAddress, int serverPort) throws IOException {
        clientSocket = new Socket(serverAddress, serverPort);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public Object sendRequest(Object request) throws IOException, ClassNotFoundException {
        out.writeObject(request);
        return in.readObject();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) {
        try {
            ServerRequestHandler serverRequestHandler = new ServerRequestHandler("localhost", 8080);
            List<Visionable> movies = (List<Visionable>) serverRequestHandler.sendRequest("GET_MOVIES");
            for (Visionable movie : movies) {
                System.out.println(movie.getTitle());
            }
            serverRequestHandler.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }
}