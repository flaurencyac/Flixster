package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;
import com.facebook.stetho.common.ArrayListAccumulator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=4a0f75d6faf355d9b99e549328290e4e&language=en-US&page=1";
    public static final String TAG = "MainActivity";

    // declared as a member var in order to make making the recycler view easier
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        RecyclerView rvMovies = binding.rvMovies;
        movies = new ArrayList<>();

        // Create the adapter
        MovieAdapter movieAdapter =  new MovieAdapter(this, movies);

        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        // Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        // make get request for movies that are now playing for our client
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                // Use a try catch in case parsing through the JSON gives you exceptions
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    // log at the info level, if successful
                    Log.i(TAG, "Results" + results.toString());
                    // defined movies w/ the list of movie obj using Movie class' method
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: "+ movies.size());
                } catch (JSONException e){
                    // log at the exception level, if unsuccessful
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }


}