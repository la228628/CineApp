package be.helha.applicine.common.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectSocket {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;


    public ObjectSocket(Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }
    public void write(Object object) throws IOException {
        out.reset();
        out.writeObject(object);
    }



    public <T> T read() throws IOException, ClassNotFoundException {
        return (T) in.readObject();
    }

    public void close() {
        try {
            this.in.close();
            this.out.close();
        } catch (IOException e) {
            // Ignore
        }
    }


}
