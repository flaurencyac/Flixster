package com.example.flixster.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.flixster.Fragments.DetailFragment;
import com.example.flixster.Fragments.RecommendedFragment;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FragmentAdapter extends FragmentStateAdapter {

    public static final String TAG = "FragmentAdapter";
    Context context;
    List<Movie> movies;
    Movie movie;
    int id;

    // constructor for fragment adapter
    public FragmentAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle,
                           Context context, List<Movie> movies, Movie movie){
        super(fragmentManager, lifecycle);
        this.context = context;
        this.movies = movies;
        this.id = movie.getId();
        this.movie = movie;
    }


    // this method creates/returns a fragment given the tab position
    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new RecommendedFragment(context, movies, id);
            default:
                return new DetailFragment(context, movie);
        }
    }

    // gets num of tabs in tab layout
    @Override
    public int getItemCount() {
        return 2;
    }
}
