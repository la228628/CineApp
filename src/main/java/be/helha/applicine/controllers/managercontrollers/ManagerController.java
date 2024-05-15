package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.controllers.MasterApplication;
import be.helha.applicine.dao.impl.MovieDAOImpl;
import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.models.Visionable;
import be.helha.applicine.views.managerviews.MainManagerViewController;
import be.helha.applicine.views.managerviews.SessionManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import be.helha.applicine.dao.MovieDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * ManagerApplication class is the controller class for the Manager view.
 * It is responsible for managing the movies and the sessions.
 * Only the manager can access this view and manage the movies and the sessions.
 */
public class ManagerController extends Application {
    private final FXMLLoader mainFxmlLoader = new FXMLLoader(MainManagerViewController.getFXMLResource());
    private final MasterApplication parentController;
    protected MovieDAO movieDAO;
    protected List<Visionable> movieList;

    private MainManagerViewController mainManagerViewController;

    public SessionManagerViewController sessionManagerViewController;

    /**
     * It fetches all the movies from the database to movieList.
     * It follows the DAO design pattern https://www.digitalocean.com/community/tutorials/dao-design-pattern.
     */
    public ManagerController(MasterApplication parentController) throws SQLException {
        this.parentController = parentController;
        movieDAO = new MovieDAOImpl();
        movieDAO.adaptAllImagePathInDataBase();
        movieList = movieDAO.getAllMovies();
    }
    /**
     * Alternative constructor for when the parentController is not defined.
     * @throws SQLException if there is an error with the database connection with DAO.
     */
    public ManagerController() throws SQLException {
        this(null);
    }

    /**
     * Starts the Manager view.
     * @param adminPage the stage of the view.
     * @throws IOException if there is an error with the fxml file.
     * @throws SQLException if there is an error with the database connection.
     */
    @Override
    public void start(Stage adminPage) throws IOException, SQLException {

        MainManagerViewController.setStageOf(mainFxmlLoader);

        parentController.setCurrentWindow(MainManagerViewController.getStage());

        mainManagerViewController = mainFxmlLoader.getController();
        MovieManagerApp movieManagerApp = new MovieManagerApp();
        movieManagerApp.setParentController(this);

        SessionManagerApp sessionManagerApp = new SessionManagerApp();
        sessionManagerApp.setParentController(this);

        movieManagerApp.addListener(sessionManagerApp);
        movieManagerApp.start(adminPage);
        sessionManagerApp.start(adminPage);

        adminPage.setOnCloseRequest(e -> {
            try {
                DatabaseConnection.closeConnection();
            } catch (SQLException ex) {
                parentController.popUpAlert("Erreur lors de la fermeture de la connexion à la base de données");
            }
        });
    }

    public void popUpAlert(String message) {
        parentController.popUpAlert(message);
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * It returns a movie to the movieList at index.
     *
     * @param index
     * @return movieList
     */
    public Visionable getMovieFrom(int index) {
        return movieList.get(index);
    }

    /**
     * Redirects to the login view and disconnect the user.
     *
     * @throws IOException
     */
    public void toLogin() throws IOException {
        parentController.toLogin();
    }

    /**
     * It returns the full movie list from the database.
     *
     * @return
     */
    protected List<Visionable> fullFieldMovieListFromDB() throws SQLException {
        return movieDAO.getAllMovies();
    }

    /**
     * It returns the fxmlLoader of the movieManager.
     * @return
     */

    protected FXMLLoader getMovieManagerFXML() {
        return mainManagerViewController.getMovieManagerFXML();
    }

    /**
     * It returns the fxmlLoader of the sessionManager.
     * @return
     */

    protected FXMLLoader getSessionManagerFXML() {
        return mainManagerViewController.getSessionManagerFXML();
    }

    //comme on n'a pas de mainController (MasterApplication) dans MovieManager, on doit redéfinir la méthode showAlert pour qu'elle soit accessible dans MovieManager
    protected boolean showAlert(Alert.AlertType alertType, String erreur, String filmIntrouvable, String s) {
        return parentController.showAlert(alertType, erreur, filmIntrouvable, s);
    }
}
