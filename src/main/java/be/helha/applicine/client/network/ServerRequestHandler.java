package be.helha.applicine.client.network;

import be.helha.applicine.common.models.request.ClientEvent;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.common.network.ServerConstants;


import java.io.*;
import java.net.*;

//thread qui gère les requêtes du client vers le serveur
public class ServerRequestHandler extends Thread {
    private ObjectSocket objectSocket;
    private static ServerRequestHandler instance;

    private Listener listener;

    private ServerRequestHandler() {
        try {
            this.objectSocket = new ObjectSocket(new Socket(ServerConstants.HOST, ServerConstants.PORT));
            this.setDaemon(true);
            this.start();
        } catch (IOException e) {
            System.out.println("Error while creating the object socket: " + e.getMessage());
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                ClientEvent response = objectSocket.read();
                System.out.println("Received response: " + response);
                listener.onResponseReceive(response);
            }
        } catch (Exception e) {
            System.out.println("Error while reading response from server: " + e.getMessage());
        }
    }

    public static synchronized ServerRequestHandler getInstance() {
        if (instance == null) {
            instance = new ServerRequestHandler();
        }
        return instance;
    }

    public void stopThread() {
        this.objectSocket.close();
    }

    public void sendRequest(Object request) throws IOException {
        this.objectSocket.write(request);
    }

    public interface Listener {
        void onResponseReceive(ClientEvent response);

        void onConnectionLost();
    }
}