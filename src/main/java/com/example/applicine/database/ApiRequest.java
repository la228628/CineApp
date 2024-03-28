package com.example.applicine.database;

import com.example.applicine.models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

public class ApiRequest {

    private static final String APIkey = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5OTlkY2U5OGE2MmRiZjY1MTVjMzIwNTNiNmIwNDRlZCIsInN1YiI6IjY2MDE2YTZmMzc4MDYyMDE2MjNhMWQxMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.tmTHxA8Y_vY4aNKMW26hL2pffx4jFX-RZZThVSYX-j0";

    public static Response getMovies() throws IOException {
        return executeRequest("https://api.themoviedb.org/3/movie/now_playing?language=fr-BE&page=1");
    }

    public void addMoviesToDatabase() throws IOException, SQLException {
        JSONArray results = new JSONObject(getMovies().body().string()).getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJson = results.getJSONObject(i);
            Movie movie = createMovieFromJson(movieJson);
            DatabaseConnection.AddMovie(movie);
        }
    }

    public Response getMovieDetails(int movieId) throws IOException {
        return executeRequest("https://api.themoviedb.org/3/movie/" + movieId + "?language=en-US");
    }

    public Response getMovieCredits(int movieId) throws IOException {
        return executeRequest("https://api.themoviedb.org/3/movie/" + movieId + "/credits?language=en-US");
    }

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

    private Movie createMovieFromJson(JSONObject movieJson) throws IOException, SQLException {
        int movieId = movieJson.getInt("id");
        JSONObject detailsObj = new JSONObject(getMovieDetails(movieId).body().string());
        JSONObject creditsObj = new JSONObject(getMovieCredits(movieId).body().string());

        String title = movieJson.getString("title");
        String synopsis = movieJson.getString("overview");
        String imageUrl = "https://image.tmdb.org/t/p/w500" + movieJson.getString("poster_path");
        String genre = detailsObj.getJSONArray("genres").getJSONObject(0).getString("name");
        int duration = detailsObj.getInt("runtime");
        String director = getDirectorFromCredits(creditsObj);

        return new Movie(title, genre, director, duration, synopsis, imageUrl);
    }

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

    public static void main(String[] args) throws IOException, SQLException {
        DatabaseConnection.deleteTableEntries();
        new ApiRequest().addMoviesToDatabase();
    }
}