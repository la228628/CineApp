package com.example.applicine.database;
import com.example.applicine.models.Movie;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {

    private static Connection connection;
    private static final String DbURL = "jdbc:sqlite:src/main/resources/com/example/applicine/views/database/CinemaTor.db";
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DbURL);
                System.out.println("Connexion à la base de données établie");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            }
        }
        return connection;
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