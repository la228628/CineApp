package be.helha.applicine.client.controllers.managercontrollers;

import be.helha.applicine.client.controllers.MasterApplication;
import be.helha.applicine.client.views.AlertViewController;
import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Saga;
import be.helha.applicine.common.models.Viewable;
import be.helha.applicine.common.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.client.views.managerviews.SpecialViewableViewController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
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
    protected List<String> movieTitleList = new ArrayList<>();
    private Movie selectedMovies = null;

    public Viewable selectedSaga = null;

    private List<Movie> addedMovies = new ArrayList<>();

    private String currentEditType = "add";

    private InvalidationListener specialViewablesChangeListener;

    //constructor de la classe SpecialViewableController qui initialise les attributs de la classe et les listeners de la vue
    public SpecialViewableController(MasterApplication parentController) throws SQLException, IOException, ClassNotFoundException {
        super(parentController);
        specialViewableFxmlLoader = new FXMLLoader(SpecialViewableViewController.getFXMLResource());
        specialViewableFxmlLoader.load();
        specialViewableViewController = specialViewableFxmlLoader.getController();
        specialViewableViewController.setListener(this);
        specialViewableViewController.init();
    }

    public void setParentController(ManagerController parentController) {
        this.parentController = parentController;
    }

    //methode d'initialisation de la vue (remplissage des listes, des combobox, etc)
    @Override
    public void start(Stage adminPage) throws SQLException {
        specialViewableFxmlLoader = parentController.getSpecialViewableFXML();
        specialViewableViewController = specialViewableFxmlLoader.getController();
        specialViewableViewController.setListener(this);
        specialViewableViewController.init();
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
    public ArrayList<String> displayAllMovie() throws SQLException {
        movieTitleList = new ArrayList<>();
        try {
            movieList = (List<Movie>) getServerRequestHandler().sendRequest("GET_MOVIES");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Movie movie : movieList) {
            movieTitleList.add(movie.getTitle());
        }
        return (ArrayList<String>) movieTitleList;
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
            else
                modifySagaInDB(this.selectedSaga.getId(), name, "Saga", getAddedMoviesIds());
            AlertViewController.showInfoMessage("La saga a été ajoutée/modifiée avec succès");
        } catch (InvalideFieldsExceptions e) {
            AlertViewController.showErrorMessage("Certains champs n'ont pas été remplis correctement: " + e.getMessage());
        }
        specialViewableViewController.refresh();
        notifyListeners(); //Permettra aux sessions de disposer des nouvelles sagas/ supprimer les anciennes
    }

    private void modifySagaInDB(int id,  String name,String type, ArrayList<Integer> addedMoviesIds) {
        ArrayList<Movie> movies = getMoviesByIDs(addedMoviesIds);
        try {
            getServerRequestHandler().sendRequest(new Saga(id, name, null, null, getTotalDuration(), null, null, null, movies));
        } catch (IOException | ClassNotFoundException e) {
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
            getServerRequestHandler().sendRequest(new Saga(0, name, null, null, getTotalDuration(), null, null, null, movies));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private ArrayList<Movie> getMoviesByIDs(ArrayList<Integer> addedMoviesIds) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int id : addedMoviesIds) {
            try {
                Movie movie = (Movie) getServerRequestHandler().sendRequest("GET_MOVIE_BY_ID " + id);
                movies.add(movie);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
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
            viewables = (ArrayList<Viewable>) getServerRequestHandler().sendRequest("GET_VIEWABLES");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Viewable viewable : viewables) {
            //si le type dynamique d'un objet viewable est une saga, on l'affiche dans le list view
            if (viewable instanceof Saga) {
                System.out.println(viewable.getTitle());
                specialViewableViewController.displaySaga(viewable);
            }
        }
        specialViewableViewController.addAddButton();
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
            String request;
            try {
                request = getServerRequestHandler().sendRequest("DELETE_VIEWABLE " + selectedSaga.getId()).toString();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (request.equals("VIEWABLE_NOT_DELETED")) {
                AlertViewController.showErrorMessage("Impossible de supprimer cette saga car des séances y sont liées");
            } else {
                specialViewableViewController.refresh();
                notifyListeners(); //Permettra aux sessions de disposer des nouvelles sagas/ supprimer les anciennes
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
            specialViewableViewController.fillMovieChoice();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}