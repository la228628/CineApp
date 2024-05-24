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

    //constructor de la classe SpecialViewableController qui initialise les attributs de la classe et les listeners de la vue
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

    public void setParentController(ManagerController parentController) {
        this.parentController = parentController;
    }

    //methode d'initialisation de la vue (remplissage des listes, des combobox, etc)
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

    @Override
    public ArrayList<String> getMovieTitleList() {
        return this.movieTitleList;
    }


    @Override
    public void onAddMovieButtonClick() {
        if (selectedMovies != null && !addedMovies.contains(selectedMovies)) {
            addedMovies.add(selectedMovies);
            specialViewableViewController.fillAddedMovieChoice(getAddedViewablesTitles(), getTotalDuration());
        }
    }

    List<String> getAddedViewablesTitles() {
        List<String> addedViewablesTitles = new ArrayList<>();
        for (Viewable viewable : addedMovies) {
            addedViewablesTitles.add(viewable.getTitle());
        }
        return addedViewablesTitles;
    }

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

    @Override
    public void onMovieChoising(int selectedIndex) {
        selectedMovies = movieList.get(selectedIndex);
        System.out.println("Film choisi: " + selectedMovies.getTitle());
    }

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

    private void modifySagaInDB(int id, String name, String type, ArrayList<Integer> addedMoviesIds) {
        ArrayList<Movie> movies = getMoviesByIDs(addedMoviesIds);
        try {
            serverRequestHandler.sendRequest(new UpdateViewableRequest(new Saga(id, name, null, null, getTotalDuration(), null, null, null, movies)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ArrayList<Integer> getAddedMoviesIds() {
        ArrayList<Integer> addedMoviesIds = new ArrayList<>();
        for (Movie movie : addedMovies) {
            addedMoviesIds.add(movie.getId());
        }
        return addedMoviesIds;
    }

    private void addSagaIntoDB(String name, String type, ArrayList<Integer> addedMoviesIds) {
        ArrayList<Movie> movies = getMoviesByIDs(addedMoviesIds);
        try {
            serverRequestHandler.sendRequest(new AddViewableRequest(new Saga(0, name, null, null, getTotalDuration(), null, null, null, movies)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    private void validateFields(String name) throws InvalideFieldsExceptions {
        if (addedMovies.size() < 2 || name.isEmpty()) {
            throw new InvalideFieldsExceptions("Certains champs n'ont pas été remplis correctement");
        }
    }

    @Override
    public void onCancelButtonClick() {
        boolean confirm = AlertViewController.showConfirmationMessage("Voulez-vous vraiment quitter ?");
        if (confirm) {
            specialViewableViewController.onCancelConfirm();
        }
    }

    @Override
    public void displaySagas() {
        ArrayList<Viewable> viewables = new ArrayList<>();
        try {
            serverRequestHandler.sendRequest(new GetViewablesRequest());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSagaDisplayButtonClick(Viewable viewable) {
        this.selectedSaga = viewable;
        currentEditType = "modify";
        addedMovies = new ArrayList<>();
        addedMovies.addAll(((Saga) viewable).getMovies());
        movieTitleList = new ArrayList<>();
        specialViewableViewController.showSagaForEdit(viewable.getTitle(), getAddedViewablesTitles(), getTotalDuration());
    }

    @Override
    public void onAddSagaButtonClick() {
        this.currentEditType = "add";
        addedMovies = new ArrayList<>();
        specialViewableViewController.prepareForAddSaga();
    }

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

    private Integer getTotalDuration() {
        int totalDuration = 0;
        for (Movie movie : addedMovies) {
            totalDuration += movie.getDuration();
        }
        return totalDuration;
    }


    @Override
    public void addListener(InvalidationListener invalidationListener) {
        specialViewablesChangeListener = invalidationListener;
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        specialViewablesChangeListener = null;

    }

    private void notifyListeners() {
        if (specialViewablesChangeListener != null) {
            specialViewablesChangeListener.invalidated(this);
        }
    }

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

    @Override
    public void visit(GetViewablesRequest getViewablesRequest) {
        viewableList = getViewablesRequest.getViewables();
        System.out.println("taille de la liste des viewables: " + viewableList.size());
        Platform.runLater(() -> specialViewableViewController.clearSagaList());
        for (Viewable viewable : viewableList) {
            if (viewable instanceof Saga) {
                System.out.println(viewable.getTitle());
                Platform.runLater(() -> specialViewableViewController.displaySaga(viewable));
            }
        }
    }

    @Override
    public void visit(GetMoviesRequest getMoviesRequest) {
        this.movieList = getMoviesRequest.getMovies();
        synchronized (lock) {
            lock.notify();  // Notifier que la réponse est arrivée
        }
        System.out.println("Liste vide: " + movieList.isEmpty());
    }

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