package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectionTest {
    private static Connection connection;
    //private static final String AppData = System.getenv("APPDATA");
    //private static final String DbURL = "jdbc:sqlite:" + AppData + "/Applicine/CinemaTor.db";

    private static final String DbURL = "jdbc:sqlite:src/test/java/database/CinemaTorTest.db";

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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movies (id INTEGER PRIMARY KEY, title TEXT, genre TEXT, director TEXT, duration INTEGER, synopsis TEXT, imagePath TEXT)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS viewables(id INTEGER PRIMARY KEY, name text not null ,type text not null)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS viewablecontains(viewableid INTEGER not null, movieid INTEGER NOT NULL, CONSTRAINT viewableidfk FOREIGN KEY (viewableid) REFERENCES viewables(id), CONSTRAINT movieidfk FOREIGN KEY (movieid) REFERENCES movies(id) )");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rooms ( id integer PRIMARY KEY, seatsnumber integer not null)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS clients ( id integer primary key, name text not null, email text not null, username text not null,hashedpassword text not null, CONSTRAINT uniqueusername UNIQUE (username), CONSTRAINT uniqueemail UNIQUE (email))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS seances(id INTEGER PRIMARY KEY, viewableid INTEGER NOT NULL, roomid INTEGER NOT NULL, version text NOT NULL, time DATETIME NOT NULL, CONSTRAINT viewableidfk FOREIGN KEY (viewableid) REFERENCES viewables(id), CONSTRAINT roomidfk FOREIGN KEY (roomid) REFERENCES rooms(id))");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tickets ( id integer PRIMARY KEY, clientid integer NOT NULL, seatcode VARCHAR(4) NOT NULL, price double NOT NULL, clienttype text NOT NULL, verificationcode text NOT NULL, seanceid integer NOT NULL, CONSTRAINT clientidfk FOREIGN KEY (clientid) REFERENCES clients(id), CONSTRAINT seanceidfk FOREIGN KEY (seanceid) REFERENCES seances(id) )");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

