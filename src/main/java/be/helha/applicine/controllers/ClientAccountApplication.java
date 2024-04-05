package be.helha.applicine.controllers;

import be.helha.applicine.views.ClientAccountControllerView;
import be.helha.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientAccountApplication extends Application implements ClientAccountControllerView.ClientAccountListener{

    //renvoie le fichier FXML de la vue ClientAccount
    private final FXMLLoader fxmlLoader = new FXMLLoader(ClientAccountControllerView.getFXMLResource());

    //permet de communiquer avec le parentController (MasterApplication) pour changer de fenêtre et de contrôleur de vue.
    private final MasterApplication parentController = new MasterApplication();

    //permet de fermer la fenêtre du client account et de retourner à la fenêtre du client. Je parle au parentController (masterApplication) pour changer de fenêtre.
    @Override
    public void toClientSide() throws Exception {
        parentController.toClient();
    }

    @Override
    public void toClientAccount() throws Exception {
        parentController.toClientAccount();
    }

    @Override
    public void start(Stage stage) throws Exception {
        ClientAccountControllerView.setStageOf(fxmlLoader);
        ClientAccountControllerView  clientAccountControllerView = fxmlLoader.getController();
        //permet à la vue de communiquer avec le controller de l'application ClientAccount
        clientAccountControllerView.setListener(this);
        //définit la fenêtre courante dans le parentController comme étant la fenêtre gérée par ManagerViewController.
        parentController.setCurrentWindow(ClientAccountControllerView.getAccountWindow());
    }
}
