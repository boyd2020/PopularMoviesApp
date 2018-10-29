package com.example.popularmoviesapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.popularmoviesapp.model.Review;
import com.example.popularmoviesapp.utils.MovieUtil;

import java.util.List;

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {

    private String movieId;

    public ReviewLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public List<Review> loadInBackground() {
        //Get Movie Link
        String link = MovieUtil.getMovieReviewUrl(movieId);
        return MovieUtil.getMovieReviewsFromServer(link);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
