package com.example.popularmoviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmoviesapp.activities.MainActivity;
import com.example.popularmoviesapp.activities.MovieDetailActivity;
import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by boyd on 5/21/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies;
    private Context context;


    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        //Retrieve movie from the list
        Movie m = movies.get(position);

        //Add Movie details to the views
        Picasso.with(context).load(m.getImage()).placeholder(R.drawable.ic_photo_camera_black_48dp).into(holder.movieImage);

        Log.e("Image", "I: " + m.getImage());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setCursor(List<Movie> m)
    {
        movies.clear();
        movies.addAll(m);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);

            movieImage = itemView.findViewById(R.id.movieImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creates intent and adds the selected movie
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MainActivity.KEY_MOVIE, movies.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
