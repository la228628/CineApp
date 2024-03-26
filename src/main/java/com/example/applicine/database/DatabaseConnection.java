package com.example.applicine.database;
import com.example.applicine.models.Movie;


import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private static final String DbURL = "jdbc:sqlite:src/main/resources/com/example/applicine/views/database/CinemaTor.db";
    private static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DbURL); // Connexion à la base de données
            if(connection == null) {
                System.out.println("Connexion échouée");
            } else{
                System.out.println("Connexion établie");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void removeMovies(int id) {
        String sqlQuery = "DELETE FROM movies WHERE id = ?";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void AddMovie(Movie movie) throws SQLException {
        try {
            String sqlQuery = "INSERT INTO movies(title, genre, director, duration, synopsis, imagePath) VALUES(?,?,?,?,?,?)";
            Connection connection = connect();
            PreparedStatement statement = getPreparedStatement(movie, connection, sqlQuery);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Sql error : " + e.getMessage());
        }
    }
    public static int getNewMovieId() throws SQLException {
        String sqlQuery = "SELECT count(*) from movies";
        Connection connection = connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        System.out.println(resultSet.getInt(1));
        return resultSet.getInt(1);
    }
    private static PreparedStatement getPreparedStatement(Movie movie, Connection connection, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, movie.getTitle());
        statement.setString(2, movie.getGenre());
        statement.setString(3, movie.getDirector());
        statement.setInt(4, movie.getDuration());
        statement.setString(5, movie.getSynopsis());
        statement.setString(6, movie.getImagePath());
        statement.executeUpdate();
        return statement;
    }
    public static ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        String sql = "SELECT * FROM movies";
        try{
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Movie movie = new Movie(resultSet.getString("title"), resultSet.getString("genre"), resultSet.getString("director"), resultSet.getInt("duration"), resultSet.getString("synopsis"), resultSet.getString("imagePath"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }
    public static Movie getMovie(int id) {
        String sql = "SELECT * FROM movies WHERE id = ?"; // Requête SQL pour récupérer un film
        Movie movie = null;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            movie = new Movie(rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), rs.getString("imagePath"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return movie;
    }
}
