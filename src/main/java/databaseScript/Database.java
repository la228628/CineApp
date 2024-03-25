package databaseScript;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    public static void main(String[] args) {
        String url = "database.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS Film ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "name VARCHAR(45) NOT NULL,"
                        + "description VARCHAR(6000) NULL,"
                        + "genre VARCHAR(45) NOT NULL,"
                        + "actors VARCHAR(1000) NULL"
                        + ");";

                conn.createStatement().executeUpdate(createTableSQL);

                String insertSQL = "INSERT INTO Film (name, description, genre, actors) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
                String[][] insertValues = {
                        {"Sausage Party", "J'adore les saucisses", "humour", "moi"},
                        {"Sausage Party", "J'adore les saucisses", "humour", "moi"}
                };
                for(int i = 0; i < insertValues.length; i++){
                    for(int j = 0; j < insertValues[i].length; j++){
                        System.out.println(insertValues[i][j]);
                        preparedStatement.setString(j, insertValues[i][j]);
                    }
                    preparedStatement.executeUpdate();
                }
                // Set values for the prepared statement here
                // For example:
                // preparedStatement.setString(1, "Film Name");
                // preparedStatement.setString(2, "Description");
                // preparedStatement.setString(3, "Genre");
                // preparedStatement.setString(4, "Actors");

                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
