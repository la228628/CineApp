package com.example.applicine.views;

import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
    int indexStart = 0;

    public void initialize() {
        showThreeMovies();
    }

    public void showThreeMovies() {
        try {
            filmContainer.getChildren().clear();
            for (int i = 0; i < 3; i++) {
                addMovieToContainer(i);
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void addMovieToContainer(int i) {
        Pane pane = createStyledPane();
        Label label = createLabel(i);
        ImageView imageView = createImageView(i);
        pane.getChildren().addAll(imageView, label);
        filmContainer.getChildren().add(pane);
    }

    private Pane createStyledPane() {
        Pane pane = new Pane();
        pane.setPrefSize(300, 300);
        pane.setStyle("-fx-background-color: #2737d3; -fx-border-color: #ffffff; -fx-border-width: 1px; -fx-text-alignment: center; -fx-font-size: 15px");
        return pane;
    }

    private Label createLabel(int i) {
        Label label = new Label(moviesList.get(indexStart + i).getTitle());
        label.setLayoutX(50);
        label.setLayoutY(400);
        return label;
    }

    private ImageView createImageView(int i) {
        String imagePath = moviesList.get(indexStart + i).getImagePath();
        ImageView imageView = new ImageView(imagePath);
        imageView.setFitWidth(275);
        imageView.setFitHeight(400);
        return imageView;
    }

    public void toLoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ControllerLogin.getFXMLResource());
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        Stage thisWindow = (Stage) rightButton.getScene().getWindow();
        thisWindow.close();
    }

    public void rightButton() {
        indexStart += 3;
        if (indexStart >= moviesList.size()) {
            indexStart = 0;
        }
        showThreeMovies();
    }

    public void leftButton() {
        indexStart -= 3;
        if (indexStart < 0) {
            indexStart = moviesList.size() - 3;
        }
        showThreeMovies();
    }

    public static URL getFXMLResource() {
        return ControllerClient.class.getResource("clientSide.fxml");
    }
}