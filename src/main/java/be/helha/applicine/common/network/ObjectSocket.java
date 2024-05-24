package be.helha.applicine.common.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectSocket {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    /**
     * Constructor of the class.
     * @param socket The socket to use.
     * @throws IOException If an I/O error occurs.
     */

    public ObjectSocket(Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Write an object to the output stream.
     * @param object The object to write.
     * @throws IOException If an I/O error occurs.
     */
    public void write(Object object) throws IOException {
        out.reset();
        out.writeObject(object);
    }


    /**
     * Read an object from the input stream.
     * @param <T> The type of the object to read.
     * @return The object read.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If the class of the object read is not found.
     */

    public <T> T read() throws IOException, ClassNotFoundException {
        return (T) in.readObject();
    }

    /**
     * Close the input and output streams.
     */
    public void close() {
        try {
            this.in.close();
            this.out.close();
        } catch (IOException e) {
            // Ignore
        }
    }


}
