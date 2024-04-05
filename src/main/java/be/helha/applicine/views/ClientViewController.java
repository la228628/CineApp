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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

<<<<<<<< HEAD:src/main/java/be/helha/applicine/views/ClientViewController.java
public class ClientViewController {
========
public class ClientControllerView {
>>>>>>>> 7d3c72af4a6b3fcdf903cd8e5508bc3ed4c259cd:src/main/java/be/helha/applicine/views/ClientControllerView.java
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane filmsContainer;
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

        if (moviesList.isEmpty()) {
            JFrame frame = null;
            try {
                frame = getWaitingWindow();

                ApiRequest.main(null);
                moviesList = movieDAO.getAllMovies();
                frame.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            frame.dispose();
        }
        showMovies();
    }

    /**
     * This method creates a JFrame that will be displayed while the database is being filled.
     *
     * @return The JFrame that will be displayed.
     */
    private JFrame getWaitingWindow() {
        JFrame frame = new JFrame();
        frame.setSize(500, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Veuillez patienter pendant que la base de données se remplit...");
        frame.add(label);
        frame.setVisible(true);
        return frame;
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
     *
     */
    public void showMovies() {
        filmsContainer.getChildren().clear();
        for (int i = 0; i < moviesList.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MoviePane.fxml"));
                Pane pane = loader.load();
                MoviePaneViewController controller = loader.getController();
                controller.setMovie(moviesList.get(i));
                filmsContainer.getChildren().add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to deconnect the user to make him return on the login page.
     * @throws Exception
     */
    public void toLoginPage() throws Exception {
        //je vais appeler la méthode toLogin() de la classe MasterApplication
        //qui va permettre de retourner à la page de connexion
        parentController.toLogin();
    }

    //servira à afficher les informations du compte en faisant pop up une nouvelle fenêtre

    public void toClientAccount() throws Exception {
        System.out.println("Account button clicked, je vais afficher les informations du compte");
        parentController.toClientAccount();
    }


    /**
     * This inner interface will be used to listen to the events in the client interface.
     *
     */
    public interface ClientViewListener {

    }


    /**
     * This method returns the URL of the fxml file of the client interface.
     * @return
     */
    public static URL getFXMLResource() {
<<<<<<<< HEAD:src/main/java/be/helha/applicine/views/ClientViewController.java
        return ClientViewController.class.getResource("clientSide.fxml");
========
        return ClientControllerView.class.getResource("clientSide.fxml");
>>>>>>>> 7d3c72af4a6b3fcdf903cd8e5508bc3ed4c259cd:src/main/java/be/helha/applicine/views/ClientControllerView.java
    }
}