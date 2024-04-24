package be.helha.applicine.controllers.managercontrollers;

import be.helha.applicine.FileMangement.FileManager;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Viewable;
import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.views.managerviews.MovieManagerViewController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

//notifiera les classes qui écoutent que la liste de films a changé

public class MovieManagerApp extends ManagerController implements MovieManagerViewController.ManagerViewListener, Observable {

    private ManagerController parentController;

    private FXMLLoader movieManagerFxmlLoader;

    private MovieManagerViewController movieManagerViewController;

    private InvalidationListener movieChangeListener;

    public MovieManagerApp() {
        super();
    }

    @Override
    public void start(Stage adminPage) throws Exception {
        movieManagerFxmlLoader = parentController.getMovieManagerFXML();
        movieManagerViewController = movieManagerFxmlLoader.getController();
        movieManagerViewController.setListener(this);
        for (Viewable movie : movieList) {
            movieManagerViewController.displayMovie(movie);
            System.out.println(movie.getId());
        }
    }

    /**
     * It sets the parent controller.
     * @param parentController
     */

    public void setParentController(ManagerController parentController) {
        this.parentController = parentController;
    }


    /**
     * Adds a new movie to the database or modify the selected film.
     * @param title
     * @param genre
     * @param director
     * @param duration
     * @param synopsis
     * @param imagePath
     * @param editType
     * @throws SQLException
     */
    @Override
    public void onValidateButtonClick(int movieID, String title, String genre, String director, String duration, String synopsis, String imagePath, String editType) throws SQLException {
        System.out.println("avant le trycatch Le chemin de l'image est " + imagePath);

        try {
            validateFields(title, genre, director, duration, synopsis, imagePath);
            if (!imagePath.contains("AppData\\Roaming\\Applicine\\images\\")) {
                imagePath = FileManager.copyImageToAppdata(imagePath);
                System.out.println("Le chemin de l'image est " + imagePath);
            }
        } catch (InvalideFieldsExceptions e) {
            parentController.showAlert(Alert.AlertType.ERROR, "Erreur", "Champs invalides", e.getMessage());
            return;
        }
        if (editType.equals("add")) {
            System.out.println(imagePath);
            Viewable newMovie = new Movie(title, genre, director, Integer.parseInt(duration), synopsis, createValidPath(imagePath));
            movieDAO.addMovie(newMovie);
            movieManagerViewController.clearEditPane();
        } else if (editType.equals("modify")) {
            Viewable existingMovie = createMovieWithRawData(movieID, title, genre, director, duration, synopsis, imagePath);
            movieDAO.updateMovie(existingMovie);
        }
        System.out.println(imagePath);

        movieList = fullFieldMovieListFromDB();
        notifyListeners();
        this.refreshMovieManager();

        //On notifie les listeners que la liste de films a changé

    }


    /**
     *
     * @param movieID
     * @param title
     * @param genre
     * @param director
     * @param duration
     * @param synopsis
     * We create a Movie object with data to use it to update database
     * @return
     */
    private Viewable createMovieWithRawData(int movieID, String title, String genre, String director, String duration, String synopsis, String imagePath) {
        Viewable existingMovie = movieDAO.getMovieById(movieID);
        existingMovie.setTitle(title);
        existingMovie.setGenre(genre);
        existingMovie.setDirector(director);
        existingMovie.setDuration(Integer.parseInt(duration));
        existingMovie.setSynopsis(synopsis);
        existingMovie.setImagePath(createValidPath(imagePath));
        return existingMovie;
    }



    /**
     * It opens a file chooser to choose an image.
     */
    @Override
    public void onImageChoiceButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Choisir une image");

        String appdata = System.getenv("APPDATA");
        String path = appdata + "/Applicine/images";

        File initialDirectory = new File(path);
        fileChooser.setInitialDirectory(initialDirectory);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            System.out.println(imagePath);
            movieManagerViewController.setImagePathLabel(imagePath);
        }
    }


    /**
     * It deletes a movie from the database.
     * It checks if the movie is linked to a session and if the user wants to delete it.
     * If the user confirms, the movie is deleted.
     *
     * @param movieId
     * @throws SQLException
     */
    public void onDeleteButtonClick(int movieId) throws SQLException {
        try {
            //Affiche une alerte de confirmation pour la suppression
            boolean confirmed = parentController.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Suppression", "Voulez-vous vraiment supprimer ce film ?");
            if (confirmed) {
                int rattachedSessions = movieDAO.sessionLinkedToMovie(movieId);
                System.out.println(rattachedSessions);
                if(rattachedSessions > 0) {
                    boolean deleteDespiteSession = parentController.showAlert(Alert.AlertType.CONFIRMATION, "Attention", "Séances trouvées", "Ce film est lié à " +rattachedSessions+ " séances. Le supprimer entraînera la suppresion de ces séances. Voulez vous continuer ?");
                    if(!deleteDespiteSession){
                        return;
                    }
                }
                movieDAO.deleteRattachedSessions(movieId);

                movieDAO.removeMovie(movieId);
                movieList = movieDAO.getAllMovies();
                this.refreshMovieManager();
                movieManagerViewController.deletionConfirmed();
                notifyListeners();
            }
        } catch (SQLException e) {
            parentController.showAlert(Alert.AlertType.ERROR, "Erreur", "Film introuvable", "Le film que vous essayez de supprimer n'existe pas");
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * It validates the fields of the movie by checking if they are empty or if the duration is a number.
     * @param title
     * @param genre
     * @param director
     * @param duration
     * @param synopsis
     * @param imagePath
     * @throws InvalideFieldsExceptions
     */

    public void validateFields(String title, String genre, String director, String duration, String synopsis, String imagePath) throws InvalideFieldsExceptions {
        if (title.isEmpty() || genre.isEmpty() || director.isEmpty() || duration.isEmpty() || synopsis.isEmpty() || imagePath.equals("...")) {
            throw new InvalideFieldsExceptions("Tous les champs doivent être remplis");
        }
        try {
            Integer.parseInt(duration);
        } catch (NumberFormatException e) {
            throw new InvalideFieldsExceptions("La durée doit être un nombre entier");
        }
    }


    /**
     * It returns the file name from the path by checking the operating system.
     *
     * @param path
     * @return
     */
    public String getFileNameFrom(String path) {
        System.out.println(System.getProperty("os.name") + " est le système d'exploitation actuel");

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return path.substring(path.lastIndexOf("\\") + 1);
        } else {
            return path.substring(path.lastIndexOf("/") + 1);
        }
    }

    /**
     * It creates a valid path by checking if the path starts with "file:".
     * This is necessary for the image to be displayed in the view.
     * If the path does not start with "file:", it adds it.
     *
     * @param imagePath
     * @return
     */

    public String createValidPath(String imagePath) {
        if (imagePath.startsWith("file:")) {
            return imagePath;
        }
        return "file:" + imagePath;
    }

    /**
     * It refreshes the movie list by clearing the movies and adding them again.
     */
    public void refreshMovieManager() {
        movieManagerViewController.clearMovies();
        for (Viewable movie : movieList) {
            movieManagerViewController.displayMovie(movie);
        }
        movieManagerViewController.setSelection();
        movieManagerViewController.refreshAfterEdit();
    }

    /**
     * It logs out the user and returns to the login page.
     * @throws IOException
     */
    public void toLogin() throws IOException {
        parentController.toLogin();
    }

    /**
     * It sets the observable listener that will be notified when the movie list changes.
     * @param movieChangeListener
     */
    @Override
    public void addListener(InvalidationListener movieChangeListener) {
        //On se sert de l'observable pour notifier les SessionApp que la liste de films a changé
        this.movieChangeListener = movieChangeListener;
    }

    /**
     * It removes the listener.
     * @param invalidationListener
     */
    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        this.movieChangeListener = null;
        }


    /**
     * It notifies the listeners that the movie list has changed.
     */
    private void notifyListeners() {
        if (movieChangeListener != null) {
            movieChangeListener.invalidated(this);
        }
    }
}
