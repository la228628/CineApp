package be.helha.applicine.client.controllers.managercontrollers;

import be.helha.applicine.client.controllers.MasterApplication;
import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.request.*;
import be.helha.applicine.common.models.Room;
import be.helha.applicine.common.models.MovieSession;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.common.models.exceptions.TimeConflictException;
import be.helha.applicine.client.views.managerviews.SessionManagerViewController;
import javafx.application.Platform;
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

    private ServerRequestHandler serverRequestHandler;

    private int capacity;
    /**
     * Constructor, super calls ManagerController constructor which initializes the movieDAO and fetches all the movies from the database.
     * It also fetches all the rooms and all the sessions from the database.
     *
     * @throws SQLException if there is an error with the database connection, created in ManagerController.
     */
    public SessionManagerApp(MasterApplication parentController) throws SQLException, IOException, ClassNotFoundException {
        super(parentController);
        this.serverRequestHandler = ServerRequestHandler.getInstance();
        serverRequestHandler.sendRequest(new GetRoomsRequest());
        serverRequestHandler.sendRequest(new GetViewablesRequest());
        serverRequestHandler.sendRequest(new GetAllSessionRequest());
    }

    public SessionManagerApp() throws IOException, ClassNotFoundException {
        super();
    }


    /**
     * Starts the session manager view.
     * Movies on the side.
     *
     * @param adminPage the stage of the view.
     */
    @Override
    public void start(Stage adminPage) {
        sessionManagerFxmlLoader = parentController.getSessionManagerFXML();
        sessionManagerViewController = sessionManagerFxmlLoader.getController();
        sessionManagerViewController.setListener(this);
        serverRequestHandler.addListener(this);
        sessionManagerViewController.init();
        try {
            for (MovieSession movieSession : movieSessionList) {
                sessionManagerViewController.createDisplaySessionButton(movieSession);
                System.out.println(movieSession.getId());
            }
        } catch (NullPointerException e) {
            AlertViewController.showErrorMessage("Problème d'affichage, la séance n'existe pas. Tentez de vous reconnecter.");
            boolean confirmed = AlertViewController.showConfirmationMessage("Voulez-vous vous reconnecter ?");
            if (confirmed) {
                parentController.toLogin();
            }
        }
        sessionManagerViewController.displaySessions();
    }

    /**
     * Sets the parent controller
     *
     * @param managerController the parent controller (ManagerController type)
     */
    public void setParentController(ManagerController managerController) {
        this.parentController = managerController;
    }

    /**
     * Adds a new session to the database or modify the selected session.
     *
     * @param sessionId         the id of the session
     * @param movieId           the id of the movie
     * @param roomId            the id of the room
     * @param version           the version of the movie
     * @param convertedDateTime the date and time of the session
     * @param currentEditType   the type of the edit (add or modify)
     * @throws InvalideFieldsExceptions if the fields are invalid (empty or wrong format)
     */
    @Override
    public void onValidateButtonClick(Integer sessionId, Integer movieId, Integer roomId, String version, String convertedDateTime, String currentEditType) {
        try {
            validateFields(sessionId, movieId, roomId, version, convertedDateTime);

            if (currentEditType.equals("add")) {
                serverRequestHandler.sendRequest(new AddSessionRequest(new MovieSession(sessionId, viewableList.get(movieId), convertedDateTime, getRoomByNumber(roomId), version)));
            } else if (currentEditType.equals("modify")) {
                serverRequestHandler.sendRequest(new UpdateSessionRequest(new MovieSession(sessionId, viewableList.get(movieId), convertedDateTime, getRoomByNumber(roomId), version)));
            }
//            serverRequestHandler.sendRequest(new GetAllSessionRequest());
//            refreshSessionManager();
        } catch (InvalideFieldsExceptions | IOException e) {
            AlertViewController.showErrorMessage("Champs invalides : " + e.getMessage());
        } catch (TimeConflictException e) {
            AlertViewController.showErrorMessage("Conflit d'horaire avec une autre séance.");
            sessionManagerViewController.highlightConflictingSessions(e.getConflictingSessionsIds());
        } catch (SQLException e) {
            AlertViewController.showInfoMessage("Impossible de modifier la séance, erreur avec la base de données. Verification de la connection au serveur.");
            try {
                validateFields(sessionId, movieId, roomId, version, convertedDateTime);
            } catch (InvalideFieldsExceptions | SQLException | TimeConflictException ex) {
                AlertViewController.showErrorMessage("Problème de connection avec le serveur. Veuillez redémarrer l'application et le serveur.");
            }
        }
    }

    /**
     * Ensure that all fields are filled and in the correct format.
     *
     * @param sessionID         the id of the session
     * @param viewableId        the id of the movie
     * @param roomId            the id of the room
     * @param version           the version of the movie
     * @param convertedDateTime the date and time of the session
     * @throws InvalideFieldsExceptions if the fields are invalid (empty or wrong format)
     */
    public void validateFields(Integer sessionID, Integer viewableId, Integer roomId, String version, String convertedDateTime) throws InvalideFieldsExceptions, TimeConflictException, SQLException {
        if (viewableId == -1 || roomId == null || version == null || !(convertedDateTime.contains(":"))) {
            throw new InvalideFieldsExceptions("Tous les champs n'ont pas été remplis");
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
     *
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
        getRoomFrom(value);
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
                serverRequestHandler.sendRequest(new DeleteSessionRequest(currentSessionID));
                serverRequestHandler.sendRequest(new GetAllSessionRequest());
                refreshSessionManager();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public MovieSession getMovieSessionById(int id) {
        return movieSessionList.get(id);
    }

    /**
     * Returns a room from an index.
     *
     * @param index the index of the room in the list.
     * @return the room by the id.
     */
    public void getRoomFrom(int index) {
        try {
            serverRequestHandler.sendRequest(new GetRoomByIdRequest(index));
        } catch (IOException e) {
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
        try {
            serverRequestHandler.sendRequest(new GetAllSessionRequest());
            serverRequestHandler.sendRequest(new GetViewablesRequest());
            refreshSessionManager();
            setPossibleMovies();
            System.out.println("SessionManagerApp invalidated");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public Room getRoomById(int id) {
        return roomList.get(id);
    }

    @Override
    public void visit(GetAllSessionRequest getAllSessionRequest) {
        movieSessionList = getAllSessionRequest.getSessions();
        Platform.runLater(this::refreshSessionManager);
    }

    @Override
    public void visit(GetRoomsRequest getRoomsRequest) {
        roomList = getRoomsRequest.getRooms();
    }

    @Override
    public void visit(GetViewablesRequest getViewablesRequest) {
        viewableList = getViewablesRequest.getViewables();
        Platform.runLater(this::setPossibleMovies);
    }

    public void visit(AddSessionRequest addSessionRequest){
        if(addSessionRequest.getSuccess()){
            Platform.runLater(() -> {
                try {
                    AlertViewController.showInfoMessage("Séance ajoutée avec succès.");
                    serverRequestHandler.sendRequest(new GetAllSessionRequest());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                refreshSessionManager();
            });
        } else {
            Platform.runLater(() -> {
                AlertViewController.showErrorMessage(addSessionRequest.getMessage());
            });
        }
    }

    public void visit(UpdateSessionRequest updateSessionRequest){
        if(updateSessionRequest.getSuccess()){
            Platform.runLater(() -> {
                try {
                    AlertViewController.showInfoMessage("Séance modifiée avec succès.");
                    serverRequestHandler.sendRequest(new GetAllSessionRequest());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                refreshSessionManager();
            });
        } else {
            Platform.runLater(() -> {
                AlertViewController.showErrorMessage(updateSessionRequest.getMessage());
            });
        }
    }



    public Room getRoomByNumber(int number){
        for(Room room : roomList){
            if(room.getNumber() == number){
                return room;
            }
        }
        return null;
    }



}