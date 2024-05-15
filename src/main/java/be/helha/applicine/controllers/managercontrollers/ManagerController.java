package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.controllers.MasterApplication;
import be.helha.applicine.dao.ViewableDAO;
import be.helha.applicine.dao.impl.MovieDAOImpl;
import be.helha.applicine.dao.impl.ViewableDAOImpl;
import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Viewable;
import be.helha.applicine.views.managerviews.MainManagerViewController;
import be.helha.applicine.views.managerviews.SessionManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import be.helha.applicine.dao.MovieDAO;

import java.io.IOException;
import java.util.List;

/**
 * ManagerApplication class is the controller class for the Manager view.
 */
public class ManagerController extends Application {
    private final FXMLLoader mainFxmlLoader = new FXMLLoader(MainManagerViewController.getFXMLResource());

    /**
     * parentController is useful to say Master which window is currently open.
     */
    private MasterApplication parentController;

    protected MovieDAO movieDAO ;

    protected ViewableDAO viewableDAO;
    protected List<Movie> movieList;

    private MainManagerViewController mainManagerViewController;

    public SessionManagerViewController sessionManagerViewController;

    /**
     * It fetches all the movies from the database to movieList.
     * It follows the DAO design pattern https://www.digitalocean.com/community/tutorials/dao-design-pattern.
     */
    public ManagerController(MasterApplication parentController) {
        this.parentController = parentController;
        viewableDAO = new ViewableDAOImpl();
        movieDAO = new MovieDAOImpl();
        movieDAO.adaptAllImagePathInDataBase();
        movieList = movieDAO.getAllMovies();

    }

    public ManagerController() {
        movieDAO = new MovieDAOImpl();
        viewableDAO = new ViewableDAOImpl();
        movieDAO.adaptAllImagePathInDataBase();
        movieList = movieDAO.getAllMovies();
    }


    @Override
    public void start(Stage adminPage) throws Exception {

        MainManagerViewController.setStageOf(mainFxmlLoader);

        parentController.setCurrentWindow(MainManagerViewController.getStage());

        mainManagerViewController = mainFxmlLoader.getController();
        mainManagerViewController.setListener(this);

        MovieManagerApp movieManagerApp = new MovieManagerApp();
        movieManagerApp.setParentController(this);



        SessionManagerApp sessionManagerApp = new SessionManagerApp();
        sessionManagerApp.setParentController(this);

        SpecialViewableController specialViewableController = new SpecialViewableController();
        specialViewableController.setParentController(this);


        movieManagerApp.addListener(sessionManagerApp);
        movieManagerApp.addSpecialViewablesListener(specialViewableController);

        specialViewableController.addListener(sessionManagerApp);
        movieManagerApp.start(adminPage);
        sessionManagerApp.start(adminPage);
        specialViewableController.start(adminPage);


        adminPage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
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
    protected List<Movie> fullFieldMovieListFromDB() {
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

    protected FXMLLoader getSpecialViewableFXML() {
        return mainManagerViewController.getSpecialViewableFXML();
    }

    //comme on n'a pas de mainController (MasterApplication) dans MovieManager, on doit redéfinir la méthode showAlert pour qu'elle soit accessible dans MovieManager
    protected boolean showAlert(Alert.AlertType alertType, String erreur, String filmIntrouvable, String s) {
        return parentController.showAlert(alertType, erreur, filmIntrouvable, s);
    }

    public Movie getMovieFrom(int id) {
        return movieDAO.getMovieById(id);
    }
}
