package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.FragmentAdapter;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    Context context;
    Movie movie;
    public static final String TAG = "MovieDetailsActivity";

    // declare the view fields
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivPoster;
    String imageURL;

    // ------Declare ViewPager2, FragmentAdapter, & TabLayout--------------------
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentAdapter;

    int drawablePath;
    int radius = 25;
    int margin = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // I used View Boilerplate w/ ViewBinding in order to reduce findViewById() lookups
        // replaced setContentView(R.layout.activity_movie_details); with:
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        // Use fields (binding.title, binding.subtitle, binding.footer, etc.) to resolve view objects
        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;
        ivPoster = binding.ivPoster;
        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager2;

        this.context = this;

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        // ---------------Fragment + ViewPager2 Usage----------------------
        // I had to use a frame layout, ViewPager, Fragments, and a Fragment Adapter
        // For recommended movies I created a linear layout, new ViewhHolder, and new movie adapter to make a list of similar movies
        // create fragment manager
        FragmentManager fm = getSupportFragmentManager();
        // declare and create an empty list of recommended movies (note: recommended is uses the similar movies API)
        List<Movie> recommendedMovies = new ArrayList<>();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle(), this, recommendedMovies, movie);
        // assign your fragment adapter to the viewPager
        viewPager2.setAdapter(fragmentAdapter);
        // create tabs on the tab layout
        tabLayout.addTab(tabLayout.newTab().setText("Recommended Movies"));
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        // assign onTabSelectedListener to tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        // -------------------------------------------------------

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        // if phone in landscape then imageUrl = back drop image
        // else in portrait then imageUrl = poster image
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
            drawablePath = R.drawable.flicks_backdrop_placeholder;
        } else {
            imageURL = movie.getPosterPath();
            drawablePath = R.drawable.flicks_movie_placeholder;
        }
        Glide.with(context)
                .load(imageURL)
                .placeholder(drawablePath)
                .transform(new CenterInside(), new RoundedCornersTransformation(radius, margin))
                .into(ivPoster);

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make intent in onclick method from details to trailer activities
                Intent intent = new Intent(context, MovieTrailerActivity.class);
                // package id as a string w/ putExtra() aka serialize the movie using parceler, use movieObj as key
                intent.putExtra("vidId", movie.getVideoId());
                // show the activity
                context.startActivity(intent);
            }
        });


}}