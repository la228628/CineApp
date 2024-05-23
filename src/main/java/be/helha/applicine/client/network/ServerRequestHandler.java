package be.helha.applicine.client.network;

import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.common.network.ServerConstants;


import java.io.*;
import java.net.*;

//thread qui gère les requêtes du client vers le serveur
public class ServerRequestHandler {
    private ObjectSocket objectSocket;
    private ReadResponseThread readResponseThread;
    private static ServerRequestHandler instance;

    private Listener listener;

    public ServerRequestHandler(Listener listener){
        this.listener = listener;
        try {
            start();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    public static ServerRequestHandler getInstance(Listener listener){
        if(instance == null){
            instance = new ServerRequestHandler(listener);
        }
        return instance;
    }
    public void start() throws IOException{
        this.objectSocket = new ObjectSocket(new Socket(ServerConstants.HOST, ServerConstants.PORT));
        this.readResponseThread = new ReadResponseThread(this.objectSocket, listener );
        this.readResponseThread.setDaemon(true);
        this.readResponseThread.start();
    }

    public void stop(){
        //on devra aussi fermer le thread d'écriture
        this.readResponseThread.interrupt();
        this.objectSocket.close();
    }

    public void sendRequest(Object request) throws IOException {
        this.objectSocket.write(request);
    }

    /**
     * Listener pour les événements envoyés par le ServerRequestHandler
     * Il doit implémenter les méthodes de ReadResponseThread.Listener pour etre capable de lire les réponses du serveur
     */
    public interface Listener extends ReadResponseThread.Listener {}
}