package be.helha.applicine.client.network;

import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.event.Event;
import be.helha.applicine.common.models.responses.FillListViewableResponse;
import be.helha.applicine.common.models.responses.ResponseVisitor;
import be.helha.applicine.common.models.responses.ToEventResponse;
import be.helha.applicine.common.network.ObjectSocket;

import java.util.ArrayList;

public class ReadResponseThread extends Thread implements ResponseVisitor {
    private final ObjectSocket objectSocket;
    public ReadResponseThread(ObjectSocket objectSocket) {
        this.objectSocket = objectSocket;

    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Object response = objectSocket.read();
                System.out.println("Received response: " + response);
                //peut etre créer un dispatcher pour le type de réponse reçu ?
            }
        } catch (Exception e) {
            e.printStackTrace();
            interrupt();
        }
    }


    //tout les clients update leur liste de viewables
    @Override
    public void visit (FillListViewableResponse fillListViewableResponse){
        
    }

    //si dans le thread on reçoit une reponse de type ToEventResponse, cela servira aux autres classes pour traiter la réponse reçue
    @Override
    public  void visit (ToEventResponse toEventResponse){
        Event event = toEventResponse.getEvent();

    }
}


