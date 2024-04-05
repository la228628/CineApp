package be.helha.applicine.database;

import java.sql.*;

/**
 * This class represents the connection to the database.
 */
public class DatabaseConnection {

    private static Connection connection;
    private static final String AppData = System.getenv("APPDATA");
    private static final String DbURL = "jdbc:sqlite:" + AppData + "/Applicine/CinemaTor.db";


    //private  static final String DbURL = "jdbc:sqlite:src/main/resources/com/example/applicine/views/database/CinemaTor.db";

    /**
     * This method get the connection to the database.
     *
     * @return The connection to the database.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DbURL);
                System.out.println("Connexion à la base de données établie");

                initializeDatabase();
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
        return connection;
    }

    /**
     * This method closes the connection to the database.
     */
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

    /**
     * This method initializes the database.
     */
    public static void initializeDatabase() {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, genre TEXT, director TEXT, duration INTEGER, synopsis TEXT, imagePath TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}