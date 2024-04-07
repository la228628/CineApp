package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.views.managerviews.SessionManagerViewController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class SessionManagerApp extends ManagerController {

    private ManagerController parentController;

    private FXMLLoader sessionManagerFxmlLoader;

    private SessionManagerViewController sessionManagerViewController;

    public SessionManagerApp() {
        super();
    }

    @Override
    public void start(Stage adminPage) throws Exception {
        sessionManagerFxmlLoader = parentController.getSessionManagerFXML();
        sessionManagerViewController = sessionManagerFxmlLoader.getController();
        sessionManagerViewController.intialize();
    }


    public void setParentController(ManagerController managerController) {
        this.parentController = managerController;
    }
}
