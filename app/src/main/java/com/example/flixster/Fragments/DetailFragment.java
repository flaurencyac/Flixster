package com.example.flixster.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.MainActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.FragmentDetailBinding;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


// Next step for this fragment class would be to use the ViewBinding library to reduce calls to findViewById
public class DetailFragment extends Fragment {

    public static final String DETAILS_URL = MainActivity.BASE_URL + "/movie/%d?api_key=4a0f75d6faf355d9b99e549328290e4e&language=en-US&page=1";
    public static final String TAG = "DetailsFragment";


    // declare view fields
    TextView tvLanguage;
    TextView tvReleaseDate;
    TextView tvVotes;
    TextView tvGenre;
    TextView tvRuntime;

    Context context;
    String language;
    String releaseDate;
    Integer votes;
    Movie movie;
    ArrayList<String> genreList = new ArrayList<>();
    Integer runtime;

    // constructor for the Detail fragment
    public DetailFragment(Context context, Movie movie) {
        this.context = context;
        this.movie = movie;
        this.releaseDate = movie.getReleaseDate();
        this.votes = movie.getVoteCount();
        if (this.movie.getLanguage().equals("en")) {
            this.language = "English";
        } else {
            this.language = movie.getLanguage();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // in this method I define the fragment's necessary variable (ie. tvLanguage, tvGenre, etc.) and use the Get Movie API to obtain the movie's runtime and genres
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // find corresponding views via ID, in the future I can use the View Binding library to do this
        // I did implement view binding in the Activity classes though!
        tvLanguage = view.findViewById(R.id.tvLanguage);
        tvReleaseDate = view.findViewById(R.id.tvReleaseDate);
        tvVotes = view.findViewById(R.id.tvVotes);
        tvGenre = view.findViewById(R.id.tvGenre);
        tvRuntime = view.findViewById(R.id.tvRuntime);

        // create client
        AsyncHttpClient client = new AsyncHttpClient();
        String request = String.format(DETAILS_URL, movie.getId());
        client.get(request, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject object = json.jsonObject;
                try{
                    // assign runtime
                    runtime = object.getInt("runtime");
                    // set the runtime text on the text view
                    tvRuntime.setText(String.format("Runtime: %d hours and %d minutes",  (runtime/60), (runtime%60)));
                    Log.d("hello", String.valueOf(runtime));
                    // parse through JSON to get array of objects that each have key: "name" val: "genre_name"
                    JSONArray genresJsonArray = object.getJSONArray("genres");
                    // use for loop to populate an array of genre names
                    for (int n = 0; n < genresJsonArray.length(); n++) {
                        genreList.add(genresJsonArray.getJSONObject(n).getString("name"));
                    }
                    String genreListString = genreList.toString();
                    int len = genreListString.length();
                    // use substring to get rid of brackets before displaying genre list
                    tvGenre.setText("Genre: "+ genreListString.substring(1,len-1));
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception in DetailFragment.java", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "Failure: " + throwable.getMessage());
            }
        });

        // Set Data here
        tvLanguage.setText("Original Language: "+  language);
        tvVotes.setText("Vote Count: " + votes);
        tvReleaseDate.setText("Release Date: "+ releaseDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}