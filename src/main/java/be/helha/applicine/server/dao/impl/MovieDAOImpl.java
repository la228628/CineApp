package be.helha.applicine.server.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.server.FileManager;
import be.helha.applicine.server.dao.MovieDAO;
import be.helha.applicine.server.dao.ViewableDAO;
import be.helha.applicine.server.database.DatabaseConnection;
import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Viewable;

/**
 * This class is the implementation of the MovieDAO interface.
 */
public class MovieDAOImpl implements MovieDAO {
    private final Connection connection;

    private final ViewableDAO viewableDAO;

    /**
     * This constructor initializes the connection and the viewableDAO.
     */
    public MovieDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
        this.viewableDAO = new ViewableDAOImpl();
    }

    /**
     * This constructor initializes the connection and the viewableDAO.
     * @param connection The connection to the database.
     */
    public MovieDAOImpl(Connection connection) {
        this.connection = connection;
        this.viewableDAO = new ViewableDAOImpl();
    }
    private static final String SELECT_ALL_MOVIES = "SELECT * FROM movies";
    private static final String SELECT_MOVIE_BY_ID = "SELECT * FROM movies WHERE id = ?";
    private static final String INSERT_MOVIE = "INSERT INTO movies (title, genre, director, duration, synopsis, imagePath) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_MOVIE = "UPDATE movies SET title = ?, genre = ?, director = ?, duration = ?, synopsis = ?, imagePath = ? WHERE id = ?";
    private static final String DELETE_MOVIE = "DELETE FROM movies WHERE id = ?";
    private static final String DELETE_ALL_MOVIES = "DELETE FROM movies";

    /**
     * This method returns all the movies in the database.
     * @return A list of all the movies in the database.
     */
    @Override
    public List<Movie> getAll() throws DaoException {
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_MOVIES);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), null, rs.getString("imagePath")));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de la liste des films");
        }
        return movies;
    }

    /**
     * This method returns a movie by its id.
     * @param id The id of the movie.
     * @return The movie with the given id.
     */
    @Override
    public Movie get(int id) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_MOVIE_BY_ID)) {
            Movie movie;
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), null, rs.getString("imagePath"));
                    movie.setImage(FileManager.getImageAsBytes(movie.getImagePath()));
                    System.out.println(movie.getTitle());
                    return movie;
                }
            } catch (SQLException e) {
                throw new DaoException("Erreur lors de la récupération de l'image du film");
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération du film");
        }
        return null;
    }

    /**
     * This method adds a movie to the database.
     *
     * @param movie The movie to add.
     */
    @Override
    public void create(Viewable movie) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_MOVIE)) {
            prepareMovie(movie, pstmt);
            pstmt.executeUpdate();

            //Je veux get l'id de la ligne insérée
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                movie.setId(generatedKeys.getInt(1));
                viewableDAO.addViewableWithOneMovie(movie.getTitle(), "SingleMovie", movie.getId());
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de l'ajout du film");
        }
    }

    /**
     * This method updates a movie in the database.
     * @param movie The movie to update.
     */
    @Override
    public void update(Viewable movie) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_MOVIE)) {
            prepareMovie(movie, pstmt);
            pstmt.setInt(7, movie.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la mise à jour du film");
        }
    }

    /**
     * This method prepares a movie to be added or updated in the database.
     * @param movie The movie to prepare.
     * @param pstmt The prepared statement to prepare the movie.
     */
    private void prepareMovie(Viewable movie, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, movie.getTitle());
        pstmt.setString(2, movie.getGenre());
        pstmt.setString(3, movie.getDirector());
        pstmt.setInt(4, movie.getTotalDuration());
        pstmt.setString(5, movie.getSynopsis());
        pstmt.setString(6, movie.getImagePath());
    }

    /**
     * This method removes a movie from the database.
     * @param id The id of the movie to remove.
     */
    @Override
    public void delete(int id) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_MOVIE)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            viewableDAO.removeViewableFromMovie(id);
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la suppression du film");
        }
    }

    /**
     * This method checks if the movie table is empty.
     *
     * @return True if the movie table is empty, false otherwise.
     */
    @Override
    public boolean isMovieTableEmpty() throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_MOVIES);
             ResultSet rs = pstmt.executeQuery()) {
            return !rs.next();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la vérification de la table des films");
        }
    }

    /**
     * returns the number of sessions linked to a movie
     * @param id the id of the movie
     * @return the number of sessions linked to the movie
     */

    @Override
    public int getSessionLinkedToMovie(int id) throws DaoException {
        int viewableId = viewableDAO.getViewableIdByMovieId(id);
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM seances WHERE viewableid = ?")) {
            pstmt.setInt(1, viewableId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération du nombre de sessions liées au film");
        }
        return 0;
    }

    /**
     * This method deletes all the movies in the database.
     */
    @Override
    public void deleteAll() {
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_ALL_MOVIES)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de tous les films : " + e.getMessage());
        }
    }
}