package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.dao.impl.ViewableDAOImpl;
import be.helha.applicine.models.Viewable;
import be.helha.applicine.views.managerviews.SpecialViewableViewController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import java.security.PublicKey;

public class SpecialViewableController extends ManagerController implements SpecialViewableViewController.SpecialViewableListener {

    private  ManagerController parentController;
    public FXMLLoader specialViewableFxmlLoader;
    public SpecialViewableViewController specialViewableViewController;
    private ViewableDAOImpl viewableDAO;
    protected List<Viewable> viewableList;
    protected List<String> movieTitleList;

    //constructor de la classe SpecialViewableController qui initialise les attributs de la classe et les listeners de la vue
    public SpecialViewableController() {
        super();
        specialViewableViewController = new SpecialViewableViewController();
        specialViewableViewController.setListener(this);
        viewableDAO = new ViewableDAOImpl();
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
        specialViewableViewController.initialize();
    }

    @Override
    public void onAddMovieButtonClick() {

    }

    @Override
    public void onRemoveMovieButtonClick() {

    }

    @Override
    public ArrayList<String> displayAllMovie() {
        //je recupere la liste des films disponibles dans la base de données et je les affiche dans la vue
        viewableList = viewableDAO.getAllViewables();
        for(Viewable viewable : viewableList){
            movieTitleList.add(viewable.getTitle());
        }
        return (ArrayList<String>) movieTitleList;
    }
}
