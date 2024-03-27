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
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/now_playing?language=fr-BE&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", APIkey)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    public void addMoviesToDatabase() throws IOException, SQLException {
    Response response = getMovies();
    String jsonData = response.body().string();
    JSONObject jobj = new JSONObject(jsonData);
    JSONArray jarr = jobj.getJSONArray("results");

    for (int i = 0; i < jarr.length(); i++) {
        JSONObject movieJson = jarr.getJSONObject(i);
        String title = movieJson.getString("title");
        String synopsis = movieJson.getString("overview");
        String imagePath = movieJson.getString("poster_path");

        int movieId = movieJson.getInt("id");

        // Get movie details
        Response detailsResponse = getMovieDetails(movieId);
        String detailsData = detailsResponse.body().string();
        JSONObject detailsObj = new JSONObject(detailsData);
        JSONArray genresArr = detailsObj.getJSONArray("genres");
        String genre = genresArr.getJSONObject(0).getString("name"); // Get the first genre
        int duration = detailsObj.getInt("runtime");

        // Get movie credits
        Response creditsResponse = getMovieCredits(movieId);
        String creditsData = creditsResponse.body().string();
        JSONObject creditsObj = new JSONObject(creditsData);
        JSONArray crewArr = creditsObj.getJSONArray("crew");
        String director = "";
        for (int j = 0; j < crewArr.length(); j++) {
            JSONObject crewMember = crewArr.getJSONObject(j);
            if (crewMember.getString("job").equals("Director")) {
                director = crewMember.getString("name");
                break;
            }
        }

        Movie movie = new Movie(title, genre, director, duration, synopsis, imagePath);
        try {
            DatabaseConnection.AddMovie(movie);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public Response getMovieDetails(int movieId) throws IOException {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://api.themoviedb.org/3/movie/" + movieId + "?language=en-US")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", APIkey)
            .build();

    Response response = client.newCall(request).execute();
    return response;
}

public Response getMovieCredits(int movieId) throws IOException {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://api.themoviedb.org/3/movie/" + movieId + "/credits?language=en-US")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", APIkey)
            .build();

    Response response = client.newCall(request).execute();
    return response;
}

    public static void main(String[] args) throws IOException, SQLException {
        DatabaseConnection.deleteTableEntries();
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.addMoviesToDatabase();
    }
}
