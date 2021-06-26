package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

// this class is made to parse through the API results (for movies) outside of the MainActivity class
// models are nouns that you operate on
// best practices: package up and categorize files in the MVC/project tree
@Parcel
public class Movie {

    // posterPath in JSON is relative path, so unusable/unrenderable bc not a full URL that I can put into Glide lib
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    Double voteAverage;
    Integer id;
    String releaseDate;
    String language;
    Integer voteCount;
    // declaring and storing a videoId property on Movie obj allows me to avoid fetching the same value more than once
    String videoId;


    // default constructor required for Parceler
    public Movie() {}

    // throws exception if any of the var assignments fails, whoever called the method handles the exception
    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        voteCount = jsonObject.getInt("vote_count");
        language = jsonObject.getString("original_language");
        releaseDate = jsonObject.getString("release_date");

        // get the video id using the Movies DB API
        AsyncHttpClient client = new AsyncHttpClient();
        String movieVideoUrl = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=4a0f75d6faf355d9b99e549328290e4e&language=en-US", this.id);
        client.get(movieVideoUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    videoId = results.getJSONObject(0).getString("key");
                    Log.d("ID", ""+videoId);
                } catch (JSONException e){
                    Log.d("MOVIE", "JSON Exception: "+ e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String s, Throwable throwable) {
                Log.d("MOVIE", "onFailure");
            }
        });
    }

    // this method iterates through the JSON array and constructs a movie for each element in the JSON array
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        // replaced w342 with the fetched poster image sizes
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getVideoId() {return videoId; }

    public String getLanguage() {return language;}

    public String getReleaseDate() {return releaseDate;}

    public Integer getVoteCount() {return voteCount;}

    public Integer getId() {
        return id;
    }
}
