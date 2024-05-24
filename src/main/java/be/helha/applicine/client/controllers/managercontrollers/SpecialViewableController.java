package be.helha.applicine.client.controllers.managercontrollers;

import be.helha.applicine.client.controllers.MasterApplication;
import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Saga;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.client.views.managerviews.SpecialViewableViewController;
import be.helha.applicine.common.models.request.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * SpecialViewableController class is the controller class for the SpecialViewable view.
 * It is responsible for managing the sagas.
 * Only the manager can access this view and manage the sagas.
 * It extends ManagerController.
 *
 * It is an observer of the serverRequestHandler.
 *
 * It implements the SpecialViewableViewController.SpecialViewableListener, Observable, InvalidationListener interfaces.
 *
 * It is an observer for the MovieManagerController and will be notified when a movie is added, modified or removed and will update the view accordingly.
 * It is an observable for the SessionManagerController and will notify the SessionManagerController when a saga is added, modified or removed.
 */
public class SpecialViewableController extends ManagerController implements SpecialViewableViewController.SpecialViewableListener, Observable, InvalidationListener {

    private ManagerController parentController;
    public FXMLLoader specialViewableFxmlLoader;
    public SpecialViewableViewController specialViewableViewController;
    protected ArrayList<String> movieTitleList;
    private Movie selectedMovies = null;

    private List<Movie> movieList = new ArrayList<>();

    public Viewable selectedSaga = null;

    private List<Movie> addedMovies = new ArrayList<>();

    private String currentEditType = "add";

    private InvalidationListener specialViewablesChangeListener;

    private ServerRequestHandler serverRequestHandler;

    private final Object lock = new Object();


    /**
     * Constructor of the SpecialViewableController.
     * It initializes the specialViewableFxmlLoader and the specialViewableViewController.
     * It adds the listener to the serverRequestHandler.
     * It fetches all the movies from the database to movieList.
     * @param parentController
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public SpecialViewableController(MasterApplication parentController) throws SQLException, IOException, ClassNotFoundException {
        super(parentController);
        movieTitleList = new ArrayList<>();
        this.serverRequestHandler = ServerRequestHandler.getInstance();
        specialViewableFxmlLoader = new FXMLLoader(SpecialViewableViewController.getFXMLResource());
        specialViewableFxmlLoader.load();
        specialViewableViewController = specialViewableFxmlLoader.getController();
        specialViewableViewController.setListener(this);
        serverRequestHandler.addListener(this);
    }

    /**
     * Setter of the parentController.
     * @param parentController
     */
    public void setParentController(ManagerController parentController) {
        this.parentController = parentController;
    }

    /**
     * Starts the SpecialViewable view.
     * This method initializes the SpecialViewableViewController, sets the current window of the parent controller,
     * sends a request to fetch all the movies from the database and waits for the response.
     * The lock object is used to synchronize the sending of the request and the receiving of the response.
     * When the request is sent, the current thread waits (via lock.wait()) until it is notified that the response has arrived.
     * @param adminPage the stage of the view.
     * @throws SQLException
     * @throws IOException
     */
    @Override
    public void start(Stage adminPage) throws SQLException, IOException {
        specialViewableFxmlLoader = parentController.getSpecialViewableFXML();
        specialViewableViewController = specialViewableFxmlLoader.getController();
        specialViewableViewController.setListener(this);

        System.out.println("Liste des films vide: " + movieList.isEmpty());

        try {
            serverRequestHandler.sendRequest(new GetMoviesRequest());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        synchronized (lock) {
            try {
                lock.wait();  // Attendre la notification de la réponse
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Platform.runLater(() -> {
            try {
                specialViewableViewController.init();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     *Getter of the movieTitleList , the list used to display the movies of a saga in the view.
     * @return the movieTitleList.
     */
    @Override
    public ArrayList<String> getMovieTitleList() {
        return this.movieTitleList;
    }


    /**
     * Adds a movie to the list of added movies.
     * If the movie is not already in the list, it is added.
     * The total duration of the added movies is updated to know the total duration of the current created / modified saga.
     */
    @Override
    public void onAddMovieButtonClick() {
        if (selectedMovies != null && !addedMovies.contains(selectedMovies)) {
            addedMovies.add(selectedMovies);
            specialViewableViewController.fillAddedMovieChoice(getAddedViewablesTitles(), getTotalDuration());
        }
    }

    /**
     * Getter of the added movies titles.
     * @return the list of the titles of the added movies.
     */
    List<String> getAddedViewablesTitles() {
        List<String> addedViewablesTitles = new ArrayList<>();
        for (Viewable viewable : addedMovies) {
            addedViewablesTitles.add(viewable.getTitle());
        }
        return addedViewablesTitles;
    }

    /**
     * Removes a movie from the list of added movies.
     * If the movie is in the list, it is removed.
     * If the list is empty, the last movie added is removed.
     * The total duration of the added movies is updated to know the total duration of the current created / modified saga.
     */
    @Override
    public void onRemoveMovieButtonClick() {
        if (selectedMovies != null && addedMovies.contains(selectedMovies)) {
            addedMovies.remove(selectedMovies);
        } else {
            try {
                addedMovies.removeLast();
            } catch (NoSuchElementException ignored) {

            }
        }
        specialViewableViewController.fillAddedMovieChoice(getAddedViewablesTitles(), getTotalDuration());
    }

    /**
     * Displays the possible movies to add to a saga.
     * It sends a request to the server to get all the movies from the database.
     *It updates the possible movies for a saga in the view.
     */
    @Override
    public void displayAllMovie() {
        GetMoviesRequest request = new GetMoviesRequest();
        try {
            movieTitleList.clear();
            serverRequestHandler.sendRequest(request);


            for (Movie movie : movieList) {
                System.out.println(movie.getTitle());
                movieTitleList.add(movie.getTitle());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * set the selected movie desired to add to/ remove from the saga.
     * @param selectedIndex
     */
    @Override
    public void onMovieChoising(int selectedIndex) {
        selectedMovies = movieList.get(selectedIndex);
        System.out.println("Film choisi: " + selectedMovies.getTitle());
    }

    /**
     * Validates the fields of the saga to add/modify.
     * If the fields are not filled correctly, an exception is thrown.
     * If the fields are filled correctly, the saga is added/modified in the database.
     * An info message is displayed to confirm the success of the operation.
     * @param name
     * @throws SQLException
     */
    @Override
    public void onValidateButtonClick(String name) throws SQLException {
        try {
            validateFields(name);
            if (this.currentEditType.equals("add"))
                addSagaIntoDB(name, "Saga", getAddedMoviesIds());
            else if (this.currentEditType.equals("modify"))
                modifySagaInDB(this.selectedSaga.getId(), name, "Saga", getAddedMoviesIds());
            AlertViewController.showInfoMessage("La saga a été ajoutée/modifiée avec succès");
        } catch (InvalideFieldsExceptions e) {
            AlertViewController.showErrorMessage("Certains champs n'ont pas été remplis correctement: " + e.getMessage());
        }
        specialViewableViewController.refresh();
        notifyListeners(); //Permettra aux sessions de disposer des nouvelles sagas/ supprimer les anciennes
    }

    /**
     * Send a request to the server to update the saga in the database.
     * @param id
     * @param name
     * @param type
     * @param addedMoviesIds
     */
    private void modifySagaInDB(int id, String name, String type, ArrayList<Integer> addedMoviesIds) {
        ArrayList<Movie> movies = getMoviesByIDs(addedMoviesIds);
        try {
            serverRequestHandler.sendRequest(new UpdateViewableRequest(new Saga(id, name, null, null, getTotalDuration(), null, null, null, movies)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter of the added movies ids.
     * @return
     */
    ArrayList<Integer> getAddedMoviesIds() {
        ArrayList<Integer> addedMoviesIds = new ArrayList<>();
        for (Movie movie : addedMovies) {
            addedMoviesIds.add(movie.getId());
        }
        return addedMoviesIds;
    }

    /**
     * Send a request to the server to add the saga in the database.
     * @param name
     * @param type
     * @param addedMoviesIds
     */
    private void addSagaIntoDB(String name, String type, ArrayList<Integer> addedMoviesIds) {
        ArrayList<Movie> movies = getMoviesByIDs(addedMoviesIds);
        try {
            serverRequestHandler.sendRequest(new AddViewableRequest(new Saga(0, name, null, null, getTotalDuration(), null, null, null, movies)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter of the movies by their ids.
     * @param addedMoviesIds
     * @return
     */
    @NotNull
    private ArrayList<Movie> getMoviesByIDs(ArrayList<Integer> addedMoviesIds) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int id : addedMoviesIds) {
            for (Movie movie : movieList) {
                if (movie.getId() == id) {
                    movies.add(movie);
                }
            }
        }
        return movies;
    }

    /**
     * Validates the fields of the saga to add/modify.
     * If the fields are not filled correctly, an exception is thrown.
     * @param name
     * @throws InvalideFieldsExceptions
     */
    private void validateFields(String name) throws InvalideFieldsExceptions {
        if (addedMovies.size() < 2 || name.isEmpty()) {
            throw new InvalideFieldsExceptions("Certains champs n'ont pas été remplis correctement");
        }
    }

    /**
     * Cancel the operation of adding/modifying a saga.
     * A confirmation message is displayed to confirm the operation.
     * If the user confirms, the operation is cancelled and the view is refreshed and the edit mode is cancelled.
     *
     */
    @Override
    public void onCancelButtonClick() {
        boolean confirm = AlertViewController.showConfirmationMessage("Voulez-vous vraiment quitter ?");
        if (confirm) {
            specialViewableViewController.onCancelConfirm();
        }
    }

    /**
     * Send a request to the server to get all the sagas from the database.
     * The server will send a response with all the sagas.
     */
    @Override
    public void displaySagas() {
        try {
            serverRequestHandler.sendRequest(new GetViewablesRequest());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the saga in the view to modify.
     * Set the current edit type to modify.
     * @param viewable
     */
    @Override
    public void onSagaDisplayButtonClick(Viewable viewable) {
        this.selectedSaga = viewable;
        currentEditType = "modify";
        addedMovies = new ArrayList<>();
        addedMovies.addAll(((Saga) viewable).getMovies());
        movieTitleList = new ArrayList<>();
        specialViewableViewController.showSagaForEdit(viewable.getTitle(), getAddedViewablesTitles(), getTotalDuration());
    }

    /**
     * Prepare the view to add a saga.
     * Set the current edit type to add.
     */
    @Override
    public void onAddSagaButtonClick() {
        this.currentEditType = "add";
        addedMovies = new ArrayList<>();
        specialViewableViewController.prepareForAddSaga();
    }

    /**
     * Send a request to the server to delete the saga from the database.
     * A confirmation message is displayed to confirm the operation.
     * If the user confirms, the request is sent to the server.
     */
    @Override
    public void onSagaDeleteButtonClick() throws SQLException {
        boolean confirm = AlertViewController.showConfirmationMessage("Voulez vous vraiment supprimer cette saga ?");
        if (confirm) {
            try {
                serverRequestHandler.sendRequest(new DeleteViewableRequest(selectedSaga.getId()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Getter of the total duration of the added movies for the current added/ modified saga.
     * @return
     */
    private Integer getTotalDuration() {
        int totalDuration = 0;
        for (Movie movie : addedMovies) {
            totalDuration += movie.getDuration();
        }
        return totalDuration;
    }


    /**
     * Add an InvalidationListener to the SpecialViewableController (observer).
     * @param invalidationListener
     */

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        specialViewablesChangeListener = invalidationListener;
    }

    /**
     * Remove an InvalidationListener from the SpecialViewableController (observer).
     * @param invalidationListener
     */
    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        specialViewablesChangeListener = null;

    }

    /**
     * Notify the listeners that the viewables (saga) have been updated.
     */
    private void notifyListeners() {
        if (specialViewablesChangeListener != null) {
            specialViewablesChangeListener.invalidated(this);
        }
    }

    /**
     * Update the viewables (sagas) in the view.
     * If a movie is added, modified or removed, the view is updated.
     * The function send a request to the server to get all the sagas from the database.
     * The lock object is used to synchronize the sending of the request and the receiving of the response.
     * When the request is sent, the current thread waits (via lock.wait()) until it is notified that the response has arrived.
     * When the response is received, the view is updated.
     *
     * @param observable
     */
    @Override
    public void invalidated(Observable observable) {
        try {
            serverRequestHandler.sendRequest(new GetMoviesRequest());

            synchronized (lock) {
                lock.wait();  // Attendre la notification de la réponse
            }


            specialViewableViewController.fillMovieChoice();
        } catch (SQLException | IOException error) {
            AlertViewController.showErrorMessage("Erreur lors de la récupération des films. Essaie de la connection au serveur.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Update the viewables (sagas) in the view.
     * The function send a request to the server to get all the sagas from the database.
     * When the response is received, the viewableList is updated and the sagas are displayed in the view.
     *
     * @param getViewablesRequest
     */
    @Override
    public void visit(GetViewablesRequest getViewablesRequest) {
        viewableList = getViewablesRequest.getViewables();
        Platform.runLater(() -> specialViewableViewController.clearSagaList());
        for (Viewable viewable : viewableList) {
            if (viewable instanceof Saga) {
                System.out.println(viewable.getTitle());
                Platform.runLater(() -> specialViewableViewController.displaySaga(viewable));
            }
        }
    }

    /**
     * Update the movies in the view.
     * The function send a request to the server to get all the movies from the database.
     * When the response is received, the movieList is updated.
     *
     * @param getMoviesRequest
     */
    @Override
    public void visit(GetMoviesRequest getMoviesRequest) {
        this.movieList = getMoviesRequest.getMovies();
        synchronized (lock) {
            lock.notify();  // Notifier que la réponse est arrivée
        }
        System.out.println("Liste vide: " + movieList.isEmpty());
    }

    /**
     * Send a request to the server to delete the saga from the database.
     * If the request is successful, the view is updated and a confirmation message is displayed.
     * and the view is updated.
     * onCancelConfirm() is called to cancel the edit mode.
     * If the request is not successful, an error message is displayed.
     *
     * @param deleteViewableRequest
     */
    @Override
    public void visit(DeleteViewableRequest deleteViewableRequest) {
        if (deleteViewableRequest.getSuccess()) {
            Platform.runLater(() -> {
                try {
                    specialViewableViewController.refresh();
                    specialViewableViewController.onCancelConfirm();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                notifyListeners();
                AlertViewController.showInfoMessage("La saga a été supprimée avec succès");
            });
        } else {
            Platform.runLater(() -> AlertViewController.showErrorMessage(deleteViewableRequest.getMessage()));
        }
    }

    @Override
    public void visit(ErrorMessage errorMessage) {
        Platform.runLater(() -> {
            AlertViewController.showErrorMessage(errorMessage.getMessage());
        });
    }
}