package be.helha.applicine.dao.impl;

import be.helha.applicine.dao.ViewableDAO;
import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.models.Movie;
import be.helha.applicine.models.Saga;
import be.helha.applicine.models.Viewable;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewableDAOImpl implements ViewableDAO {
    private Connection connection;

    public ViewableDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addViewableWithOneMovie(String title, String singleMovie, int id) {
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
                    throw new SQLException("Creating viewable failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addViewable(String name, String type, ArrayList<Integer> movieIDs) {
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
                    throw new SQLException("Creating viewable failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeViewable(Integer id) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM viewables WHERE id = " + id);
            removeViewableMovieCorrespondance(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateViewable(Integer id, String name, String type) {
        try {
            connection.createStatement().executeUpdate("UPDATE viewables SET name = '" + name + "', type = '" + type + "' WHERE id = " + id);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void addMovieToViewable(Integer viewableID, Integer movieID) {
        try {
            connection.createStatement().executeUpdate("INSERT INTO viewablecontains (viewableid, movieid) VALUES (" + viewableID + ", " + movieID + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeViewableMovieCorrespondance(Integer viewableID) {
        try {
            connection.createStatement().executeUpdate("DELETE FROM viewablecontains WHERE viewableid = " + viewableID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Movie> getMoviesFromViewable(Integer viewableID) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM viewablecontains WHERE viewableid = " + viewableID);
            while (rs.next()) {
                Movie movie = new MovieDAOImpl().getMovieById(rs.getInt("movieid"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    public ArrayList<Viewable> getAllViewables() {
        ArrayList<Viewable> array = new ArrayList<Viewable>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM viewables");
            while (rs.next()) {
                Viewable viewable = null;
                ArrayList<Movie> moviesForViewable = getMoviesFromViewable(rs.getInt("id"));
                if (moviesForViewable.size() == 1) {
                    viewable = moviesForViewable.getFirst();
                } else {
                    Movie refMovie = moviesForViewable.getFirst();
                    viewable = new Saga(rs.getInt("id"), rs.getString("name"), refMovie.getGenre(), refMovie.getDirector(), getTotalDurationFromMovies(moviesForViewable), "moviesForViewable", refMovie.getImagePath(), moviesForViewable);
                }

                array.add(viewable);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public Viewable getViewableById(int id) {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM viewables WHERE id = " + id);
            if (rs.next()) {
                Viewable viewable = null;
                ArrayList<Movie> moviesForViewable = getMoviesFromViewable(rs.getInt("id"));
                if (moviesForViewable.size() == 1) {
                    viewable = moviesForViewable.getFirst();
                } else {
                    Movie refMovie = moviesForViewable.getFirst();
                    if (rs.getString("type").equals(("Saga"))) {
                        viewable = new Saga(rs.getInt("id"), rs.getString("name"), refMovie.getGenre(), refMovie.getDirector(), getTotalDurationFromMovies(moviesForViewable), "moviesForViewable", refMovie.getImagePath(), moviesForViewable);
                    }
                }
                return viewable;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
