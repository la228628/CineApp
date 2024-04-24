package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Viewable;
import be.helha.applicine.views.managerviews.SpecialViewableViewController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SpecialViewableController extends ManagerController implements SpecialViewableViewController.SpecialViewableListener {

    private  ManagerController parentController;
    public FXMLLoader specialViewableFxmlLoader;
    public SpecialViewableViewController specialViewableViewController;
    protected List<String> movieTitleList = new ArrayList<>();

    private Movie selectedMovies = null;

    private List<Movie> addedMovies = new ArrayList<>();

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
        //methode d'initialisation de la vue (remplissage du combobox)
        specialViewableViewController.init();
    }

    @Override
    public void onAddMovieButtonClick() {
        if(selectedMovies != null && !addedMovies.contains(selectedMovies)){
            addedMovies.add(selectedMovies);
            specialViewableViewController.fillAddedMovieChoice(getAddedViewablesTitles(),getTotalDuration());
        }
    }

    List<String> getAddedViewablesTitles() {
        List<String> addedViewablesTitles = new ArrayList<>();
        for(Viewable viewable : addedMovies){
            addedViewablesTitles.add(viewable.getTitle());
        }
        return addedViewablesTitles;
    }

    @Override
    public void onRemoveMovieButtonClick() {
        if(selectedMovies != null && addedMovies.contains(selectedMovies)){
            addedMovies.remove(selectedMovies);
        }else{
            try{
                addedMovies.removeLast();
            }catch (NoSuchElementException ignored){

            }
        }
        specialViewableViewController.fillAddedMovieChoice(getAddedViewablesTitles(), getTotalDuration());
    }

    @Override
    public ArrayList<String> displayAllMovie() {
        movieList = movieDAO.getAllMovies();
        for(Movie movie : movieList){
            movieTitleList.add(movie.getTitle());
        }
        return (ArrayList<String>) movieTitleList;
    }

    @Override
    public void onMovieChoising(int selectedIndex) {
        selectedMovies = movieList.get(selectedIndex);
        System.out.println("Film choisi: "+ selectedMovies.getTitle());
    }

    private Integer getTotalDuration(){
        Integer totalDuration = 0;
        for(Movie movie : addedMovies){
            totalDuration += movie.getDuration();
        }
        return totalDuration;
    }



}
