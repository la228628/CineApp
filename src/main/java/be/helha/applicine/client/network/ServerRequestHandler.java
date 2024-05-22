package be.helha.applicine.client.network;

import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.event.Event;
import be.helha.applicine.common.models.event.EventListener;
import be.helha.applicine.common.models.responses.FillListViewableResponse;
import be.helha.applicine.common.models.responses.ResponseVisitor;
import be.helha.applicine.common.models.responses.ToEventResponse;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.common.network.ServerConstants;


import java.io.*;
import java.net.*;
import java.util.ArrayList;

//thread qui gère les requêtes du client vers le serveur
public class ServerRequestHandler extends Thread {
    private ObjectSocket objectSocket;
    private ReadResponseThread readResponseThread;
    private static ServerRequestHandler instance;

    private ServerRequestHandler() throws IOException {
        this.objectSocket = new ObjectSocket(new Socket(ServerConstants.HOST, ServerConstants.PORT));
        this.readResponseThread = new ReadResponseThread(objectSocket);
        //le set deamon permet de dire que le thread s'arrête quand le programme principal s'arrête
        this.readResponseThread.setDaemon(true);
        this.readResponseThread.start();
    }

    public void run(){
        try{
            while(!this.isInterrupted()){
                Object obj = this.objectSocket.read();
                System.out.println("Objet recu: " + obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequest(Object request) throws IOException {
        this.objectSocket.write(request);
    }

    public static ServerRequestHandler getInstance() throws IOException {
        if (instance == null) {
            instance = new ServerRequestHandler();
            instance.start();
        }
        return instance;
    }

    public void stopThread(){
        //on devra aussi fermer le thread d'écriture
        this.readResponseThread.interrupt();
        this.objectSocket.close();
    }


    public void close() throws IOException {
        this.objectSocket.close();
    }


}