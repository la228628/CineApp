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
    protected ArrayList<String> movieTitleList = new ArrayList<>();
    private Movie selectedMovies = null;

    public Viewable selectedSaga = null;

    private List<Movie> addedMovies = new ArrayList<>();

    private String currentEditType = "add";

    private InvalidationListener specialViewablesChangeListener;

    private ServerRequestHandler serverRequestHandler;

    //constructor de la classe SpecialViewableController qui initialise les attributs de la classe et les listeners de la vue
    public SpecialViewableController(MasterApplication parentController) throws SQLException, IOException, ClassNotFoundException {
        super(parentController);
        movieTitleList = new ArrayList<>();
        this.serverRequestHandler = ServerRequestHandler.getInstance();
        this.serverRequestHandler.setListener(this);
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
    public ArrayList<String> getMovieTitleList(){
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
            serverRequestHandler.sendRequest(request);
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
            else
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
            Movie movie = null;
            try {
                serverRequestHandler.sendRequest(new GetMovieByIdRequest(id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            movies.add(movie);
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
            String request = null;
            try {
                serverRequestHandler.sendRequest(new DeleteViewableRequest(selectedSaga.getId()));
            } catch (IOException e) {
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
        } catch (SQLException | IOException error) {
            AlertViewController.showErrorMessage("Erreur lors de la récupération des films. Essaie de la connection au serveur.");
            serverRequestHandler = ServerRequestHandler.getInstance();
            serverRequestHandler.setListener(this);
        }
    }


    //visit
    public void visit(GetMoviesRequest getMoviesRequest){
        List<Movie> listOfMovies = getMoviesRequest.getMovieList();
        for(Movie movie : listOfMovies){
            String title = movie.getTitle();
            movieTitleList.add(title);
        }
    }
}