package be.helha.applicine.server.database;

import be.helha.applicine.server.dao.MovieDAO;
import be.helha.applicine.server.dao.impl.MovieDAOImpl;
import be.helha.applicine.common.models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ArrayList;

public class ApiRequest {

    private static final String APIkey = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5OTlkY2U5OGE2MmRiZjY1MTVjMzIwNTNiNmIwNDRlZCIsInN1YiI6IjY2MDE2YTZmMzc4MDYyMDE2MjNhMWQxMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.tmTHxA8Y_vY4aNKMW26hL2pffx4jFX-RZZThVSYX-j0";

    private static MovieDAO movieDAO = new MovieDAOImpl();

    public static Response getMovies() throws IOException {
        return executeRequest("https://api.themoviedb.org/3/movie/now_playing?language=fr-BE&page=1");
    }

    /**
     * download image from url and save it to target directory
     * @param imageUrl
     * @param targetDirectory
     * @return
     * @throws IOException
     */

    private String downloadImage(String imageUrl, String targetDirectory) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path outputPath = Paths.get(targetDirectory, fileName);
            Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
            return outputPath.toString();
        }
    }

    /**
     * get movies from the api (we use themoviedb.org)
     * @return
     */
    public List<Movie> getApiMovies() {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray results = new JSONObject(getMovies().body().string()).getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                Movie movie = createMovieFromJson(movieJson);
                movies.add(movie);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * get movie details from the api
     * @param movieId
     * @return
     * @throws IOException
     */
    private Response getMovieDetails(int movieId) throws IOException {
        return executeRequest("https://api.themoviedb.org/3/movie/" + movieId + "?language=en-US");
    }

    /**
     * get movie credits from the api
     * @param movieId
     * @return
     * @throws IOException
     */
    private Response getMovieCredits(int movieId) throws IOException {
        return executeRequest("https://api.themoviedb.org/3/movie/" + movieId + "/credits?language=en-US");
    }

    /**
     * execute request to the api
     * @param url
     * @return
     * @throws IOException
     */
    private static Response executeRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", APIkey)
                .build();

        return client.newCall(request).execute();
    }

    /**
     * create a movie object from a json object
     * @param movieJson
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private Movie createMovieFromJson(JSONObject movieJson) throws IOException, SQLException {
        int movieId = movieJson.getInt("id");
        JSONObject detailsObj = new JSONObject(getMovieDetails(movieId).body().string());
        JSONObject creditsObj = new JSONObject(getMovieCredits(movieId).body().string());

        String title = movieJson.getString("title");
        String synopsis = movieJson.getString("overview");
        String imageUrl = "https://image.tmdb.org/t/p/w500" + movieJson.getString("poster_path");
        String getAppdata = System.getenv("APPDATA");
        String localImagePath = null;
        System.out.println(getAppdata);
        localImagePath = downloadImage(imageUrl, getAppdata + "/Applicine/images/");


        //String localImagePath = downloadImage(imageUrl, "src/main/resources/com/example/applicine/views/images/");
        String ImagePath = "file:" + localImagePath;
        String genre = detailsObj.getJSONArray("genres").getJSONObject(0).getString("name");
        int duration = detailsObj.getInt("runtime");
        String director = getDirectorFromCredits(creditsObj);

        return new Movie(title, genre, director, duration, synopsis, ImagePath);
    }


    /**
     * get the director from the credits
     * @param creditsObj
     * @return
     */
    private String getDirectorFromCredits(JSONObject creditsObj) {
        JSONArray crewArr = creditsObj.getJSONArray("crew");
        for (int j = 0; j < crewArr.length(); j++) {
            JSONObject crewMember = crewArr.getJSONObject(j);
            if (crewMember.getString("job").equals("Director")) {
                return crewMember.getString("name");
            }
        }
        return "";
    }

    /**
     * fill the database with movies that are currently playing
     */
    public void fillDatabase() {
        List<Movie> movies = getApiMovies();
        for (Movie movie : movies) {
            movieDAO.addMovie(movie);
        }
    }
}