package com.example.flixster.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.MainActivity;
import com.example.flixster.R;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class RecommendedFragment extends Fragment {

    public static final String TAG = "RecommendedFragment";
    public static final String RECOMMENDED_URL = MainActivity.BASE_URL + "movie/%d/similar?api_key=4a0f75d6faf355d9b99e549328290e4e&language=en-US&page=1";
    Context context;

    // declare the recycler view, movies list, movie id, and adapter
    RecyclerView rvMovies;
    List<Movie> moviesList;
    MovieAdapter adapter;
    int id;

    // constructor of fragment containing recycler view for similar movies
    public RecommendedFragment(Context context, List<Movie> movies, int id) {
        this.context = context;
        this.id = id;
        this.moviesList = movies;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // define Recycler view and movies list array
        rvMovies = view.findViewById(R.id.rvRecommendedMovies);
        moviesList = new ArrayList<>();
        // create a layout manager and assign it to the recycler view
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rvMovies.setLayoutManager(llm);
        // create a new movie adapter and assign it to the recycler view
        adapter = new MovieAdapter(context, moviesList);
        rvMovies.setAdapter(adapter);

        // call in separate method to create the list of recommended movies using the Get Similar Movies API
        // in my app, I presented similar movies to the user when they clicked on the recommended movies tab
        // pass in the movie id to the method so that the API has access to it
        getRecommendedMovies(id);
        // set the data
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommended2, container, false);
    }

    // Usage of Get Similar Movies to get the recommended movies via similarity to movie
    public void getRecommendedMovies(int id){
        AsyncHttpClient client  = new AsyncHttpClient();
        String request = String.format(RECOMMENDED_URL, id);
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject object = json.jsonObject;
                try {
                    JSONArray results = object.getJSONArray("results");
                    moviesList.addAll(Movie.fromJsonArray(results));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception in RecommendedFragment.Java", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "Failure: " + throwable.getMessage());
            }
        });
    }
}