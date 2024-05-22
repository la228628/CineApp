package be.helha.applicine.client.controllers;

import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.event.Event;
import be.helha.applicine.common.models.event.EventListener;
import be.helha.applicine.common.network.ServerConstants;
import kotlin.reflect.KParameter;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerRequestHandler {
    private static ServerRequestHandler instance;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    //listeners est une liste d'objets qui implémentent l'interface EventListener, donc qui veulent écouter les events
    //C'est une liste d'objets qui implémentent l'interface EventListener (donc qui ont une méthode onEventReceived)
    private ArrayList<EventListener> listeners = new ArrayList<>();
    private boolean isWaitingForEvents = false;

    private ServerRequestHandler() throws IOException {
        clientSocket = new Socket(ServerConstants.HOST, ServerConstants.PORT);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
        listenForEvents();
    }

    public <T> T sendRequest(Object request){
        isWaitingForEvents = false;
        System.out.println("Le client envoie une requête au serveur");
        try {
            out.writeObject(request);
            Object response = in.readObject();
            System.out.println("Le client a reçu une réponse du serveur, il attend maintenant des events");
            isWaitingForEvents = true;
            return (T) response;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de l'envoi de la requête au serveur");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ServerRequestHandler getInstance() throws IOException {
        if (instance == null) {
            instance = new ServerRequestHandler();
        }
        return instance;
    }
    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
