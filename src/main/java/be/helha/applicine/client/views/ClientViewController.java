package be.helha.applicine.client.views;

import be.helha.applicine.client.controllers.ClientController;
import be.helha.applicine.common.models.Viewable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;


public class ClientViewController {
    @FXML
    private Button loginButton;
    @FXML
    private Button myAccountButton;
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
    public void addMovie(Viewable movie, ClientController clientController) {
        try {
            FXMLLoader moviePane = new FXMLLoader(MoviePaneViewController.getFXMLResource());
            Pane pane = moviePane.load();
            MoviePaneViewController controller = moviePane.getController();
            controller.setMovie(movie);
            controller.setListener(clientController);
            filmsContainer.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void toLoginPage() throws Exception {
        listener.toLoginPage();
    }

    //servira à afficher les informations du compte en faisant pop up une nouvelle fenêtre

    public void toClientAccount() throws Exception {
        System.out.println("Account button clicked, je vais afficher les informations du compte");
        listener.toClientAccount();
    }

    public void updateButtonText(boolean isLogged) {
        if (isLogged) {
            loginButton.setText("Se déconnecter");
            myAccountButton.setVisible(true);
        } else {
            loginButton.setText("Login");
            myAccountButton.setVisible(false);
        }
    }

    public void showNotLoggedInAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText("Vous devez être connecté pour acheter des billets.");
        alert.showAndWait();
    }

    /**
     * This inner interface will be used to listen to the events in the client interface.
     */
    public interface ClientViewListener {
        void toLoginPage() throws Exception;

        void setCurrentWindow(Window currentWindow);

        void toClientAccount() throws Exception;
    }

    /**
     * This method returns the URL of the fxml file of the client interface.
     *
     * @return clientSide.fxml
     */
    public static URL getFXMLResource() {
        return ClientViewController.class.getResource("clientSide.fxml");
    }
}