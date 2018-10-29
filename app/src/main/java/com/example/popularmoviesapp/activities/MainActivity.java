package com.example.popularmoviesapp.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.adapters.MovieAdapter;
import com.example.popularmoviesapp.data.FavoritesDBHandler;
import com.example.popularmoviesapp.fragments.FavoritesFragment;
import com.example.popularmoviesapp.fragments.MovieFragment;
import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.utils.MovieUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    //Constants
    public static final String KEY_PATH = "path";
    public static final String KEY_MOVIE = "movie";
    public static final String MOVIE_ID = "movieId";

    //Views
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if(savedInstanceState == null)
        {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_PATH, MovieUtil.PATH_POPULAR);
            addMovieFragment(bundle);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Bundle bundle = new Bundle();

        switch (menuItem.getItemId())
        {
            case R.id.menu_popular:
                bundle.putString(KEY_PATH, MovieUtil.PATH_POPULAR);
                addMovieFragment(bundle);
                break;

            case R.id.menu_top_rated:
                bundle.putString(KEY_PATH, MovieUtil.PATH_TOP_RATED);
                addMovieFragment(bundle);
                break;

            case R.id.menu_favorites:
                addFavoritesFragment();
                break;
        }
        return true;
    }

    private void addMovieFragment(Bundle bundle)
    {
        //Create Fragment and add path
        Fragment fragment = new MovieFragment();
        fragment.setArguments(bundle);

        //Add Fragment to the frame
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    private void addFavoritesFragment()
    {
        Fragment fragment = new FavoritesFragment();

        //Add Fragment to the frame
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }

}
