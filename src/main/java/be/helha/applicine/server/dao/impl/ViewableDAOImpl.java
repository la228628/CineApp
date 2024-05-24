package be.helha.applicine.server.dao.impl;

import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.server.FileManager;
import be.helha.applicine.server.dao.ViewableDAO;
import be.helha.applicine.server.database.DatabaseConnection;
import be.helha.applicine.common.models.Movie;
import be.helha.applicine.common.models.Saga;
import be.helha.applicine.common.models.Viewable;

import java.io.IOException;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewableDAOImpl implements ViewableDAO {
    private Connection connection;

    public ViewableDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addViewableWithOneMovie(String title, String singleMovie, int id) throws DaoException {
        try {
            String query = "INSERT INTO viewables (name, type) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, singleMovie);
            preparedStatement.executeUpdate();

            addMovieToViewable(preparedStatement.getGeneratedKeys().getInt(1), id);

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idViewable = generatedKeys.getInt(1);
                    System.out.println("Inserted viewable ID: " + idViewable);
                } else {
                    throw new DaoException("La création du viewable a échoué, aucun ID n'a été obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de l'ajout d'un viewable avec un seul film");
        }
    }


    public void addViewable(String name, String type, ArrayList<Integer> movieIDs) throws DaoException {
        try {
            String query = "INSERT INTO viewables (name, type) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.executeUpdate();

            for (Integer movieID : movieIDs) {
                addMovieToViewable(preparedStatement.getGeneratedKeys().getInt(1), movieID);
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.println("Inserted viewable ID: " + id);
                } else {
                    throw new DaoException("La création du viewable a échoué, aucun ID n'a été obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de l'ajout d'un viewable");
        }
    }

    @Override
    public boolean removeViewable(Integer id) throws DaoException {
        if (!getSeancesLinkedToViewable(id).isEmpty()) {
            return false;
        }
        try {
            connection.createStatement().executeUpdate("DELETE FROM viewables WHERE id = " + id);
            removeViewableMovieCorrespondance(id);
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la suppression du viewable");
        }
        return true;
    }


    @Override
    public void removeViewableFromMovie(int movieId) throws DaoException {
        try {
            int viewableId = 0;
            ResultSet rs = connection.createStatement().executeQuery("SELECT viewableid FROM viewablecontains WHERE movieid = " + movieId);
            if (rs.next()) {
                viewableId = rs.getInt("viewableid");
            }
            removeViewable(viewableId);
        } catch (Exception e) {
            throw new DaoException("Erreur lors de la suppression du viewable");
        }
    }

    @Override
    public int getViewableIdByMovieId(int id) throws DaoException {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT viewableid FROM viewablecontains WHERE movieid = " + id);
            if (rs.next()) {
                return rs.getInt("viewableid");
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de l'id du viewable");
        }
        return id;
    }


    public void updateViewable(Integer id, String name, String type, ArrayList<Integer> movieIDs) throws DaoException {
        try {
            String sql = "UPDATE viewables SET name = ?, type = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            updateViewableMovieCorrespondance(id, movieIDs);
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la mise à jour du viewable");
        }
    }

    public void addMovieToViewable(Integer viewableID, Integer movieID) throws DaoException {
        try {
            connection.createStatement().executeUpdate("INSERT INTO viewablecontains (viewableid, movieid) VALUES (" + viewableID + ", " + movieID + ")");
        } catch (Exception e) {
            throw new DaoException("Erreur lors de l'ajout d'un film à un viewable");
        }
    }

    public void updateViewableMovieCorrespondance(Integer viewableID, ArrayList<Integer> movieIDs) throws DaoException {
        removeViewableMovieCorrespondance(viewableID);
        for (Integer movieID : movieIDs) {
            addMovieToViewable(viewableID, movieID);
        }
    }

    public void removeViewableMovieCorrespondance(Integer viewableID) throws DaoException {
        try {
            connection.createStatement().executeUpdate("DELETE FROM viewablecontains WHERE viewableid = " + viewableID);
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la suppression des films d'un viewable");
        }
    }

    public ArrayList<Movie> getMoviesFromViewable(Integer viewableID) throws DaoException {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM viewablecontains WHERE viewableid = " + viewableID);
            while (rs.next()) {
                Movie movie = new MovieDAOImpl().get(rs.getInt("movieid"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération des films d'un viewable");
        }
        return movies;
    }

    public ArrayList<Viewable> getAllViewables() throws DaoException {
        ArrayList<Viewable> array = new ArrayList<Viewable>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM viewables");
            while (rs.next()) {
                Viewable viewable = null;
                ArrayList<Movie> moviesForViewable = getMoviesFromViewable(rs.getInt("id"));
                if (moviesForViewable.size() == 1) {
                    viewable = new Movie(rs.getInt("id"), moviesForViewable.getFirst().getTitle(), moviesForViewable.getFirst().getGenre(), moviesForViewable.getFirst().getDirector(), moviesForViewable.getFirst().getDuration(), moviesForViewable.getFirst().getSynopsis(), moviesForViewable.getFirst().getImage(), moviesForViewable.getFirst().getImagePath());
                } else {
                    Movie refMovie = moviesForViewable.getFirst();
                    viewable = new Saga(rs.getInt("id"), rs.getString("name"), refMovie.getGenre(), refMovie.getDirector(), getTotalDurationFromMovies(moviesForViewable), "moviesForViewable", refMovie.getImage(), refMovie.getImagePath(), moviesForViewable);
                }
                if (viewable.getImage() == null) {
                    viewable.setImage(FileManager.getImageAsBytes(viewable.getImagePath()));
                }
                array.add(viewable);
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération de tous les viewables");
        }
        return array;
    }

    public int getTotalDurationFromMovies(ArrayList<Movie> movies) {
        int totalDuration = 0;
        for (Movie movie : movies) {
            totalDuration += movie.getDuration();
        }
        return totalDuration;
    }

    public Viewable getViewableById(int id) throws DaoException {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM viewables WHERE id = " + id);
            if (rs.next()) {
                Viewable viewable = null;
                ArrayList<Movie> moviesForViewable = getMoviesFromViewable(rs.getInt("id"));
                if (moviesForViewable.size() == 1) {
                    viewable = new Movie(rs.getInt("id"),moviesForViewable.getFirst().getTitle(), moviesForViewable.getFirst().getGenre(), moviesForViewable.getFirst().getDirector(), moviesForViewable.getFirst().getDuration(), moviesForViewable.getFirst().getSynopsis(), moviesForViewable.getFirst().getImage(), moviesForViewable.getFirst().getImagePath());
                } else {
                    Movie refMovie = moviesForViewable.getFirst();
                    if (rs.getString("type").equals(("Saga"))) {
                        viewable = new Saga(rs.getInt("id"), rs.getString("name"), refMovie.getGenre(), refMovie.getDirector(), getTotalDurationFromMovies(moviesForViewable), "moviesForViewable", refMovie.getImage(), refMovie.getImagePath(), moviesForViewable);
                    }
                }
                return viewable;
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération d'un viewable");
        }
        return null;
    }

    @Override
    public ArrayList<Integer> getSeancesLinkedToViewable(int id) throws DaoException {
        ArrayList<Integer> seances = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM seances WHERE viewableid = " + id);
            while (rs.next()) {
                seances.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération des séances liées à un viewable");
        }
        return seances;
    }

    /**
     * Récupère le nombre de sagas liées à un film
     *
     * @param movieId
     * @return
     */
    @Override
    public int sagasLinkedToMovie(int movieId) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT count(*) FROM viewables v JOIN viewablecontains vc ON v.id = vc.viewableid WHERE v.type = 'Saga' AND vc.movieid = ?")) {
            pstmt.setInt(1, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération du nombre de sagas liées à un film");
        }
        return 0;
    }


}
