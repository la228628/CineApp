package com.example.applicine.controllers;
import com.example.applicine.dao.MovieDAO;
import com.example.applicine.dao.impl.MovieDAOImpl;
import com.example.applicine.views.ControllerClient;
import com.example.applicine.views.LoginControllerView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This class is the main class for the client interface application.
 */
public class ClientInterfaceApplication extends Application implements ControllerClient.ClientViewListener {
    private final MasterApplication parentController = new MasterApplication();
    @FXML
    private Button rightButton;
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

    /**
     * This method is used to increment the offset by 3.
     * @param offset
     * @return offset + 3 if offset + 3 is less than the size of the list of movies. Else, it returns 0.
     */
    @Override
    public int incrementOffset(int offset) {
        if((offset + 3) >= movieDao.getAllMovies().size()) {
            return 0;
        }
        return offset + 3;
    }

    /**
     * This method is used to decrement the offset by 3.
     * @param offset
     * @return offset - 3 if offset - 3 is less than 0. Else, it returns the size of the list of movies - 3.
     */
    @Override
    public int decrementOffset(int offset) {
        if((offset - 3) <= 0) {
            return movieDao.getAllMovies().size() - 3;
        }
        return offset - 3;
    }
}