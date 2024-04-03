package com.example.applicine.views;

import com.example.applicine.controllers.LoginApplication;
import com.example.applicine.controllers.MasterApplication;
import com.example.applicine.dao.MovieDAO;
import com.example.applicine.dao.impl.MovieDAOImpl;
import com.example.applicine.database.ApiRequest;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerClient {
    @FXML
    private HBox filmsContainer;
    @FXML
    private Button rightButton;
    @FXML
    private Button leftButton;

    private MovieDAO movieDAO = new MovieDAOImpl();
    private List<Movie> moviesList;
    //attribute to keep track of the index of the first movie to be displayed
    int offsetIndex = 0;
    private ClientViewListener listener;
    private final MasterApplication parentController = new MasterApplication();
    private static Stage clientWindow;

    public static Stage getClientWindow() {
        return clientWindow;
    }

    public void setListener(ClientViewListener listener) {
        this.listener = listener;
    }

    public void initialize() {
        parentController.setCurrentWindow(clientWindow);
        movieDAO.adaptAllImagePathInDataBase();
        moviesList = movieDAO.getAllMovies();

        if(moviesList.isEmpty()){
            try {
                ApiRequest.main(null);
                moviesList = movieDAO.getAllMovies();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        showThreeMovies();
    }

    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        clientWindow = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 750);
        clientWindow.setTitle("Client Interface");
        clientWindow.setScene(scene);
        clientWindow.show();
    }

    public void showThreeMovies() {
        filmsContainer.getChildren().clear();
        if (moviesList.size() < 3) {
            for(int i = 0 ; i< moviesList.size(); i++){
                Pane pane = new Pane();
                pane.setPrefSize(300, 300);
                pane.setStyle("-fx-background-color: #2737d3; -fx-border-color: #ffffff; -fx-border-width: 1px; -fx-text-alignment: center; -fx-font-size: 15px");
                Label label = new Label(moviesList.get(i).getTitle());
                label.setLayoutX(50);
                label.setLayoutY(400);
                String imagePath = moviesList.get(i).getImagePath();
                ImageView imageView = new ImageView(imagePath);
                imageView.setFitWidth(275);
                imageView.setFitHeight(400);
                pane.getChildren().add(imageView);
                pane.getChildren().add(label);
                filmsContainer.getChildren().add(pane);
            }
        } else {
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
                filmsContainer.getChildren().add(pane);
            }
        }
    }

    public void toLoginPage() throws Exception {
        parentController.toLogin();
    }

    public void rightButton() {
        try {
            offsetIndex = listener.incrementOffset(offsetIndex);
            showThreeMovies();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No more movies to show");
            offsetIndex = moviesList.size() - 3;
            showThreeMovies();
        }
        System.out.println(offsetIndex);
    }

    public void leftButton() {
        try {
            if (offsetIndex % 3 != 0) {
                do {
                    offsetIndex -= 1;
                } while (offsetIndex % 3 != 0);
            } else {
                offsetIndex = listener.decrementOffset(offsetIndex);
            }
            showThreeMovies();

        } catch (IndexOutOfBoundsException e) {
            System.out.println("No more movies to show");
            offsetIndex = 0;
            showThreeMovies();
        }
        System.out.println(offsetIndex);
    }

    public interface ClientViewListener {
        int incrementOffset(int offset);

        int decrementOffset(int offset);
    }

    public static URL getFXMLResource() {
        return ControllerClient.class.getResource("clientSide.fxml");
    }
}