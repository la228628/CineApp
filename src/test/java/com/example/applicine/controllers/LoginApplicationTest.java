package com.example.applicine.controllers;

import com.example.applicine.controllers.LoginApplication;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginApplicationTest {

    //this currentWindow est null car il n'est pas défini dans le test et n'est pas utilisé dans la méthode inputHandling de LoginApplication
    @Test
    public void testInputHandling_AdminCredentials_ReturnsTrue() throws Exception {
        ManagerApplication manager = new ManagerApplication();
        ClientInterfaceApplication clientInterfaceApplication = new ClientInterfaceApplication();
        LoginApplication loginApplication = new LoginApplication();
        //this.currentWindow = loginApplication;

        String username = "admin";
        String password = "admin";
        //loginApplication.setCurrentWindow(new javafx.stage.Stage());

        boolean result = loginApplication.inputHandling(username, password);

        assertTrue(result);
    }
}