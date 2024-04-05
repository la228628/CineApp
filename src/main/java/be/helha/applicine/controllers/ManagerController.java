package be.helha.applicine.controllers;

import be.helha.applicine.FileMangement.FileManager;
import be.helha.applicine.dao.impl.MovieDAOImpl;
import be.helha.applicine.database.ApiRequest;
import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.exceptions.InvalideFieldsExceptions;
import be.helha.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import be.helha.applicine.dao.MovieDAO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.swing.*;

/**
 * ManagerApplication class is the controller class for the Manager view.
 */
public class ManagerController extends Application implements ManagerViewController.ManagerViewListener{
    private final FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
    /**
     * parentController is useful to say Master which window is currently open.
     */
    private final MasterApplication parentController = new MasterApplication();
    private MovieDAO movieDAO;
    private List<Movie> movieList;
    private ManagerViewController managerViewController;
    /**
     * It fetches all the movies from the database to movieList.
     * It follows the DAO design pattern https://www.digitalocean.com/community/tutorials/dao-design-pattern.
     */
    public ManagerController() {
        movieDAO = new MovieDAOImpl();
        movieDAO.adaptAllImagePathInDataBase();
        movieList = movieDAO.getAllMovies();
//        if(movieList.isEmpty()){
//            JFrame frame = null;
//            frame = getWaitingWindow();
//            ApiRequest apiRequest = new ApiRequest();
//            apiRequest.fillDatabase();
//            movieList = movieDAO.getAllMovies();
//            frame.dispose();
//        }
    }


    @Override
    public void start(Stage adminPage) throws Exception {
        ManagerViewController.setStageOf(fxmlLoader);
        managerViewController = fxmlLoader.getController();
        managerViewController.setListener(this);
        for (Movie movie : movieList) {
            managerViewController.displayMovie(movie);
            System.out.println(movie.getId());
        }
        parentController.setCurrentWindow(ManagerViewController.getStage());
        adminPage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * It returns a movie to the movieList at index.
     * @param index
     * @return movieList
     */
    public Movie getMovieFrom(int index) {
        return movieList.get(index);
    }

    /**
     * Redirects to the login view and disconnect the user.
     * @throws IOException
     */
    public void toLogin() throws IOException{
        parentController.toLogin();
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
        try {
            validateFields(title, genre, director, duration, synopsis, imagePath);
            if (!imagePath.contains("AppData\\Roaming\\Applicine\\images\\")) {
                imagePath = FileManager.copyImageToAppdata(imagePath);
                System.out.println("Le chemin de l'image est " + imagePath);
            }
        } catch (InvalideFieldsExceptions e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs invalides", e.getMessage());
            return;
        }
        if (editType.equals("add")) {
            Movie newMovie = new Movie(title, genre, director, Integer.parseInt(duration), synopsis, createValidPath(imagePath));
            movieDAO.addMovie(newMovie);
        } else if (editType.equals("modify")) {
            Movie existingMovie = createMovieWithRawData(movieID, title, genre, director, duration, synopsis, imagePath);
            movieDAO.updateMovie(existingMovie);
        }
        System.out.println(imagePath);

        movieList = fullFieldMovieListFromDB();
        this.refresh();
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
    private Movie createMovieWithRawData(int movieID, String title, String genre, String director, String duration, String synopsis, String imagePath) {
        Movie existingMovie = movieDAO.getMovieById(movieID);
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
            managerViewController.setImagePathLabel(imagePath);
        }
    }


    /**
     * It deletes a movie from the database.
     * @param movieId
     * @throws SQLException
     */
    public void onDeleteButtonClick(int movieId) throws SQLException {
        try {
            //Affiche une alerte de confirmation pour la suppression
            boolean confirmed = showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Suppression", "Voulez-vous vraiment supprimer ce film ?");
            if (confirmed) {
                movieDAO.removeMovie(movieId);
                movieList = movieDAO.getAllMovies();
                this.refresh();
                managerViewController.deletionConfirmed();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Film introuvable", "Le film que vous essayez de supprimer n'existe pas");
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
     * It shows an alert with the given parameters, and returns true if the user clicks on OK.
     * @param alertType
     * @param title
     * @param headerText
     * @param contentText
     * @return
     */

    private boolean showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        //alert.showAndWait();
        Optional<ButtonType> result = alert.showAndWait();
        //Si l'utilisateur clique sur OK, la méthode retourne true
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * It returns the file name from the path by checking the operating system.
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
     * @param imagePath
     * @return
     */

    public String createValidPath(String imagePath) {
        if(imagePath.startsWith("file:")) {
            return imagePath;
        }
        return "file:" + imagePath;
    }

    /**
     * It refreshes the movie list by clearing the movies and adding them again.
     */

    public void refresh() {
        managerViewController.clearMovies();
        for (Movie movie : movieList) {
            managerViewController.displayMovie(movie);
        }
        managerViewController.setSelection();
        managerViewController.refreshAfterEdit();
    }

    /**
     * It returns the full movie list from the database.
     * @return
     */
    private List<Movie> fullFieldMovieListFromDB() {
        return movieDAO.getAllMovies();
    }


}