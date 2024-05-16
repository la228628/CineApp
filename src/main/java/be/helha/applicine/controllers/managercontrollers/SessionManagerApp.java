package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.dao.impl.RoomDAOImpl;
import be.helha.applicine.dao.impl.SessionDAOImpl;
import be.helha.applicine.dao.impl.ViewableDAOImpl;
import be.helha.applicine.models.Room;
import be.helha.applicine.models.MovieSession;
import be.helha.applicine.models.Viewable;
import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.models.exceptions.TimeConflictException;
import be.helha.applicine.views.managerviews.SessionManagerViewController;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//ecoute les changements de la liste de films et de la liste de séances de l'app MovieManagerApp

public class SessionManagerApp extends ManagerController implements SessionManagerViewController.SessionManagerViewListener, InvalidationListener {

    private ManagerController parentController;

    private FXMLLoader sessionManagerFxmlLoader;

    private SessionManagerViewController sessionManagerViewController;

    protected List<Room> roomList;

    protected List<MovieSession> movieSessionList;

    private RoomDAOImpl roomDAO;
    private SessionDAOImpl sessionDAO;

    private List<Viewable> viewableList;

    /**
     * Constructor, super calls ManagerController constructor which initializes the movieDAO and fetches all the movies from the database.
     * It also fetches all the rooms and all the sessions from the database.
     * @throws SQLException if there is an error with the database connection, created in ManagerController.
     */
    public SessionManagerApp() throws SQLException {
        super();
        roomDAO = new RoomDAOImpl();
        sessionDAO = new SessionDAOImpl();
        viewableDAO = new ViewableDAOImpl();
        try {
            this.roomList = roomDAO.getAllRooms();
            this.viewableList = viewableDAO.getAllViewables();
            this.movieSessionList = sessionDAO.getAllSessions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Starts the session manager view.
     * Movies on the side.
     * @param adminPage the stage of the view.
     */

    @Override
    public void start(Stage adminPage) {
        sessionManagerFxmlLoader = parentController.getSessionManagerFXML();
        sessionManagerViewController = sessionManagerFxmlLoader.getController();
        sessionManagerViewController.setListener(this);
        sessionManagerViewController.init();
        try {
            for (MovieSession movieSession : movieSessionList) {
                sessionManagerViewController.createDisplaySessionButton(movieSession);
                System.out.println(movieSession.getId());
            }
        } catch (NullPointerException e) {

        }
        sessionManagerViewController.displaySessions();
    }

    /**
     * Sets the parent controller
     * @param managerController the parent controller (ManagerController type)
     */
    public void setParentController(ManagerController managerController) {
        this.parentController = managerController;
    }

    /**
     * Adds a new session to the database or modify the selected session.
     * @param sessionId the id of the session
     * @param movieId the id of the movie
     * @param roomId the id of the room
     * @param version the version of the movie
     * @param convertedDateTime the date and time of the session
     * @param currentEditType the type of the edit (add or modify)
     * @throws InvalideFieldsExceptions if the fields are invalid (empty or wrong format)
     */

    @Override
    public void onValidateButtonClick(Integer sessionId, Integer movieId, Integer roomId, String version, String convertedDateTime, String currentEditType) throws InvalideFieldsExceptions, SQLException {
        try {
            validateFields(sessionId, movieId, roomId, version, convertedDateTime);
        } catch (InvalideFieldsExceptions e) {
            parentController.showAlert(Alert.AlertType.ERROR, "Erreur", "Champs invalides", e.getMessage());
            return;
        } catch (TimeConflictException e) {
            parentController.showAlert(Alert.AlertType.ERROR, "Erreur", "Conflit d'horaire", e.getMessage());
            sessionManagerViewController.highlightConflictingSessions(e.getConflictingSessionsIds());
            return;
        }
        if (currentEditType.equals("add")) {
            sessionDAO.addSession(movieId, roomId, convertedDateTime, version);
        } else if (currentEditType.equals("modify")) {
            sessionDAO.updateSession(sessionId, movieId, roomId, convertedDateTime, version);
        }
        movieSessionList = sessionDAO.getAllSessions();
        refreshSessionManager();
    }

    /**
     * Ensure that all fields are filled and in the correct format.
     *
     * @param
     * @param roomId
     * @param version
     * @param convertedDateTime
     * @throws InvalideFieldsExceptions
     */

    public void validateFields(Integer sessionID, Integer viewableId, Integer roomId, String version, String convertedDateTime) throws InvalideFieldsExceptions, TimeConflictException, SQLException {
        if (viewableId == -1 || roomId == null || version == null || convertedDateTime.isEmpty() || !(convertedDateTime.contains(":"))) {
            throw new InvalideFieldsExceptions("Tous les champs n'ont pas été remplis");
        } else {
            List<Integer> sessionsWithConflict = sessionDAO.checkTimeConflict(sessionID, roomId, convertedDateTime, viewableDAO.getViewableById(viewableId).getTotalDuration());

            if (!sessionsWithConflict.isEmpty()) {
                throw new TimeConflictException("Il y a un conflit d'horaire avec une autre séance", sessionsWithConflict);
            }
        }
    }

    /**
     * Sets the possible movies names that can be selected in the view.
     */
    @Override
    public void setPossibleMovies() {
        sessionManagerViewController.clearPossibleNames();
        for (Viewable v : viewableList) {
            sessionManagerViewController.addPossibleName(v.getTitle());
        }
    }

    /**
     * Returns the duration of a movie from an id in the database.
     * @param id id from the view
     * @return the duration of the movie
     */
    @Override
    public Integer getMovieDuration(int id) {
        Viewable v = viewableDAO.getViewableById(id);
        int duration = v.getTotalDuration();
        return duration;
    }

    /**
     * Sets the possible rooms that can be selected in the view.
     */
    @Override
    public void setPossibleRooms() {
        for (Room r : roomList) {
            sessionManagerViewController.addPossibleRoom(r.getNumber());
        }
    }

    /**
     * Returns the viewable from the current selection in the view.
     *
     * @param currentSelection
     * @return
     */

    public Viewable getViewableFrom(Integer currentSelection) {
        return viewableList.get(currentSelection);
    }

    /**
     * Returns the room from the current selection in the view.
     *
     * @param value
     * @throws SQLException
     */
    @Override
    public void onRoomSelectedEvent(Integer value) throws SQLException {
        if (value == null) {
            return;
        }
        Room room = getRoomFrom(value);
        int capacity = room.getCapacity();
        sessionManagerViewController.setRoomCapacity(capacity);
    }

    /**
     * Deletes a session from the database.
     *
     * @param currentSessionID
     */
    @Override
    public void onDeleteButtonClick(int currentSessionID) {
        try {
            boolean confirmed = parentController.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Suppression", "Voulez-vous vraiment supprimer cette séance ?");
            if (confirmed) {
                sessionDAO.removeSession(currentSessionID);
                movieSessionList = sessionDAO.getAllSessions();
                refreshSessionManager();
            }
        }catch (SQLException e){
            parentController.popUpAlert("Erreur de connexion à la base de données. Mais ça va changer donc trql.");
        }
    }

    @Override
    public MovieSession getMovieSessionById(int id) {
        return movieSessionList.get(id);
    }

    /**
     * Returns a room from an index.
     *
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
        try {
            for (MovieSession movieSession : movieSessionList) {
                sessionManagerViewController.createDisplaySessionButton(movieSession);
            }
        } catch (NullPointerException ignored) {

        }
        sessionManagerViewController.displaySessions();
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
        try {
            this.movieSessionList = sessionDAO.getAllSessions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.viewableList = viewableDAO.getAllViewables();
        refreshSessionManager();
        setPossibleMovies();

        System.out.println("SessionManagerApp invalidated");

    }
}
