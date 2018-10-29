package com.example.popularmoviesapp.loaders;

import android.content.Context;
import com.example.popularmoviesapp.model.Movie;
import android.support.v4.content.AsyncTaskLoader;

import com.example.popularmoviesapp.utils.MovieUtil;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    String path;

    public MovieLoader(Context context, String path) {
        super(context);
        this.path = path;
    }

    @Override
    public List<Movie> loadInBackground() {
        return MovieUtil.getMoviesFromServer(path);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

}
