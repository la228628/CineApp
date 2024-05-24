package be.helha.applicine.common.models;

import java.io.Serializable;

/**
 * This class represents a room.
 */
public class Room implements Serializable {
    /**
     * The number of the room.
     */
    private final Integer number;
    /**
     * The capacity of the room.
     */
    private final Integer capacity;

    /**
     * Constructor for the room.
     *
     * @param number   The number of the room.
     * @param capacity The capacity of the room.
     */
    public Room(int number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    /**
     * Get the number of the room.
     *
     * @return The number of the room.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Get the capacity of the room.
     *
     * @return The capacity of the room.
     */
    public int getCapacity() {
        return capacity;
    }
}
