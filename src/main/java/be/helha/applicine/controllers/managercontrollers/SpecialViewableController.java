package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.views.managerviews.SpecialViewableViewController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.security.PublicKey;

public class SpecialViewableController extends ManagerController implements SpecialViewableViewController.SpecialViewableListener {

    private  ManagerController parentController;
    public FXMLLoader specialViewableFxmlLoader;
    public SpecialViewableViewController specialViewableViewController;

    public SpecialViewableController() {
        super();
        specialViewableFxmlLoader = parentController.getSpecialViewableFXML();
        specialViewableViewController = new SpecialViewableViewController();
        specialViewableViewController.setListener(this);
    }

    public void setParentController(ManagerController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void start(Stage adminPage) throws Exception {


    }

    @Override
    public void onAddMovieButtonClick() {

    }

    @Override
    public void onRemoveMovieButtonClick() {

    }

    @Override
    public void displayAllMovie() {

    }
}
