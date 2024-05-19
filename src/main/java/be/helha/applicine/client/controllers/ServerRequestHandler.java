package be.helha.applicine.client.controllers;

import be.helha.applicine.common.network.ServerConstants;

import java.io.*;
import java.net.*;

public class ServerRequestHandler {
    private static ServerRequestHandler instance;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ServerRequestHandler() throws IOException {
        clientSocket = new Socket(ServerConstants.HOST, ServerConstants.PORT);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public static ServerRequestHandler getInstance() throws IOException {
        if (instance == null) {
            instance = new ServerRequestHandler();
        }
        return instance;
    }

    public <T> T sendRequest(Object request) throws IOException, ClassNotFoundException {
        out.writeObject(request);
        return (T) in.readObject();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}