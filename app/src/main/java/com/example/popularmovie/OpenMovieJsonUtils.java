package com.example.popularmovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class OpenMovieJsonUtils {

    public static List<Movie> getMovieListFromJson (String movieJsonString) throws JSONException{
        List<Movie> movies = new ArrayList<>();

        final String RESULTS_ARRAY = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String VOTE_AVERAGE = "vote_average";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String DATE = "release_date";
        final String MESSAGE_CODE = "cod";

        JSONObject movieJson = new JSONObject(movieJsonString);

        if (movieJson.has(MESSAGE_CODE)) {

            int errorCode = movieJson.getInt(MESSAGE_CODE);
            switch (errorCode){
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray resultsArray = movieJson.getJSONArray(RESULTS_ARRAY);
        for (int i =0; i<resultsArray.length(); i++){
            JSONObject movieSingeItem = resultsArray.getJSONObject(i);

            Double rate = movieSingeItem.getDouble(VOTE_AVERAGE);
            String poster = movieSingeItem.getString(POSTER_PATH);
            String originalTitle = movieSingeItem.getString(ORIGINAL_TITLE);
            String overview = movieSingeItem.getString(OVERVIEW);
            String date = movieSingeItem.getString(DATE);

            Movie singleMovie = new Movie(originalTitle, date, rate, overview, poster);
            movies.add(singleMovie);
        }
        return movies;

    }
}
