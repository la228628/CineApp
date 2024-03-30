package com.example.applicine.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.applicine.dao.MovieDAO;
import com.example.applicine.database.DatabaseConnection;
import com.example.applicine.models.Movie;

public class MovieDAOImpl implements MovieDAO {

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sqlQuery = "SELECT * FROM movies";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), rs.getString("imagePath")));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des films : " + e.getMessage());
        }
        return movies;
    }

    @Override
    public Movie getMovieById(int id) {
        String sqlQuery = "SELECT * FROM movies WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Movie(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), rs.getString("imagePath"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du film : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addMovie(Movie movie) {
        String sqlQuery = "INSERT INTO movies (title, genre, director, duration, synopsis, imagePath) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
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
        String sqlQuery = "UPDATE movies SET title = ?, genre = ?, director = ?, duration = ?, synopsis = ?, imagePath = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
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
        String sqlQuery = "DELETE FROM movies WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du film : " + e.getMessage());
        }
    }

    @Override
    public void removeAllMovies() {
        String sqlQuery = "DELETE FROM movies";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de tous les films : " + e.getMessage());
        }
    }
}