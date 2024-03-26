package com.example.applicine.database;
import com.example.applicine.models.Movie;


import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private static final String DbURL = "jdbc:sqlite:src/main/resources/com/example/applicine/views/database/CinemaTor.db"; // URL de la base de données

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DbURL); // Connexion à la base de données
            if(conn == null) {
                System.out.println("Connexion échouée");
            } else{
                System.out.println("Connexion établie");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void removeMovies(int id) {
        String sql = "DELETE FROM movies WHERE id = ?"; // Requête SQL pour supprimer un film
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int AddMovie(Movie movie) {
        String sql = "INSERT INTO movies(title, genre, director, duration, synopsis, imagePath) VALUES(?,?,?,?,?,?)"; // Requête SQL pour ajouter un film
        //On récupère l'id du film dans la base de données et je l'ajoute à l'objet Movie
        int idInDatabase = 0;
        Connection conn = null;
        try {
            conn = connect();
            PreparedStatement pstmt = getPreparedStatement(movie, conn, sql);
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        //On récupère l'id du film dans la base de données
                        idInDatabase = generatedKeys.getInt(1);
                    }
                    else {
                        throw new SQLException("Creation du film échouée, aucun ID récupéré");
                    }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            if (conn != null) {
                try {
                    conn.close(); // Fermer la connexion
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return idInDatabase;
    }


    //Prepare l'order SQL pour ajouter un film à la base de données
    private static PreparedStatement getPreparedStatement(Movie movie, Connection conn, String sql) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, movie.getTitle());
        pstmt.setString(2, movie.getGenre());
        pstmt.setString(3, movie.getDirector());
        pstmt.setInt(4, movie.getDuration());
        pstmt.setString(5, movie.getSynopsis());
        pstmt.setString(6, movie.getImagePath());
        pstmt.executeUpdate();
        return pstmt;
    }

    //retourne tout le contenu de la table movies sous forme d'une liste de films
    public static ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        String sql = "SELECT * FROM movies"; // Requête SQL pour récupérer les films
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movie movie = new Movie(rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), rs.getString("imagePath"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }

    //retourne un film en fonction de l'id

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
