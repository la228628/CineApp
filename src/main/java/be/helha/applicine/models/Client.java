package be.helha.applicine.models;

/**
 * This class represents a client.
 */
public class Client {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;

    /**
     * Constructor for the client.
     * @param name The name of the client.
     * @param email The email of the client.
     * @param phone The phone number of the client.
     * @param address The address of the client.
     */
    public Client(int id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
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
    public String getPhone() {
        return phone;
    }
    /**
     * Getter for the address of the client.
     * @return The address of the client.
     */
    public String getAddress() {
        return address;
    }
}
