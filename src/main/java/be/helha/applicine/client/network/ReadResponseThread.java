package be.helha.applicine.client.network;

import be.helha.applicine.common.models.request.ClientEvent;
import be.helha.applicine.common.network.ObjectSocket;

public class ReadResponseThread extends Thread {
    private final ObjectSocket objectSocket;
    private final Listener listener;

    public ReadResponseThread(ObjectSocket objectSocket, Listener listener) {
        this.objectSocket = objectSocket;
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
            e.printStackTrace();
            listener.onConnectionLost();
        }
    }
    public interface Listener{
        void onResponseReceive(ClientEvent response);

        void onConnectionLost();
    }
}


