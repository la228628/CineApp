package com.example.applicine.views;

import com.example.applicine.controllers.LoginApplication;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ControllerClient {
    @FXML
    private HBox filmContainer;
    @FXML
    private Button rightButton;
    @FXML
    private Button leftButton;

    private ArrayList<Movie> moviesList = DatabaseConnection.getAllMovies();

    //attribute to keep track of the index of the first movie to be displayed
    int offsetIndex = 0;
    private ClientViewListener listener;

    public void setListener(ClientViewListener listener) {
        this.listener = listener;
    }

    public void initialize() {
        showThreeMovies();
    }

    public void showThreeMovies() {
        filmContainer.getChildren().clear();
        for (int i = 0; i < 3; i++) {
            Pane pane = new Pane();
            pane.setPrefSize(300, 300);
            pane.setStyle("-fx-background-color: #2737d3; -fx-border-color: #ffffff; -fx-border-width: 1px; -fx-text-alignment: center; -fx-font-size: 15px");
            Label label = new Label(moviesList.get(offsetIndex + i).getTitle());
            label.setLayoutX(50);
            label.setLayoutY(400);
            String imagePath = moviesList.get(offsetIndex + i).getImagePath();
            ImageView imageView = new ImageView(imagePath);
            imageView.setFitWidth(275);
            imageView.setFitHeight(400);
            pane.getChildren().add(imageView);
            pane.getChildren().add(label);
            filmContainer.getChildren().add(pane);
        }
    }
    public void loadPage(Stage stage, FXMLLoader fxmlLoader, Scene scene) throws IOException {
        stage.setTitle("Client Interface");
        stage.setScene(scene);
        stage.show();
    }
    public void toLoginPage() throws Exception {
        LoginApplication loginApplication = new LoginApplication();
        loginApplication.start(new Stage());
        Stage thisWindow = (Stage)rightButton.getScene().getWindow();
        thisWindow.close();
    }

    public void rightButton() {
        offsetIndex = listener.incrementOffset(offsetIndex);
        showThreeMovies();
    }

    public void leftButton() {
        offsetIndex =  listener.decrementOffset(offsetIndex);
        showThreeMovies();
    }
    public interface ClientViewListener {
        int incrementOffset(int offset);
        int decrementOffset(int offset);
    }
    public static URL getFXMLResource() {
        return ControllerClient.class.getResource("clientSide.fxml");
    }
}