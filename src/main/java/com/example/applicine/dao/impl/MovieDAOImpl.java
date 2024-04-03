package com.example.applicine.dao.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.applicine.dao.MovieDAO;
import com.example.applicine.database.ApiRequest;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;

public class MovieDAOImpl implements MovieDAO {
    private final Connection connection;

    public MovieDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    public MovieDAOImpl(Connection connection) {
        this.connection = connection;
    }
    private static final String SELECT_ALL_MOVIES = "SELECT * FROM movies";
    private static final String SELECT_MOVIE_BY_ID = "SELECT * FROM movies WHERE id = ?";
    private static final String INSERT_MOVIE = "INSERT INTO movies (title, genre, director, duration, synopsis, imagePath) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_MOVIE = "UPDATE movies SET title = ?, genre = ?, director = ?, duration = ?, synopsis = ?, imagePath = ? WHERE id = ?";
    private static final String DELETE_MOVIE = "DELETE FROM movies WHERE id = ?";
    private static final String DELETE_ALL_MOVIES = "DELETE FROM movies";

    private static final String REORDER_ALL_ID = "UPDATE movies SET id = id - 1 WHERE id > ?";

    @Override
    public List<Movie> getAllMovies() {
        createTable();
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_MOVIES);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"),  rs.getString("imagePath")));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la liste des films : " + e.getMessage());
            if(e.getMessage().contains("missing database")){
                System.out.println("La base de données n'existe pas");
            }

        }
        return movies;
    }

    @Override
    public Movie getMovieById(int id) {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_MOVIE_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("chemin va être réadapté");
                    return new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), (rs.getString("imagePath")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du film : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addMovie(Movie movie) {
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_MOVIE)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getGenre());
            pstmt.setString(3, movie.getDirector());
            pstmt.setInt(4, movie.getDuration());
            pstmt.setString(5, movie.getSynopsis());
            pstmt.setString(6, movie.getImagePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du film : " + e.getMessage());
        }
    }

    @Override
    public void updateMovie(Movie movie) {
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_MOVIE)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getGenre());
            pstmt.setString(3, movie.getDirector());
            pstmt.setInt(4, movie.getDuration());
            pstmt.setString(5, movie.getSynopsis());
            pstmt.setString(6, movie.getImagePath());
            pstmt.setInt(7, movie.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du film : " + e.getMessage());
        }
    }

    @Override
    public void removeMovie(int id) {
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_MOVIE)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            //reorderAllID(id);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du film : " + e.getMessage());
        }
    }


    public void reorderAllID(int offset) throws SQLException {
        try {

            System.out.println("ID avant réorganisés");
            PreparedStatement statement = connection.prepareStatement(REORDER_ALL_ID);
            System.out.println("ID réorganisés");
            statement.setInt(1, offset);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllMovies() {
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_ALL_MOVIES)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de tous les films : " + e.getMessage());
        }
    }

    private String adaptImagePathForCurrentOS(String imagePath) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return imagePath.replace("/", "\\");
        } else {
            return imagePath.replace("\\", "/");
        }
    }

    public void adaptAllImagePathInDataBase() {
        List<Movie> movies = getAllMovies();
        System.out.println("Tout les chemins vont être réadaptés");
        for (Movie movie : movies) {
            String adaptedImagePath = adaptImagePathForCurrentOS(movie.getImagePath());
            movie.setImagePath(adaptedImagePath);
            updateMovie(movie);
        }
    }

    private void createTable() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS movies (id INTEGER PRIMARY KEY, title TEXT, genre TEXT, director TEXT, duration INTEGER, synopsis TEXT, imagePath TEXT)");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de la base de données : " + e.getMessage());
        }
    }


}