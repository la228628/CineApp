package be.helha.applicine.models.exceptions;

import be.helha.applicine.common.network.ObjectSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testObjectSocket {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Socket serverSideSocket;
    private ObjectSocket clientObjectSocket;
    private ObjectSocket serverObjectSocket;

    @BeforeEach
    public void setUp() throws IOException {
        // Créer un server socket sur un port aléatoire
        serverSocket = new ServerSocket(0);
        int port = serverSocket.getLocalPort();

        // Connecter le client au serveur
        clientSocket = new Socket("localhost", port);
        serverSideSocket = serverSocket.accept();

        // Initialiser les ObjectSocket des deux côtés
        clientObjectSocket = new ObjectSocket(clientSocket);
        serverObjectSocket = new ObjectSocket(serverSideSocket);
    }

    @AfterEach
    public void tearDown() throws IOException {
        clientObjectSocket.close();
        serverObjectSocket.close();
        clientSocket.close();
        serverSideSocket.close();
        serverSocket.close();
    }

    @Test
    public void testWriteAndRead() throws IOException, ClassNotFoundException {
        String message = "Hello, World!";
        clientObjectSocket.write(message);

        String receivedMessage = serverObjectSocket.read();
        assertEquals(message, receivedMessage);
    }
}
