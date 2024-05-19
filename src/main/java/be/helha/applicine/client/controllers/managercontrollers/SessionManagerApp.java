package be.helha.applicine.client.controllers.managercontrollers;

import be.helha.applicine.client.controllers.MasterApplication;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.request.*;
import be.helha.applicine.common.models.Room;
import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.common.models.exceptions.TimeConflictException;
import be.helha.applicine.client.views.managerviews.SessionManagerViewController;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//ecoute les changements de la liste de films et de la liste de séances de l'app MovieManagerApp

public class SessionManagerApp extends ManagerController implements SessionManagerViewController.SessionManagerViewListener, InvalidationListener {

    private ManagerController parentController;

    private FXMLLoader sessionManagerFxmlLoader;

    private SessionManagerViewController sessionManagerViewController;

    protected List<Room> roomList;

    protected List<MovieSession> movieSessionList;

    private List<Viewable> viewableList;

    /**
     * Constructor, super calls ManagerController constructor which initializes the movieDAO and fetches all the movies from the database.
     * It also fetches all the rooms and all the sessions from the database.
     * @throws SQLException if there is an error with the database connection, created in ManagerController.
     */
    public SessionManagerApp(MasterApplication parentController) throws SQLException, IOException, ClassNotFoundException {
        super(parentController);
        try {
            this.roomList = (List<Room>) getServerRequestHandler().sendRequest(new GetRoomsRequest());
            this.viewableList = (ArrayList<Viewable>) getServerRequestHandler().sendRequest(new GetViewablesRequest());
            this.movieSessionList = (List<MovieSession>) getServerRequestHandler().sendRequest(new GetAllSessionRequest());
        } catch (IOException | ClassNotFoundException e) {
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
            AlertViewController.showErrorMessage("Champs invalides"+ e.getMessage());
            return;
        } catch (TimeConflictException e) {
            AlertViewController.showErrorMessage("Conflit d'horaire avec une autre séance");
            sessionManagerViewController.highlightConflictingSessions(e.getConflictingSessionsIds());
            return;
        }
        if (currentEditType.equals("add")) {
            try {
                getServerRequestHandler().sendRequest(new AddSessionRequest(new MovieSession(sessionId, viewableList.get(movieId), convertedDateTime, getRoomById(roomId), version)));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (currentEditType.equals("modify")) {
            try {
                getServerRequestHandler().sendRequest(new UpdateSessionRequest(new MovieSession(sessionId, viewableList.get(movieId), convertedDateTime, getRoomById(roomId), version)));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            movieSessionList = (List<MovieSession>) getServerRequestHandler().sendRequest(new GetAllSessionRequest());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
//            List<Integer> sessionsWithConflict = sessionDAO.checkTimeConflict(sessionID, roomId, convertedDateTime, viewableDAO.getViewableById(viewableId).getTotalDuration());
//
//            if (!sessionsWithConflict.isEmpty()) {
//                throw new TimeConflictException("Il y a un conflit d'horaire avec une autre séance", sessionsWithConflict);
//            }
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
        Viewable v = viewableList.get(id);
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
            boolean confirmed = AlertViewController.showConfirmationMessage("Voulez-vous vraiment supprimer cette séance ?");
            if (confirmed) {
                getServerRequestHandler().sendRequest(new DeleteSessionRequest(currentSessionID));
                try {
                    movieSessionList = (List<MovieSession>) getServerRequestHandler().sendRequest(new GetAllSessionRequest());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                refreshSessionManager();
            }
        }catch (IOException | ClassNotFoundException e){
            AlertViewController.showErrorMessage("Impossible de supprimer cette séance");
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
        try {
            return (Room) getServerRequestHandler().sendRequest(new GetRoomByIdRequest(index));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            this.movieSessionList = (List<MovieSession>) getServerRequestHandler().sendRequest(new GetAllSessionRequest());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            this.viewableList = (ArrayList<Viewable>) getServerRequestHandler().sendRequest(new GetViewablesRequest());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        refreshSessionManager();
        setPossibleMovies();
        System.out.println("SessionManagerApp invalidated");

    }

    public Room getRoomById(int id){
        try {
            return (Room) getServerRequestHandler().sendRequest(new GetRoomByIdRequest(id));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
