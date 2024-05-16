package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Saga;
import be.helha.applicine.models.Viewable;
import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.views.managerviews.SpecialViewableViewController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

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
    public SpecialViewableController() {
        super();
    }

    public void setParentController(ManagerController parentController) {
        this.parentController = parentController;
    }

    //methode d'initialisation de la vue (remplissage des listes, des combobox, etc)
    @Override
    public void start(Stage adminPage) throws Exception {
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
    public ArrayList<String> displayAllMovie() {
        movieTitleList = new ArrayList<>();
        movieList = movieDAO.getAllMovies();
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
    public void onValidateButtonClick(String name) {
        try {
            validateFields(name);
            if (this.currentEditType.equals("add"))
                addSagaIntoDB(name, "Saga", getAddedMoviesIds());
            else
                modifySagaInDB(this.selectedSaga.getId(), name, "Saga", getAddedMoviesIds());
            parentController.showAlert(Alert.AlertType.INFORMATION, "Succès", "Saga ajoutée", "La saga a été ajoutée/modifiée avec succès");
        } catch (InvalideFieldsExceptions e) {
            parentController.showAlert(Alert.AlertType.ERROR, "Erreur", "Champs invalides", e.getMessage());
        }
        specialViewableViewController.refresh();
        notifyListeners(); //Permettra aux sessions de disposer des nouvelles sagas/ supprimer les anciennes
    }

    private void modifySagaInDB(int id, String type, String name, ArrayList<Integer> addedMoviesIds) {
        viewableDAO.updateViewable(id, type, name, addedMoviesIds);
    }

    ArrayList<Integer> getAddedMoviesIds() {
        ArrayList<Integer> addedMoviesIds = new ArrayList<>();
        for (Movie movie : addedMovies) {
            addedMoviesIds.add(movie.getId());
        }
        return addedMoviesIds;
    }

    private void addSagaIntoDB(String name, String type, ArrayList<Integer> addedMoviesIds) {
        viewableDAO.addViewable(name, type, addedMoviesIds);
    }

    private void validateFields(String name) throws InvalideFieldsExceptions {
        if (addedMovies.size() < 2 || name.isEmpty()) {
            throw new InvalideFieldsExceptions("Certains champs n'ont pas été remplis correctement");
        }
    }

    @Override
    public void onCancelButtonClick() {
        boolean confirm = parentController.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Voulez-vous vraiment quitter ?", "");
        if (confirm) {
            specialViewableViewController.onCancelConfirm();
        }
    }

    @Override
    public void displaySagas() {
        for (Viewable viewable : viewableDAO.getAllViewables()) {
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
    public void onSagaDeleteButtonClick() {
        boolean confirm = parentController.showAlert(Alert.AlertType.CONFIRMATION, "Voulez vous vraiment supprimer", "Voulez vous vraiment supprimer cette saga ?", "");
        if (confirm) {
            if (!viewableDAO.getSeancesLinkedToViewable(selectedSaga.getId()).isEmpty()) {
                parentController.showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer", "La saga est liée à des séances");
            } else {
                viewableDAO.removeViewable(selectedSaga.getId());
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
        specialViewableViewController.fillMovieChoice();
    }
}