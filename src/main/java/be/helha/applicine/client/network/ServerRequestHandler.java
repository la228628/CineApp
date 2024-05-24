package be.helha.applicine.client.network;

import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.request.ClientEvent;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.common.network.ServerConstants;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * This class is responsible for handling the responses from the server after sending a request.
 * It is also responsible for sending requests to the server.
 * It is a singleton class that extends the Thread class.
 */
public class ServerRequestHandler extends Thread {
    private ObjectSocket objectSocket;
    private static ServerRequestHandler instance;

    private ArrayList<Listener> listenersList = new ArrayList<>();

    /**
     * Constructor for the ServerRequestHandler class.
     * It creates a new ObjectSocket and starts the thread.
     * The constructor is private to ensure that only one instance of the class is created.
     * The class is a singleton.
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
     * listeners are notified when a response is received from the server.
     * @param listener
     */

    public void addListener(Listener listener) {
        if (this.listenersList.contains(listener)) {
            return;
        }
        System.out.println("Listener added");
        this.listenersList.add(listener);
        System.out.println(listenersList.size());
    }


    /**
     * Removes a listener from the list of listeners.
     */
    public void removeAllListeners() {
        this.listenersList.clear();
        System.out.println(listenersList.size());
    }

    /**
     * The run method of the thread.
     * It reads the response from the server and notifies the listeners.
     * If an exception occurs, it prints an error message.
     * The thread stops when it is interrupted.
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
     * Returns the instance of the ServerRequestHandler class.
     * If the instance is null, it creates a new instance.
     * The synchronized keyword ensures that only one thread can access this method at a time.
     * @return
     */

    public static synchronized ServerRequestHandler getInstance() {
        if (instance == null) {
            instance = new ServerRequestHandler();
        }
        return instance;
    }


    /**
     * Sends a request to the server.
     * It writes the request to the object socket.
     * @param request
     * @throws IOException
     */
    public void sendRequest(Object request) throws IOException {
        System.out.println("Sending request: " + request);
        this.objectSocket.write(request);
    }

    /**
     * This interface is used to notify the listeners when a response is received from the server.
     * The onResponseReceive method is called when a response is received.
     */
    public interface Listener {
        void onResponseReceive(ClientEvent response);

        default void onConnectionLost(){

        }
    }
}