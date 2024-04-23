package be.helha.applicine.views.managerviews;

import be.helha.applicine.controllers.managercontrollers.ManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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
     * @param fxmlLoader
     * @throws IOException
     */
    public static void setStageOf(FXMLLoader fxmlLoader) throws IOException {
        adminWindow = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 1200, 750);
        scene.getStylesheets().add(MovieManagerViewController.class.getResource("/be/helha/applicine/views/CSS/managerStyle.css").toExternalForm());
        adminWindow.setScene(scene);
        adminWindow.setTitle("Manager Application");
        adminWindow.setScene(scene);
        adminWindow.show();
    }

    /**
     * get the specific fxmlLoader for the movie manager view
     * @return
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
     * @return
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

    public void setListener(ManagerController managerController) {
    }

}
