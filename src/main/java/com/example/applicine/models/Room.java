package com.example.applicine.models;

/**
 * This class represents a room.
 */
public class Room {
    private int number;
    private int capacity;

    /**
     * Constructor for the room.
     * @param number The number of the room.
     * @param capacity The capacity of the room.
     */
    public Room(int number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }
}
