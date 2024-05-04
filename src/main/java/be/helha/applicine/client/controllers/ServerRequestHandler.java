package be.helha.applicine.client.controllers;

import be.helha.applicine.common.network.ServerConstants;

import java.io.*;
import java.net.*;

public class ServerRequestHandler {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServerRequestHandler() throws IOException {
        clientSocket = new Socket("localhost", ServerConstants.PORT);
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
}