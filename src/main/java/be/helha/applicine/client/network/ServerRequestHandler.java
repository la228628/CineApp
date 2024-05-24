package be.helha.applicine.client.network;

import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.request.ClientEvent;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.common.network.ServerConstants;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * This class is responsible for handling the requests to the server.
 * It is a singleton class.
 * It is a thread that listens to the server's responses.
 * It has a list of listeners that are notified when a response is received thus responsible for broadcasting the response to the listeners.
 */
public class ServerRequestHandler extends Thread {
    private ObjectSocket objectSocket;
    private static ServerRequestHandler instance;

    private Listener listener;

    private ArrayList<Listener> listenersList = new ArrayList<>();

    /**
     * Constructor of the ServerRequestHandler.
     * It initializes the object socket.
     */
    private ServerRequestHandler() {
        try {
            this.objectSocket = new ObjectSocket(new Socket(ServerConstants.HOST, ServerConstants.PORT));
            this.setDaemon(true);
            this.start();
        } catch (IOException e) {
            System.out.println("Error while creating the object socket: " + e.getMessage());
        }
    }

    /**
     * Adds a listener to the list of listeners.
     * @param listener the listener to add.
     */
    public void addListener(Listener listener) {
        // check if the listener is already in the list
        if (this.listenersList.contains(listener)) {
            return;
        }
        System.out.println("Listener added");
        this.listenersList.add(listener);
        System.out.println(listenersList.size());
    }

    /**
     * Removes a listener from the list of listeners.
     * @param listener the listener to remove.
     */
    public void removeListener(Listener listener) {
        if (listenersList.contains(listener)) {
            this.listenersList.remove(listener);
            System.out.println("Listener removed");
            System.out.println(listenersList.size());
        }
    }

    /**
     * Removes all listeners from the list of listeners.
     */
    public void removeAllListeners() {
        this.listenersList.clear();
        System.out.println(listenersList.size());
    }

    /**
     * The run method of the thread.
     * It listens to the server's responses and notifies the listeners when a response is received.
     */
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

    /**
     * Gets the instance of the ServerRequestHandler.
     * @return the instance of the ServerRequestHandler.
     */
    public static synchronized ServerRequestHandler getInstance() {
        if (instance == null) {
            instance = new ServerRequestHandler();
        }
        return instance;
    }

    /**
     * Stops the thread.
     */
    public void stopThread() {
        this.objectSocket.close();
    }

    /**
     * Sends a request to the server.
     * @param request the request to send.
     * @throws IOException when an error occurs while sending the request.
     */
    public void sendRequest(Object request) throws IOException {
        System.out.println("Sending request: " + request);
        this.objectSocket.write(request);
    }

    /**
     * Interface for the listeners of the ServerRequestHandler.
     * Handles the response received from the server. (Including connection lost)
     */
    public interface Listener {
        void onResponseReceive(ClientEvent response);

        void onConnectionLost();
    }
}