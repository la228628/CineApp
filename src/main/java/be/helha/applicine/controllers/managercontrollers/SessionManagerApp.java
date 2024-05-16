package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.dao.impl.RoomDAOImpl;
import be.helha.applicine.dao.impl.SessionDAOImpl;
import be.helha.applicine.models.Room;
import be.helha.applicine.models.MovieSession;
import be.helha.applicine.models.Visionable;
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

    /**
     * Constructor, super calls ManagerController constructor which initializes the movieDAO and fetches all the movies from the database.
     * It also fetches all the rooms and all the sessions from the database.
     * @throws SQLException if there is an error with the database connection, created in ManagerController.
     */
    public SessionManagerApp() throws SQLException {
        super();
        roomDAO = new RoomDAOImpl();
        sessionDAO = new SessionDAOImpl();
        this.roomList = roomDAO.getAllRooms();
        this.movieSessionList = sessionDAO.getAllSessions();
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
        sessionManagerViewController.intialize();
        for (MovieSession movieSession : movieSessionList) {
            sessionManagerViewController.createDisplaySessionButton(movieSession);
            System.out.println(movieSession.getId());
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
    public void onValidateButtonClick(Integer sessionId, Integer movieId, Integer roomId, String version, String convertedDateTime, String currentEditType) throws InvalideFieldsExceptions {
        try {
            validateFields(sessionId, movieId, roomId, version, convertedDateTime);
            if (currentEditType.equals("add")) {
                sessionDAO.addSession(movieId, roomId, convertedDateTime, version);
            } else if (currentEditType.equals("modify")) {
                sessionDAO.updateSession(sessionId, movieId, roomId, convertedDateTime, version);
            }
            movieSessionList = sessionDAO.getAllSessions();
            refreshSessionManager();
        } catch (InvalideFieldsExceptions e) {
            parentController.showAlert(Alert.AlertType.ERROR, "Erreur", "Champs invalides", e.getMessage());
        } catch (TimeConflictException e) {
            parentController.showAlert(Alert.AlertType.ERROR, "Erreur", "Conflit d'horaire", e.getMessage());
            sessionManagerViewController.highlightConflictingSessions(e.getConflictingSessionsIds());
        }catch (SQLException e){
            parentController.popUpAlert("Erreur de connexion à la base de données. Mais ça va changer donc trql.");
        }

    }
    /**
     * Ensure that all fields are filled and in the correct format.
     * @param movieId the id of the movie
     * @param roomId the id of the room
     * @param version the version of the movie
     * @param convertedDateTime the date and time of the session
     * @throws InvalideFieldsExceptions if the fields are invalid (empty or wrong format)
     */
    public void validateFields(Integer sessionID, Integer movieId, Integer roomId, String version, String convertedDateTime) throws InvalideFieldsExceptions, TimeConflictException {
        try {
            if (movieId == -1 || roomId == null || version == null || convertedDateTime.isEmpty() || !(convertedDateTime.contains(":"))) {
                throw new InvalideFieldsExceptions("Tous les champs n'ont pas été remplis");
            } else {
                List<Integer> sessionsWithConflict = sessionDAO.checkTimeConflict(sessionID, roomId, convertedDateTime, movieDAO.getMovieById(movieId).getTotalDuration());

                if (!sessionsWithConflict.isEmpty()) {
                    throw new TimeConflictException("Il y a un conflit d'horaire avec une autre séance", sessionsWithConflict);
                }
            }
        }catch (SQLException e){
            parentController.popUpAlert("Erreur de connexion à la base de données. Mais ça va changer donc trql.");
        }
    }

    /**
     * Sets the possible movies names that can be selected in the view.
     */
    @Override
    public void setPossibleMovies() {
        sessionManagerViewController.clearPossibleNames();
        for (Visionable m : movieList) {
            sessionManagerViewController.addPossibleName(m.getTitle());
        }
    }

    /**
     * Returns the duration of a movie from an id in the database.
     * @param id id from the view
     * @return the duration of the movie
     */
    @Override
    public Integer getMovieDuration(int id) {
        Visionable m = movieDAO.getMovieById(id);
        return m.getTotalDuration();
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
     * Returns the movie from the current selection in the view.
     *
     * @param currentSelection
     * @return
     */
    @Override
    public Visionable getMovieFrom(Integer currentSelection) {
        return super.getMovieFrom(currentSelection);
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
        for (MovieSession movieSession : movieSessionList) {
            sessionManagerViewController.createDisplaySessionButton(movieSession);
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
            this.movieList = movieDAO.getAllMovies();
            refreshSessionManager();
            setPossibleMovies();
        }catch (SQLException e){
            parentController.popUpAlert("Erreur lors de la récupération des films ou des séances");
        }
        System.out.println("SessionManagerApp invalidated");

    }
}
