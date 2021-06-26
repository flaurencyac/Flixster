package com.example.flixster.adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

//import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

// the adapter
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // a context is where the adapter is being constructed from and is needed to inflate a view
    Context context;
    List<Movie> movies;
    int radius = 25; // corner radius, higher value = more rounded
    int margin = 5; // crop margin, set to 0 for corners with no crop

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    // Expensive relative to onBindViewHolder, we only need to create as many view holders as will fit on the screen
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        // wrap the view inside a view holder
        return new ViewHolder(movieView);
    }

    // Involves populating data into the view through viewholder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the view holder
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // class CANNOT not be static
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // declare for each view
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        String imageURL;

        // constructor
        public ViewHolder (View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            // bug : placeholder image doesn't load for each poster image, only a singular one in the center
            // tried to resolve with centercrop(), which made it worse
            // checked whether or not the layout width and heights were all okay, they were
            // resolution: had to use Pixel 4 emulator and enable auto-rotate in settings

            // bug: kept reverting to portrait layout
            // resolution: had to use Pixel 4 emulator and enable auto-rotate in settings
            
            // if phone in landscape then imageUrl = back drop image
            // else in portrait then imageUrl = poster image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
                Glide.with(context)
                        .load(imageURL)
                        .placeholder(R.drawable.flicks_backdrop_placeholder)
                        .transform(new CenterInside(), new RoundedCornersTransformation(radius, margin))
                        .into(ivPoster);
            } else {
                imageURL = movie.getPosterPath();
                Glide.with(context)
                        .load(imageURL)
                        .placeholder(R.drawable.flicks_movie_placeholder)
                        .transform(new CenterInside(), new RoundedCornersTransformation(radius, margin))
                        //.transform(new CenterInside(), new RoundedCorners(24))
                        .into(ivPoster);
            }

        }

        @Override
        public void onClick(View v) {
            // Gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
