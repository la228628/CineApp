package be.helha.applicine.views;

import be.helha.applicine.controllers.MasterApplication;
import be.helha.applicine.dao.MovieDAO;
import be.helha.applicine.dao.impl.MovieDAOImpl;
import be.helha.applicine.database.ApiRequest;
import be.helha.applicine.models.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class ClientViewController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane filmsContainer;
    private ClientViewListener listener;
    private static Stage clientWindow;

    public Stage getStage() {
        return clientWindow;
    }

    public void setListener(ClientViewListener listener) {
        this.listener = listener;
    }

    /**
     * This method sets the stage of the client interface.
     *
     * @param fxmlLoader
     * @throws IOException
     */
    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        clientWindow = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        clientWindow.setTitle("Client Interface");
        clientWindow.setScene(scene);
        clientWindow.show();
    }

    /**
     * This method displays the movies in the database.
     * We get the MoviePane fxml file and set the movie in the controller.
     * We then add the pane to the filmsContainer.
     */
    public void addMovie(Movie movie) {
        try {
            FXMLLoader moviePane = new FXMLLoader(MoviePaneViewController.getFXMLResource());
            Pane pane = moviePane.load();
            MoviePaneViewController controller = moviePane.getController();
            controller.setMovie(movie);
            filmsContainer.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void toLoginPage() throws Exception {
        listener.toLoginPage();
    }


    /**
     * This inner interface will be used to listen to the events in the client interface.
     */
    public interface ClientViewListener {
        void toLoginPage() throws Exception;

        void setCurrentWindow(Window currentWindow);
    }

    /**
     * This method returns the URL of the fxml file of the client interface.
     *
     * @return
     */
    public static URL getFXMLResource() {
        return ClientViewController.class.getResource("clientSide.fxml");
    }
}