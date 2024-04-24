package be.helha.applicine.controllers;

import java.io.*;
import java.net.*;

public class ServerRequestHandler {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerRequestHandler(String serverAddress, int serverPort) throws IOException {
        clientSocket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendRequest(String request) throws IOException {
        out.println(request);
        return in.readLine();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}