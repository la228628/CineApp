package com.example.applicine.controllers;
import com.example.applicine.dao.MovieDAO;
import com.example.applicine.dao.impl.MovieDAOImpl;
import com.example.applicine.views.ControllerClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

/**
 * This class is the main class for the client interface application.
 */
public class ClientInterfaceApplication extends Application implements ControllerClient.ClientViewListener {
    MovieDAO movieDao = new MovieDAOImpl();
    @Override
    public void start(Stage clientWindow) throws Exception {
        FXMLLoader clientFXML = new FXMLLoader(ControllerClient.getFXMLResource());
        ControllerClient.setStageOf(clientFXML);
        ControllerClient controller = clientFXML.getController();
        controller.setListener(this);
    }
    public static void main(String[] args) {
        launch();
    }


}