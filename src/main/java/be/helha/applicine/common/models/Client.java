package be.helha.applicine.common.models;

import java.io.Serializable;
/**
 * This class represents a client.
 */
public class Client implements Serializable {
    private Integer id;
    private final String name;
    private final String email;
    private final String username;
    private final String password;

    /**
     * Constructor for the client.
     * @param id The id of the client.
     * @param name The name of the client.
     * @param email The email of the client.
     * @param username The username of the client.
     * @param password The password of the client.
     */
    public Client(int id, String name, String email, String username, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    /**
     * Constructor for the client.
     * @param name The name of the client.
     * @param email The email of the client.
     * @param username The username of the client.
     * @param password The password of the client.
     */
    public Client(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    /**
     * Getter for the name of the client.
     * @return The name of the client.
     */
    public String getName() {
        return name;
    }
    /**
     * Getter for the email of the client.
     * @return The email of the client.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Getter for the phone number of the client.
     * @return The phone number of the client.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Getter for the address of the client.
     * @return The address of the client.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Getter for the id of the client.
     * @return The id of the client.
     */
    public int getId() {
        return id;
    }
    /**
     * checks if the email is valid
     * @param email The email to validate.
     * @return True if the email is valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
