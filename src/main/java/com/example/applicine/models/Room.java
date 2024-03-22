package com.example.applicine.models;

public class Room {
    private int number;
    private int capacity;

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
