package com.example.applicine.controllers;
import com.example.applicine.dao.impl.MovieDAOImpl;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;
import com.example.applicine.models.exceptions.InvalideFieldsExceptions;
import com.example.applicine.views.ManagerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.example.applicine.dao.MovieDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerApplication extends Application implements ManagerViewController.ManagerViewListener{
    private final FXMLLoader fxmlLoader = new FXMLLoader(ManagerViewController.getFXMLResource());
    private final MasterApplication parentController = new MasterApplication();
    private MovieDAO movieDAO;
    private List<Movie> movieList;

    private ManagerViewController managerViewController;

    public ManagerApplication() {
        movieDAO = new MovieDAOImpl();
        movieList = movieDAO.getAllMovies();
    }

    @Override
    public void start(Stage adminPage) throws Exception {
        ManagerViewController.setStageOf(fxmlLoader);
        managerViewController = fxmlLoader.getController();
        managerViewController.setListener(this);
        for (Movie movie : movieList) {
            managerViewController.addMovieLabel(movie);
        }
        parentController.setCurrentWindow(ManagerViewController.getStage());
        adminPage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
    }

    public static void main(String[] args) {
        launch();
    }

    public Movie getMovieFrom(int index) {
        return movieList.get(index);
    }
    public void toLogin() throws IOException{
        parentController.toLogin();
    }

    @Override
    public void onValidateButtonClick(String title, String genre, String director, String duration, String synopsis, String imagePath, String editType) throws SQLException {

//J'essaye de gérer l'exception comme ça si vous trouvez que c'est pas bon dites le moi svp sorry j'ai encore un peu de mal avec ça
        try {
            //vérifie si les champs sont valides
            validateFields(title, genre, director, duration, synopsis, imagePath);
        } catch (InvalideFieldsExceptions e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs invalides", e.getMessage());
            return;
        }
        String fileName = getFileNameFrom(imagePath);
        String validPath = createValidPath(fileName);
        if (editType.equals("add")) {
            Movie movie = new Movie(title, genre, director, Integer.parseInt(duration), synopsis, validPath);
            movieDAO.addMovie(movie);
            //mise à jour de la liste des films avec les films de la base de données (qui contient le nouveau film)
            movieList = fullFieldMovieListFromDB();
        }
        //je rafraichis la liste des films pour afficher le nouveau film ou les modifications
        this.refresh();


    }

    @Override
    public void onValidateButtonClick(int movieID, String title, String genre, String director, String duration, String synopsis, String imagePath, String editType) {

        try {
            validateFields(title, genre, director, duration, synopsis, imagePath);
        } catch (InvalideFieldsExceptions e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champs invalides", e.getMessage());
            return;
        }

        String fileName = getFileNameFrom(imagePath);
        String validPath = createValidPath(fileName);

        // Récupérer le film existant depuis la base de données
        Movie existingMovie = movieDAO.getMovieById(movieID);
        System.out.println("Le movie ID est" + movieID);

        System.out.println(existingMovie.getId() + " " + existingMovie.getTitle() + " " + existingMovie.getGenre() + " " + existingMovie.getDirector() + " " + existingMovie.getDuration() + " " + existingMovie.getSynopsis() + " " + existingMovie.getImagePath() );

        // Mets à jour les attributs du film avec les nouvelles valeurs, comme ça je ne crée pas un nouvel objet Movie
        existingMovie.setTitle(title);
        existingMovie.setGenre(genre);
        existingMovie.setDirector(director);
        existingMovie.setDuration(Integer.parseInt(duration));
        existingMovie.setSynopsis(synopsis);
        existingMovie.setImagePath(validPath);

        // Modifier le film dans la base de données
        movieDAO.updateMovie(existingMovie);

        movieList = movieDAO.getAllMovies();
        System.out.println(movieList);
        this.refresh();
    }

    @Override
    public void onImageChoiceButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Choisir une image");

        java.io.File initialDirectory = new java.io.File("src/main/resources/com/example/applicine/views/images");
        fileChooser.setInitialDirectory(initialDirectory);

        java.io.File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            System.out.println(imagePath);
            managerViewController.setImagePathLabel(imagePath);
        }
    }






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


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public String getFileNameFrom(String path) {
        System.out.println(System.getProperty("os.name") + " est le système d'exploitation actuel");

        if(System.getProperty("os.name").toLowerCase().contains("win")) {
            return path.substring(path.lastIndexOf("\\") + 1);
        } else {
            return path.substring(path.lastIndexOf("/") + 1);
        }
    }

    public String createValidPath(String fileName) {
        if(System.getProperty("os.name").toLowerCase().contains("win")) {
            return "file:src\\main\\resources\\com\\example\\applicine\\views\\images\\" + fileName;
        } else {
            return "file:src/main/resources/com/example/applicine/views/images/" + fileName;
        }
    }

    public void refresh() {
        managerViewController.clearMovies();
        for (Movie movie : movieList) {
            managerViewController.addMovieLabel(movie);
        }
    }


    private List<Movie> fullFieldMovieListFromDB() {
        return movieDAO.getAllMovies();
    }


}
