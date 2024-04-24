package be.helha.applicine.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import be.helha.applicine.dao.MovieDAO;
import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Visionable;

public class MovieDAOImpl implements MovieDAO {
    private final Connection connection;

    public MovieDAOImpl() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Connection failed");
        }
    }
    //MovieDAOImpl constructeur avec connection en paramètre pour les tests unitaires
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

    /**
     * This method returns all the movies in the database.
     * @return A list of all the movies in the database.
     */
    @Override
    public List<Visionable> getAllMovies() throws SQLException{
        List<Visionable> movies = new ArrayList<>();
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
            throw new SQLException(e);
        }
        return movies;
    }

    /**
     * This method returns a movie by its id.
     * @param id The id of the movie.
     * @return The movie with the given id.
     */
    @Override
    public Visionable getMovieById(int id) {
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

    /**
     * This method adds a movie to the database.
     * @param movie The movie to add.
     */
    @Override
    public void addMovie(Visionable movie) {
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_MOVIE)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getGenre());
            pstmt.setString(3, movie.getDirector());
            pstmt.setInt(4, movie.getTotalDuration());
            pstmt.setString(5, movie.getSynopsis());
            pstmt.setString(6, movie.getImagePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du film : " + e.getMessage());
        }
    }

    /**
     * This method updates a movie in the database.
     * @param movie The movie to update.
     */
    @Override
    public void updateMovie(Visionable movie) {
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_MOVIE)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getGenre());
            pstmt.setString(3, movie.getDirector());
            pstmt.setInt(4, movie.getTotalDuration());
            pstmt.setString(5, movie.getSynopsis());
            pstmt.setString(6, movie.getImagePath());
            pstmt.setInt(7, movie.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du film : " + e.getMessage());
        }
    }

    /**
     * This method removes a movie from the database.
     * @param id The id of the movie to remove.
     */
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

    /**
     * This method reorders all the ids in the database.
     * @param offset The offset to reorder the ids.
     */
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

    /**
     * This method removes all the movies from the database.
     */
    @Override
    public void removeAllMovies() {
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_ALL_MOVIES)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de tous les films : " + e.getMessage());
        }
    }

    /**
     * This method adapts the image path for the current operating system.
     * @param imagePath The image path to adapt.
     * @return The adapted image path.
     */
    private String adaptImagePathForCurrentOS(String imagePath) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return imagePath.replace("/", "\\");
        } else {
            return imagePath.replace("\\", "/");
        }
    }

    /**
     * This method adapts all the image paths in the database for the current operating system.
     */
    public void adaptAllImagePathInDataBase() throws SQLException{
        List<Visionable> movies = getAllMovies();
        System.out.println("Tout les chemins vont être réadaptés");
        for (Visionable movie : movies) {
            String adaptedImagePath = adaptImagePathForCurrentOS(movie.getImagePath());
            movie.setImagePath(adaptedImagePath);
            updateMovie(movie);
        }
    }

    /**
     * This method checks if the movie table is empty.
     *
     * @return True if the movie table is empty, false otherwise.
     */
    @Override
    public boolean isMovieTableEmpty() {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_MOVIES);
             ResultSet rs = pstmt.executeQuery()) {
            return !rs.next();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la liste des films : " + e.getMessage());
        }
        return true;
    }

    /**
     * returns the number of sessions linked to a movie
     * @param id
     * @return
     */

    public int sessionLinkedToMovie(int id) {

        try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM seances WHERE movieid = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nombre de sessions liées au film : " + e.getMessage());
        }
        return 0;
    }

    /**
     * delete all sessions linked to a movie
     * @param id
     */
    @Override
    public void deleteRattachedSessions(int id) {
        try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM seances WHERE movieid = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des sessions liées au film : " + e.getMessage());
        }
    }

}