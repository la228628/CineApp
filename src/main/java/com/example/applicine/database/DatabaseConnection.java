package com.example.applicine.database;
import com.example.applicine.models.Movie;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {

    private static Connection connection = connection();
    private static final String DbURL = "jdbc:sqlite:src/main/resources/com/example/applicine/views/database/CinemaTor.db";
    private static Connection connection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DbURL);
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
        try (Connection conn = connection  ;
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

            PreparedStatement statement = getPreparedStatement(movie, connection, sqlQuery);
            System.out.println("Movie added");
            //statement.executeUpdate();
            statement.close();
            //connection.close();
        } catch (SQLException e) {
            System.out.println("Sql error : " + e.getMessage());

        }
    }

    public static int getNewMovieId() throws SQLException {
        String sqlQuery = "SELECT count(*) from movies";
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

    //retourne un film en fonction de l'id
    public static Movie getMovie(int id) {
        String sql = "SELECT * FROM movies WHERE id = ?"; // Requête SQL pour récupérer un film
        Movie movie = null;
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            movie = new Movie(rs.getString("title"), rs.getString("genre"), rs.getString("director"), rs.getInt("duration"), rs.getString("synopsis"), rs.getString("imagePath"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return movie;
    }


    public static void fillDB() throws SQLException {
        String [] movieNames = {"Sausage party", "The Godfather", "The Dark Knight", "The Lord of the Rings: The Return of the King", "L'orange mécanique", "Narnia", "Inception", "Fight Club","The Lord of the ring", "Forrest Gump", "Ping Pong"};
        String [] genres = {"Comedy", "Crime", "Action", "Adventure", "Drama", "Biography", "Action", "Drama", "Drama","adventure", "Sport"};

        String [] directors = {"Greg Tiernan, Conrad Vernon", "Francis Ford Coppola", "Christopher Nolan", "Peter Jackson", "Stanley Kubrick", "Steven Spielberg", "Christopher Nolan", "David Fincher", "Robert Zemeckis", "Christopher Nolan", "Fumihiko Sori"};

        int [] durations = {89, 175, 152, 201, 136, 195, 148, 139, 142,120, 114};

        String [] synopses = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        String [] imagePaths = {"file:src/main/resources/com/example/applicine/views/images/1.jpg", "file:src/main/resources/com/example/applicine/views/images/2.jpg", "file:src/main/resources/com/example/applicine/views/images/3.jpg", "file:src/main/resources/com/example/applicine/views/images/4.jpg", "file:src/main/resources/com/example/applicine/views/images/5.jpg", "file:src/main/resources/com/example/applicine/views/images/6.jpg", "file:src/main/resources/com/example/applicine/views/images/7.jpg", "file:src/main/resources/com/example/applicine/views/images/8.jpg", "file:src/main/resources/com/example/applicine/views/images/9.jpg", "file:src/main/resources/com/example/applicine/views/images/10.jpg", "file:src/main/resources/com/example/applicine/views/images/11.jpg"};

        for (int i = 0; i < movieNames.length; i++) {
            try {
                Movie movie = new Movie(movieNames[i], genres[i], directors[i], durations[i], synopses[i], imagePaths[i]);
                AddMovie(movie);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        connection.close();
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