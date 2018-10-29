package com.example.popularmoviesapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.popularmoviesapp.model.Trailer;
import com.example.popularmoviesapp.utils.MovieUtil;

import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>> {

    private String movieId;

    public TrailerLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public List<Trailer> loadInBackground() {
        //Get Movie Link
        String link = MovieUtil.getMovieTrailerUrl(movieId);
        return MovieUtil.getMovieTrailersFromServer(link);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
