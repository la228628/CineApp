package be.helha.applicine.client.network;

import be.helha.applicine.common.models.request.ClientEvent;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.common.network.ServerConstants;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

//thread qui gère les requêtes du client vers le serveur
public class ServerRequestHandler extends Thread {
    private ObjectSocket objectSocket;
    private static ServerRequestHandler instance;

    private Listener listener;

    private ArrayList<Listener> listenersList = new ArrayList<>();

    private ServerRequestHandler() {
        try {
            this.objectSocket = new ObjectSocket(new Socket(ServerConstants.HOST, ServerConstants.PORT));
            this.setDaemon(true);
            this.start();
        } catch (IOException e) {
            System.out.println("Error while creating the object socket: " + e.getMessage());
        }
    }

    public void addListener(Listener listener) {
        // check if the listener is already in the list
        if (this.listenersList.contains(listener)) {
            return;
        }
        System.out.println("Listener added");
        this.listenersList.add(listener);
        System.out.println(listenersList.size());
    }

    public void removeListener(Listener listener) {
        if (listenersList.contains(listener)) {
            this.listenersList.remove(listener);
            System.out.println("Listener removed");
            System.out.println(listenersList.size());
        }
    }

    public void removeAllListeners() {
        this.listenersList.clear();
        System.out.println(listenersList.size());
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                ClientEvent response = objectSocket.read();
                for (Listener listener : listenersList) {
                     listener.onResponseReceive(response);
                }
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
        System.out.println("Sending request: " + request);
        this.objectSocket.write(request);
    }

    public interface Listener {
        void onResponseReceive(ClientEvent response);

        void onConnectionLost();
    }
}