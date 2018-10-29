package com.example.popularmoviesapp.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.activities.MainActivity;
import com.example.popularmoviesapp.adapters.MoviePagerAdapter;
import com.example.popularmoviesapp.data.FavoritesContract;
import com.example.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by boyd on 5/21/2018.
 */

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1200;

    //Views
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton favoriteFab;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView originalTitleTextView, movieDateTextView, movieRatingTextView, movieSummaryTextView;
    private ImageView movieImageView, movieHeaderImageView;

    //Object
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize Views
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        favoriteFab = findViewById(R.id.favoriteFab);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        originalTitleTextView = findViewById(R.id.movieOriginalTitleTextView);
        movieDateTextView = findViewById(R.id.movieDateTextView);
        movieRatingTextView = findViewById(R.id.movieRatingTextView);
        movieSummaryTextView = findViewById(R.id.movieSummaryTextView);
        movieImageView = findViewById(R.id.movieImageView);
        movieHeaderImageView = findViewById(R.id.movieHeaderImageView);

        if(savedInstanceState != null)
            movie = (Movie) savedInstanceState.getParcelable(MainActivity.KEY_MOVIE);
        else
            movie = getIntent().getParcelableExtra(MainActivity.KEY_MOVIE);

        addTabsAndPager();
        addData();

        getSupportLoaderManager().destroyLoader(LOADER_ID);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        favoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(favoriteFab.isSelected())
                    removeFromFavorites();
                else
                    addToFavorites();
            }
        });
    }

    private void addToFavorites()
    {
        //Get Uri
        Uri uri = FavoritesContract.FavoriteEntry.CONTENT_URI;

        //Add Values
        ContentValues values = new ContentValues();
        values.put(FavoritesContract.FavoriteEntry._ID, movie.getId());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_TITLE, movie.getTitle());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getImage());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getRating());

        Log.e("Image", "I: " + movie.getImage());
        //Insert into db
        getContentResolver().insert(uri, values);

        //Display message stating favorite has been added
        Snackbar.make(findViewById(R.id.movieDetailLayout),
                getString(R.string.favorite_added), Snackbar.LENGTH_LONG).show();
    }

    private void removeFromFavorites()
    {
        //Delete Row from Favorites Table
        Uri uri = FavoritesContract.FavoriteEntry.buildFlavorsUri(movie.getId());
        getContentResolver().delete(uri, null, null);

        //Display message stating favorite has been removed
        Snackbar.make(findViewById(R.id.movieDetailLayout),
                getString(R.string.favorite_removed), Snackbar.LENGTH_LONG).show();
    }

    private void addTabsAndPager()
    {
        //Add Tabs to Layout
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.trailers)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.reviews)));

        //Add View pager adapter and link to tab layout listener
        viewPager.setAdapter(new MoviePagerAdapter(getSupportFragmentManager(), String.valueOf(movie.getId())));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Add TabLayout Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.KEY_MOVIE, movie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Verify if Movie is saved in local db
        String[] projection = new String[]{FavoritesContract.FavoriteEntry._ID};
        Uri uri = FavoritesContract.FavoriteEntry.buildFlavorsUri(movie.getId());

        return new CursorLoader(getApplicationContext(), uri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        isFavorite(data.moveToFirst());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void isFavorite(boolean favorite)
    {
        if(favorite)
        {
            favoriteFab.setImageResource(R.drawable.baseline_favorite_black_48);
            favoriteFab.setSelected(true);
        }
        else
        {
            favoriteFab.setSelected(false);
            favoriteFab.setImageResource(R.drawable.baseline_favorite_border_black_48);
        }

    }

    private void addData()
    {
        collapsingToolbar.setTitle(movie.getTitle());
        Picasso.with(this).load(movie.getImage()).into(movieImageView);
        Picasso.with(this).load(movie.getImage()).into(movieHeaderImageView);


        if(!movie.getOriginalTitle().isEmpty())
            originalTitleTextView.setText(movie.getOriginalTitle());

        if(!movie.getReleaseDate().isEmpty())
            movieDateTextView.setText(movie.getReleaseDate());

        if(!movie.getRating().isEmpty())
            movieRatingTextView.setText(movie.getRating());

        if(!movie.getOverview().isEmpty())
            movieSummaryTextView.setText(movie.getOverview());
    }


}
