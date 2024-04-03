package com.example.applicine.controllers;
import com.example.applicine.database.ApiRequest;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class MasterApplication extends Application {
    private Window currentWindow;
    public void setCurrentWindow(Window currentWindow) {
        this.currentWindow = currentWindow;
        System.out.println("Current window set to: " + currentWindow);
    }
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        createDataFolder(); // On va créer le dossier images dans le dossier AppData

        LoginApplication loginApplication = new LoginApplication();
        loginApplication.start(stage);
        this.currentWindow = stage;
    }
    public void toLogin() throws IOException{
        currentWindow.hide();
        LoginApplication loginApplication = new LoginApplication();
        loginApplication.start(new Stage());
    }
    public void toClient() throws Exception {
        currentWindow.hide();
        ClientInterfaceApplication clientInterfaceApplication = new ClientInterfaceApplication();
        clientInterfaceApplication.start(new Stage());
    }
    public void toAdmin() throws Exception {
        currentWindow.hide();
        ManagerApplication managerApplication = new ManagerApplication();
        managerApplication.start(new Stage());
    }
    public static void main(String[] args) {
        launch();
    }


    private void createDataFolder() {
        String getAppdata = System.getenv("APPDATA");
        Path path = Paths.get(getAppdata + "/Applicine/images/");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


