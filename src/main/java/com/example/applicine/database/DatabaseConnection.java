package com.example.applicine.database;
import com.example.applicine.models.Movie;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private static String DbURL = "jdbc:sqlite:src/main/resources/com/example/applicine/views/database/CinemaTor.db"; // URL de la base de données
    private static Connection connection = null;
    //j'initialise la connexion à la base de données au démarrage de l'application
    static {
        try {
            connection = DriverManager.getConnection(DbURL);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            System.out.println("Connexion échouée : " + e.getMessage());
        }
    }

    public static void removeMovies(int id) {
        String sqlQuery = "DELETE FROM movies WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
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
        String sql = "SELECT * FROM movies"; // Requête SQL pour récupérer les films
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movie movie = new Movie(rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), rs.getString("imagePath"));
                String sql = "SELECT * FROM movies";
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return movies;
        }
    }
    //retourne un film en fonction de l'id
    public static Movie getMovie(int id) {
        String sql = "SELECT * FROM movies WHERE id = ?"; // Requête SQL pour récupérer un film
        Movie movie = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            movie = new Movie(rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), rs.getString("imagePath"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return movie;
    }
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la fermeture de la connexion à la base de données : " + e.getMessage());
        }
    }
}
