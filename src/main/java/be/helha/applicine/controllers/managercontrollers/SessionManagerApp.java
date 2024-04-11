package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.dao.impl.RoomDAOImpl;
import be.helha.applicine.dao.impl.SessionDAOImpl;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Room;
import be.helha.applicine.models.movieSession;
import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.views.managerviews.SessionManagerViewController;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class SessionManagerApp extends ManagerController implements SessionManagerViewController.SessionManagerViewListener, InvalidationListener {

    private ManagerController parentController;

    private FXMLLoader sessionManagerFxmlLoader;

    private SessionManagerViewController sessionManagerViewController;

    protected List<Room> roomList;

    protected List<movieSession> movieSessionList;

    private RoomDAOImpl roomDAO;
    private SessionDAOImpl sessionDAO;

    /**
     * Constructor
     */
    public SessionManagerApp() {
        super();
        roomDAO = new RoomDAOImpl();
        sessionDAO = new SessionDAOImpl();
        try {
            this.roomList = roomDAO.getAllRooms();
            this.movieSessionList = sessionDAO.getAllSessions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Starts the session manager view.
     * @param adminPage
     * @throws Exception
     */

    @Override
    public void start(Stage adminPage) throws Exception {
        sessionManagerFxmlLoader = parentController.getSessionManagerFXML();
        sessionManagerViewController = sessionManagerFxmlLoader.getController();
        sessionManagerViewController.setListener(this);
        for (movieSession movieSession : movieSessionList) {
            sessionManagerViewController.displaySession(movieSession);
            System.out.println(movieSession.getId());
        }
        sessionManagerViewController.intialize();

    }

    /**
     * Sets the parent controller
     * @param managerController
     */
    public void setParentController(ManagerController managerController) {
        this.parentController = managerController;
    }

    /**
     * Adds a new session to the database or modify the selected session.
     *
     * @param sessionId
     * @param movieId
     * @param roomId
     * @param version
     * @param convertedDateTime
     * @param currentEditType
     * @throws InvalideFieldsExceptions
     */

    @Override
    public void onValidateButtonClick(Integer sessionId, Integer movieId, Integer roomId, String version, String convertedDateTime, String currentEditType) throws InvalideFieldsExceptions {
        try {
            validateFields(movieId, roomId, version, convertedDateTime);
        }catch (InvalideFieldsExceptions e){
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs invalides", e.getMessage());
            return;
        }
        if (currentEditType.equals("add")) {
            sessionDAO.addSession(movieId, roomId, convertedDateTime, version);
        }else if (currentEditType.equals("modify")) {
            sessionDAO.updateSession(sessionId, movieId, roomId, convertedDateTime, version);
        }
        movieSessionList = sessionDAO.getAllSessions();
        refreshSessionManager();
    }

    /**
     * Ensure that all fields are filled and in the correct format.
     * @param movieId
     * @param roomId
     * @param version
     * @param convertedDateTime
     * @throws InvalideFieldsExceptions
     */

    public void validateFields(Integer movieId, Integer roomId, String version, String convertedDateTime) throws InvalideFieldsExceptions {
        if (movieId == -1 || roomId == null || version==null || convertedDateTime.isEmpty() || !(convertedDateTime.contains(":"))) {
            throw new InvalideFieldsExceptions("Tous les champs n'ont pas été remplis");
        }
    }

    /**
     * Sets the possible movies names that can be selected in the view.
     */
    @Override
    public void setPossibleMovies() {
        sessionManagerViewController.clearPossibleNames();
        for (Movie m : movieList) {
            sessionManagerViewController.addPossibleName(m.getTitle());
        }
    }

    /**
     * Returns the duration of a movie from an id in the database.
     * @param id id from the view
     * @return
     */
    @Override
    public Integer getMovieDuration(int id) {
        Movie m = movieDAO.getMovieById(id);
        int duration = m.getDuration();
        return duration;
    }

    /**
     * Sets the possible rooms that can be selected in the view.
     *
     */
    @Override
    public void setPossibleRooms() {
        for (Room r : roomList) {
            sessionManagerViewController.addPossibleRoom(r.getNumber());
        }
    }

    /**
     * Returns the movie from the current selection in the view.
     * @param currentSelection
     * @return
     */
    @Override
    public Movie getMovieFrom(Integer currentSelection) {
        return super.getMovieFrom(currentSelection);
    }

    /**
     * Returns the room from the current selection in the view.
     * @param value
     * @throws SQLException
     */
    @Override
    public void onRoomSelectedEvent(Integer value) throws SQLException {
        if(value == null){
            return;
        }
        Room room = getRoomFrom(value);
        int capacity = room.getCapacity();
        sessionManagerViewController.setRoomCapacity(capacity);
    }

    /**
     * Deletes a session from the database.
     * @param currentSessionID
     */
    @Override
    public void onDeleteButtonClick(int currentSessionID) {
        boolean confirmed = showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Suppression", "Voulez-vous vraiment supprimer cette séance ?");
        if (confirmed) {
            sessionDAO.removeSession(currentSessionID);
            movieSessionList = sessionDAO.getAllSessions();
            refreshSessionManager();
        }else {
            return;
        }

    }

    /**
     * Returns a room from an index.
     * @param index
     * @return
     * @throws SQLException
     */
    public Room getRoomFrom(int index) throws SQLException {
        return roomDAO.getRoomById(index);
    }

    /**
     * Refreshes the session manager view.
     */
    public void refreshSessionManager() {
        sessionManagerViewController.clearSessions();
        for (movieSession movieSession : movieSessionList) {
            sessionManagerViewController.displaySession(movieSession);
        }

        sessionManagerViewController.refreshAfterEdit();
    }


    /**
     * Refreshes the view after a modification.
     *
     * @param observable is the observable that has been modified ( here the movieManagerApp)
     */
    @Override
    public void invalidated(javafx.beans.Observable observable) {
        //On se sert de l'observable pour notifier les SessionApp que la liste de films a changé
        this.movieSessionList = sessionDAO.getAllSessions();
        this.movieList = movieDAO.getAllMovies();
        refreshSessionManager();
        setPossibleMovies();

        System.out.println("SessionManagerApp invalidated");

    }
}
