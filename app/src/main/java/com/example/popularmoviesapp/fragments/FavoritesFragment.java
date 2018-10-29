package com.example.popularmoviesapp.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.adapters.MovieAdapter;
import com.example.popularmoviesapp.data.FavoritesContract;
import com.example.popularmoviesapp.model.Movie;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{

    //Constants
    private static final int FAVORITE_LOADER_ID = 100;
    private static final int OFFSET = 0;
    private static final String SCROLL_POS = "scrollPosition";

    //Views
    private TextView emptyText;
    private RecyclerView recyclerView;

    //Objects
    private MovieAdapter adapter;

    //Variables
    private int scrollPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_fragment, container, false);

        //Initialize recycler view
        emptyText = v.findViewById(R.id.emptyText);
        recyclerView = v.findViewById(R.id.recyclerView);

        //Set Empty Text
        emptyText.setText(getString(R.string.no_favorites_found));

        //Scroll to previous position if fragment orientation is changed
        if(savedInstanceState != null)
            scrollPosition = savedInstanceState.getInt(SCROLL_POS);
        else
            scrollPosition = 0;


        //Initialize adapter and attach to RecyclerView
        adapter = new MovieAdapter(getContext(), new ArrayList<Movie>());

        //Create Layout animation, add animation and adapter to RecyclerView
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_top);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.setAdapter(adapter);

        //Initialize Favorite Loader
        getActivity().getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID, null, this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POS, ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = FavoritesContract.FavoriteEntry.CONTENT_URI;
        String[] projection = new String[]{FavoritesContract.FavoriteEntry._ID, FavoritesContract.FavoriteEntry.COLUMN_TITLE,
                FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE, FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,
                FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW};

        return new CursorLoader(getContext(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            ArrayList<Movie> movies = getMoviesFromCursor(data);
            adapter.setCursor(movies);
            recyclerView.startLayoutAnimation();
            ((GridLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(scrollPosition, OFFSET);
            emptyText.setVisibility(View.GONE);
        }
        else
            emptyText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(new ArrayList<Movie>());
    }

    private ArrayList<Movie> getMoviesFromCursor(Cursor cursor)
    {
      ArrayList<Movie> movies = new ArrayList<>();

      if(cursor.moveToFirst())
      {
          do{
              Movie m = new Movie();
              m.setId(cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoriteEntry._ID)));
              m.setTitle(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_TITLE)));
              m.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE)));
              m.setOverview(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW)));
              m.setImage(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH)));
              m.setRating(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE)));
              m.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE)));
              movies.add(m);
          } while (cursor.moveToNext());
      }
      return movies;
    }

}
