package be.helha.applicine.client.views.managerviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * This class is the controller of the main manager view
 */
public class MainManagerViewController {

    @FXML
    private Tab movieListTab;

    @FXML
    private Tab sessionListTab;

    @FXML
    private Tab sagaListTab;


    private static Stage adminWindow;




    public static URL getFXMLResource() {
        return MainManagerViewController.class.getResource("mainManagerView.fxml");
    }

    public static Stage getStage() {
        return adminWindow;
    }

    /**
     * Sets the stage of the given fxmlLoader
     * @param fxmlLoader the fxmlLoader to set the stage of
     * @throws IOException if the fxmlLoader can't be loaded
     */
    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        adminWindow = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 1200, 750);
        scene.getStylesheets().add(MovieManagerViewController.class.getResource("/be/helha/applicine/client/views/CSS/managerStyle.css").toExternalForm());
        adminWindow.setScene(scene);
        adminWindow.setTitle("Manager Application");
        adminWindow.setScene(scene);
        adminWindow.show();
    }

    /**
     * get the specific fxmlLoader for the movie manager view
     * @return the fxmlLoader
     */
    public FXMLLoader getMovieManagerFXML() {
        try {
            FXMLLoader movieManagerFXML = new FXMLLoader(MovieManagerViewController.getFXMLResource());
            AnchorPane movieManagerPane = movieManagerFXML.load();
            movieListTab.setContent(movieManagerPane);
            return movieManagerFXML;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get the specific fxmlLoader for the session manager view
     * @return the fxmlLoader
     */

    public FXMLLoader getSessionManagerFXML() {
        try {
            FXMLLoader sessionManagerFXML = new FXMLLoader(SessionManagerViewController.getFXMLResource());
            AnchorPane sessionManagerPane = sessionManagerFXML.load();
            sessionListTab.setContent(sessionManagerPane);
            return sessionManagerFXML;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get the specific fxmlLoader for the saga manager view
     * @return the fxmlLoader
     */

    public FXMLLoader getSpecialViewableFXML() {
        try {
            FXMLLoader sagaManagerFXML = new FXMLLoader(SpecialViewableViewController.getFXMLResource());
            AnchorPane sagaManagerPane = sagaManagerFXML.load();
            sagaListTab.setContent(sagaManagerPane);
            return sagaManagerFXML;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
