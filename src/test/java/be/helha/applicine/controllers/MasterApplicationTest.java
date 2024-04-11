package be.helha.applicine.controllers;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MasterApplicationTest {
    @Test
    void isLogged() {
        MasterApplication masterApplication = new MasterApplication();
        masterApplication.setLogged(true);
        assertTrue(masterApplication.isLogged());
    }

}